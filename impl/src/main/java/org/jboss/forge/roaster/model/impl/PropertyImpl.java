/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TextElement;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.model.source.PropertyHolderSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.jboss.forge.roaster.model.util.Strings;

/**
 * Implementation of PropertySource.
 *
 * @author mbenson
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 *
 * @param <O>
 */
class PropertyImpl<O extends JavaSource<O> & PropertyHolderSource<O>> implements PropertySource<O>
{

   private final O origin;
   private String name;

   PropertyImpl(String name, O origin)
   {
      super();
      this.origin = origin;
      this.name = name;
   }

   @Override
   public Object getInternal()
   {
      return getOrigin().getInternal();
   }

   @Override
   public O getOrigin()
   {
      return origin;
   }

   @Override
   public String getName()
   {
      return name == null ? "<missing>" : name;
   }

   @Override
   public Type<O> getType()
   {
      if (isAccessible())
      {
         return getAccessor().getReturnType();
      }
      if (isMutable())
      {
         return getMutator().getParameters().get(0).getType();
      }
      if (hasField())
      {
         return getField().getType();
      }
      return null;
   }

   @Override
   public boolean hasField()
   {
      return getField() != null;
   }

   @Override
   public FieldSource<O> getField()
   {
      final FieldSource<O> field = getOrigin().getField(name);
      if (field != null && !field.isStatic())
      {
         return field;
      }
      return null;
   }

   @Override
   public boolean isAccessible()
   {
      return getAccessor() != null;
   }

   @Override
   public boolean isMutable()
   {
      return getMutator() != null;
   }

   @Override
   public MethodSource<O> getAccessor()
   {
      for (MethodSource<O> method : getOrigin().getMethods())
      {
         if (isAccessor(method))
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public MethodSource<O> getMutator()
   {
      final Type<O> type;
      if (hasField())
      {
         type = getField().getType();
      }
      else if (isAccessible())
      {
         type = getAccessor().getReturnType();
      }
      else
      {
         type = null;
      }

      for (MethodSource<O> method : getOrigin().getMethods())
      {
         if (isMutator(method))
         {
            if (type == null
                     || Strings.areEqual(type.getQualifiedName(), method.getParameters().get(0).getType()
                              .getQualifiedName()))
            {
               return method;
            }
         }
      }
      return null;
   }

   @Override
   public MethodSource<O> createAccessor()
   {
      if (getAccessor() != null)
      {
         throw new IllegalStateException("Accessor method already exists");
      }

      final Type<O> type = getType();
      final String accessorName = methodName(type.isType(boolean.class) ? "is" : "get", name);
      final MethodSource<O> result = getOrigin().addMethod().setReturnType(typeName())
               .setName(accessorName);

      if (!getOrigin().isInterface())
      {
         result.setVisibility(Visibility.PUBLIC);
         if (hasField())
         {
            final String body = String.format("return %s;", getName());
            result.setBody(body);
         }
      }
      return result;
   }

   @Override
   public MethodSource<O> createMutator()
   {
      if (getMutator() != null)
      {
         throw new IllegalStateException("Mutator method already exists");
      }

      final String mutatorName = methodName("set", name);
      final String parameters = String.format("%s %s", typeName(), getName());

      final MethodSource<O> result = getOrigin().addMethod().setReturnTypeVoid().setName(mutatorName)
               .setParameters(parameters);

      if (!getOrigin().isInterface())
      {
         result.setVisibility(Visibility.PUBLIC);
         if (hasField())
         {
            final String body = String.format("this.%1$s = %1$s;", getName());
            result.setBody(body);
         }
      }
      return result;
   }

   @Override
   public FieldSource<O> createField()
   {
      if (getOrigin().isInterface())
      {
         throw new IllegalStateException("An interface cannot declare a nonstatic field");
      }
      if (getField() != null)
      {
         throw new IllegalStateException("Field already exists");
      }
      final FieldSource<O> result = getOrigin().addField().setVisibility(Visibility.PRIVATE).setType(typeName())
               .setName(name);
      if (getOrigin().isEnum())
      {
         result.setFinal(true);
      }
      if (isAccessible() && !getAccessor().isAbstract())
      {
         removeAccessor();
         createAccessor();
      }
      if (isMutable() && !getMutator().isAbstract())
      {
         removeMutator();
         createMutator();
      }
      return result;
   }

   @Override
   public PropertySource<O> setName(final String name)
   {
      if (Strings.isBlank(name))
      {
         throw new IllegalStateException("Property name cannot be null/empty/blank");
      }

      if (hasField())
      {
         getField().setName(name);
      }

      final String oldName = this.name;
      final boolean visitDocTags = true;

      final ASTVisitor renameVisitor = new ASTVisitor(visitDocTags)
      {
         @Override
         public boolean visit(SimpleName node)
         {
            if (Strings.areEqual(oldName, node.getIdentifier()))
            {
               node.setIdentifier(name);
            }
            return super.visit(node);
         }

         @Override
         public boolean visit(TextElement node)
         {
            final String text = node.getText();
            if (!text.contains(oldName))
            {
               return super.visit(node);
            }
            final int matchLength = oldName.length();
            final int textLength = text.length();
            final StringBuilder buf = new StringBuilder(text.length());
            final ParsePosition pos = new ParsePosition(0);

            while (pos.getIndex() < textLength)
            {
               final int index = pos.getIndex();
               final char c = text.charAt(index);
               if (Character.isJavaIdentifierStart(c))
               {
                  final int next = index + matchLength;

                  if (next <= textLength && Strings.areEqual(oldName, text.substring(index, next)))
                  {
                     buf.append(name);
                     pos.setIndex(next);
                     continue;
                  }
               }
               buf.append(c);
               pos.setIndex(index + 1);
            }

            node.setText(buf.toString());
            return super.visit(node);
         }
      };

      if (isAccessible())
      {
         final MethodSource<O> accessor = getAccessor();
         final String prefix = accessor.getReturnType().isType(boolean.class) ? "is" : "get";
         accessor.setName(methodName(prefix, name));
         ((MethodDeclaration) accessor.getInternal()).accept(renameVisitor);
      }

      if (isMutable())
      {
         final MethodSource<O> mutator = getMutator();
         mutator.setName(methodName("set", name));
         ((MethodDeclaration) mutator.getInternal()).accept(renameVisitor);
      }

      this.name = name;

      return this;
   }

   @Override
   public PropertySource<O> setType(Class<?> clazz)
   {
      return setType(clazz.getName());
   }

   @Override
   public PropertySource<O> setType(String type)
   {
      final MethodSource<O> accessor = getAccessor();
      final MethodSource<O> mutator = getMutator();
      final FieldSource<O> field = getField();

      if (accessor != null)
      {
         final Type<O> originalType = accessor.getReturnType();
         accessor.setReturnType(type);
         if (originalType.isType(boolean.class) || accessor.getReturnType().isType(boolean.class))
         {
            // potential name change:
            final String accessorName = methodName(accessor.getReturnType().isType(boolean.class) ? "is" : "get",
                     getName());
            accessor.setName(accessorName);
         }
      }
      if (mutator != null)
      {
         for (ParameterSource<O> param : mutator.getParameters())
            mutator.removeParameter(param);
         mutator.addParameter(type, getName());
      }
      if (field != null)
      {
         field.setType(type);
      }
      return this;
   }

   @Override
   public PropertySource<O> setType(JavaType<?> entity)
   {
      return setType(entity.getQualifiedName());
   }

   @Override
   public PropertySource<O> setAccessible(boolean accessible)
   {
      if (isAccessible() != accessible)
      {
         if (accessible)
         {
            createAccessor();
         }
         else
         {
            removeAccessor();
         }
      }
      return this;
   }

   @Override
   public PropertySource<O> setMutable(boolean mutable)
   {
      if (isMutable() != mutable)
      {
         if (mutable)
         {
            if (hasField())
            {
               getField().setFinal(false);
            }
            createMutator();
         }
         else
         {
            if (hasField())
            {
               getField().setFinal(true);
            }
            removeMutator();
         }
      }
      return this;
   }

   @Override
   public PropertySource<O> removeAccessor()
   {
      if (isAccessible())
      {
         getOrigin().removeMethod(getAccessor());
      }
      return this;
   }

   @Override
   public PropertySource<O> removeMutator()
   {
      if (isMutable())
      {
         getOrigin().removeMethod(getMutator());
      }
      return this;
   }

   @Override
   public PropertySource<O> removeField()
   {
      if (hasField())
      {
         getOrigin().removeField(getField());
      }
      return this;
   }

   @Override
   public String toString()
   {
      return "Property: " + getName();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (!(obj instanceof PropertyImpl<?>))
      {
         return false;
      }
      final PropertyImpl<?> other = (PropertyImpl<?>) obj;
      return getOrigin() == other.getOrigin() && Strings.areEqual(getName(), other.getName());
   }

   @Override
   public int hashCode()
   {
      // compatible with Java 6:
      return Arrays.hashCode(new Object[] { getOrigin(), getName() });
   }

   /**
    * Helpful method to determine whether this object actually represents a real property.
    */
   public boolean isValid()
   {
      return hasField() || isAccessible() || isMutable();
   }

   private String typeName()
   {
      final Type<O> type = getType();
      return type == null ? "<missing>" : type.toString();
   }

   private boolean isAccessor(Method<O, ?> method)
   {
      if (method.isConstructor())
      {
         return false;
      }
      if (method.isReturnTypeVoid())
      {
         return false;
      }
      if (method.getParameters().isEmpty())
      {
         if (method.getReturnType().isType(boolean.class) && Strings.areEqual(method.getName(), methodName("is", name)))
         {
            return true;
         }
         return Strings.areEqual(method.getName(), methodName("get", name));
      }
      return false;
   }

   private boolean isMutator(Method<O, ?> method)
   {
      if (method.isConstructor())
      {
         return false;
      }
      return method.isReturnTypeVoid() && method.getParameters().size() == 1
               && Strings.areEqual(method.getName(), methodName("set", name));
   }

   private static String methodName(String prefix, String property)
   {
      return prefix + Strings.capitalize(property);
   }

   @Override
   public Annotation<O> getAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      Annotation<O> ann = null;
      FieldSource<O> field = getField();
      if (field != null)
      {
         ann = field.getAnnotation(type);
      }
      if (ann == null)
      {
         MethodSource<O> accessor = getAccessor();
         if (accessor != null)
         {
            ann = accessor.getAnnotation(type);
         }
      }
      if (ann == null)
      {
         MethodSource<O> mutator = getMutator();
         if (mutator != null)
         {
            ann = mutator.getAnnotation(type);
         }
      }
      return ann;
   }

   @Override
   public Annotation<O> getAnnotation(String type)
   {
      Annotation<O> ann = null;
      FieldSource<O> field = getField();
      if (field != null)
      {
         ann = field.getAnnotation(type);
      }
      if (ann == null)
      {
         MethodSource<O> accessor = getAccessor();
         if (accessor != null)
         {
            ann = accessor.getAnnotation(type);
         }
      }
      if (ann == null)
      {
         MethodSource<O> mutator = getMutator();
         if (mutator != null)
         {
            ann = mutator.getAnnotation(type);
         }
      }
      return ann;
   }

   @Override
   public List<? extends Annotation<O>> getAnnotations()
   {
      List<Annotation<O>> annotations = new ArrayList<>();
      FieldSource<O> field = getField();
      if (field != null)
      {
         List<AnnotationSource<O>> fieldAnnotations = field.getAnnotations();
         annotations.addAll(fieldAnnotations);
      }
      MethodSource<O> accessor = getAccessor();
      if (accessor != null)
      {
         List<AnnotationSource<O>> accessorAnnotations = accessor.getAnnotations();
         annotations.addAll(accessorAnnotations);
      }
      MethodSource<O> mutator = getMutator();
      if (mutator != null)
      {
         List<AnnotationSource<O>> mutatorAnnotations = mutator.getAnnotations();
         annotations.addAll(mutatorAnnotations);
      }
      return annotations;
   }

   @Override
   public boolean hasAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      boolean hasAnnotation = false;
      FieldSource<O> field = getField();
      if (field != null)
      {
         hasAnnotation = field.hasAnnotation(type);
      }
      if (!hasAnnotation)
      {
         MethodSource<O> accessor = getAccessor();
         if (accessor != null)
         {
            hasAnnotation = accessor.hasAnnotation(type);
         }
      }
      if (!hasAnnotation)
      {
         MethodSource<O> mutator = getMutator();
         if (mutator != null)
         {
            hasAnnotation = mutator.hasAnnotation(type);
         }
      }
      return hasAnnotation;
   }

   @Override
   public boolean hasAnnotation(String type)
   {
      boolean hasAnnotation = false;
      FieldSource<O> field = getField();
      if (field != null)
      {
         hasAnnotation = field.hasAnnotation(type);
      }
      if (!hasAnnotation)
      {
         MethodSource<O> accessor = getAccessor();
         if (accessor != null)
         {
            hasAnnotation = accessor.hasAnnotation(type);
         }
      }
      if (!hasAnnotation)
      {
         MethodSource<O> mutator = getMutator();
         if (mutator != null)
         {
            hasAnnotation = mutator.hasAnnotation(type);
         }
      }
      return hasAnnotation;
   }
}
