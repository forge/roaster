/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.ast.AnnotationAccessor;
import org.jboss.forge.roaster.model.source.AnnotationElementSource;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Matt Benson
 */
public class AnnotationElementImpl implements AnnotationElementSource
{
   private class AnnotationValue extends AnnotationImpl<JavaAnnotationSource, JavaAnnotationSource>
   {

      AnnotationValue(JavaAnnotationSource parent)
      {
         super(parent);
      }

      AnnotationValue(JavaAnnotationSource parent, Object internal)
      {
         super(parent, internal);
      }

      @Override
      protected void replace(org.eclipse.jdt.core.dom.Annotation oldNode, org.eclipse.jdt.core.dom.Annotation newNode)
      {
         member.setDefault(newNode);
      }
   }

   private class DefaultValueImpl implements DefaultValue
   {

      @Override
      public String getString()
      {
         return Strings.unquote(getLiteral());
      }

      @Override
      public String getLiteral()
      {
         Expression expr = member.getDefault();
         return expr == null ? null : expr.toString();
      }

      @Override
      public <E extends Enum<E>> E getEnum(final Class<E> type)
      {
         return convertLiteralToEnum(type, getLiteral());
      }

      @Override
      public AnnotationSource<JavaAnnotationSource> getAnnotation()
      {
         Expression expr = member.getDefault();
         if (expr instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            return new AnnotationValue(parent, expr);
         }
         return null;
      }

      @Override
      public DefaultValue setLiteral(String value)
      {
         if (value == null)
         {
            member.setDefault(null);
         }
         else
         {
            String stub = "public @interface Stub { String stub() default " + value + "; }";
            JavaAnnotationSource temp = (JavaAnnotationSource) Roaster.parse(stub);
            AnnotationTypeMemberDeclaration internal = (AnnotationTypeMemberDeclaration) temp.getAnnotationElements()
                     .get(0).getInternal();
            member.setDefault((Expression) ASTNode.copySubtree(ast, internal.getDefault()));
         }
         return this;
      }

      @Override
      public DefaultValue setString(String value)
      {
         requireNonNull(value, "null not accepted");
         return setLiteral(Strings.enquote(value));
      }

      @SuppressWarnings("unchecked")
      @Override
      public <T extends Enum<T>> DefaultValue setEnum(T values)
      {
         return setEnumArray(values);
      }

      @Override
      public <T extends Enum<T>> DefaultValue setEnumArray(T... values)
      {
         requireNonNull(values, "null array not accepted");

         final List<String> literals = new ArrayList<>();

         for (Enum<?> value : values)
         {
            requireNonNull(value, "null value not accepted");

            getOrigin().addImport(value.getDeclaringClass());
            literals.add(value.getDeclaringClass().getSimpleName() + "." + value.name());
         }

         return setLiteral(literals.size() == 1 ? literals.get(0) : String.format("{%s}", Strings.join(literals, ",")));
      }

      @Override
      public AnnotationSource<JavaAnnotationSource> setAnnotation()
      {
         AnnotationValue result = new AnnotationValue(parent);
         member.setDefault((Expression) result.getInternal());
         return result;
      }

      @Override
      public <E extends Enum<E>> E[] getEnumArray(Class<E> type)
      {
         Expression expr = member.getDefault();
         if (expr instanceof ArrayInitializer)
         {
            final List<E> results = new ArrayList<>();
            @SuppressWarnings("unchecked")
            final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
            for (Expression arrayElement : arrayElements)
            {
               results.add(convertLiteralToEnum(type, arrayElement.toString()));
            }
            @SuppressWarnings("unchecked")
            final E[] result = (E[]) Array.newInstance(type, results.size());
            return results.toArray(result);
         }
         else if (expr != null)
         {
            final E instance = convertLiteralToEnum(type, expr.toString());
            if (type.isInstance(instance))
            {
               @SuppressWarnings("unchecked")
               final E[] result = (E[]) Array.newInstance(type, 1);
               result[0] = instance;
               return result;
            }
         }
         return null;
      }

      @Override
      public DefaultValue setSingleClass(Class<?> value)
      {
         return setClassArray(value);
      }

      @Override
      public DefaultValue setClassArray(Class<?>... values)
      {
         requireNonNull(values, "null array not accepted");

         final List<String> literals = new ArrayList<>();
         for (Class<?> value : values)
         {
            requireNonNull(value, "null value not accepted");

            if (!value.isPrimitive())
            {
               getOrigin().addImport(value);
            }
            literals.add(value.getSimpleName() + ".class");
         }
         return setLiteral(literals.size() == 1 ? literals.get(0) : String.format("{%s}", Strings.join(literals, ",")));
      }

      private <E extends Enum<E>> E convertLiteralToEnum(final Class<E> type, String literalValue)
      {
         for (E inst : type.getEnumConstants())
         {
            String[] tokens = literalValue.split("\\.");
            if (tokens.length > 1)
            {
               literalValue = tokens[tokens.length - 1];
            }

            if (inst.name().equals(literalValue))
            {
               return inst;
            }
         }
         return null;

      }

      @Override
      public Class<?> getSingleClass()
      {
         final Expression expr = member.getDefault();
         if (expr instanceof TypeLiteral)
         {
            return resolveTypeLiteral((TypeLiteral) expr);
         }
         return null;
      }

      @Override
      public Class<?>[] getClassArray()
      {
         final Expression expr = member.getDefault();
         if (expr instanceof ArrayInitializer)
         {
            final List<Class<?>> result = new ArrayList<>();
            @SuppressWarnings("unchecked")
            final List<Expression> arrayElements = ((ArrayInitializer) expr).expressions();
            for (Expression arrayElement : arrayElements)
            {
               result.add(resolveTypeLiteral((TypeLiteral) arrayElement));
            }
            return result.toArray(new Class[result.size()]);
         }
         if (expr instanceof TypeLiteral)
         {
            return new Class[] { resolveTypeLiteral((TypeLiteral) expr) };
         }
         return null;
      }

      private Class<?> resolveTypeLiteral(TypeLiteral typeLiteral)
      {
         final Type<JavaAnnotationSource> type = new TypeImpl<>(getOrigin(), typeLiteral.getType());
         if (type.isPrimitive())
         {
            final Class<?>[] primitiveTypes = { boolean.class, byte.class, short.class, int.class, long.class,
                     float.class, double.class };
            for (Class<?> c : primitiveTypes)
            {
               if (c.getSimpleName().equals(type.getName()))
               {
                  return c;
               }
            }
            return null;
         }

         final String classname = type.getQualifiedName();

         try
         {
            return Class.forName(getOrigin().resolveType(classname));
         }
         catch (ClassNotFoundException e)
         {
            return null;
         }
      }
   }

   private final AnnotationAccessor<JavaAnnotationSource, AnnotationElementSource> annotations = new AnnotationAccessor<>();

   private final JavaAnnotationSource parent;
   private final AST ast;
   private final AnnotationTypeMemberDeclaration member;

   public AnnotationElementImpl(final JavaAnnotationSource parent)
   {
      this(parent, ((ASTNode) parent.getInternal()).getAST().newAnnotationTypeMemberDeclaration());
   }

   public AnnotationElementImpl(final JavaAnnotationSource parent, final String declaration)
   {
      this(parent, parseElement(parent, declaration));
   }

   public AnnotationElementImpl(final JavaAnnotationSource parent, final Object internal)
   {
      this.parent = parent;
      ast = ((ASTNode) parent.getInternal()).getAST();
      member = (AnnotationTypeMemberDeclaration) internal;
   }

   private static AnnotationTypeMemberDeclaration parseElement(JavaAnnotationSource parent, String declaration)
   {
      if (!declaration.trim().endsWith(";"))
      {
         declaration = declaration + ";";
      }
      String stub = "public @interface Stub { " + declaration + " }";
      JavaAnnotationSource temp = (JavaAnnotationSource) Roaster.parse(stub);
      List<AnnotationElementSource> fields = temp.getAnnotationElements();
      AnnotationTypeMemberDeclaration newField = (AnnotationTypeMemberDeclaration) fields.get(0).getInternal();
      return (AnnotationTypeMemberDeclaration) ASTNode.copySubtree(((ASTNode) parent.getInternal()).getAST(), newField);
   }

   @Override
   public JavaAnnotationSource getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return member;
   }

   /*
    * Annotation<O> Modifiers
    */
   @Override
   public AnnotationSource<JavaAnnotationSource> addAnnotation()
   {
      return annotations.addAnnotation(this, member);
   }

   @Override
   public AnnotationSource<JavaAnnotationSource> addAnnotation(
            final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, member, clazz.getSimpleName());
   }

   @Override
   public AnnotationSource<JavaAnnotationSource> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, member, className);
   }

   @Override
   public List<AnnotationSource<JavaAnnotationSource>> getAnnotations()
   {
      return annotations.getAnnotations(this, member);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, member, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, member, type);
   }

   @Override
   public AnnotationSource<JavaAnnotationSource> getAnnotation(
            final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, member, type);
   }

   @Override
   public AnnotationSource<JavaAnnotationSource> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, member, type);
   }

   @Override
   public AnnotationElementSource removeAnnotation(final Annotation<JavaAnnotationSource> annotation)
   {
      return annotations.removeAnnotation(this, member, annotation);
   }

   @Override
   public void removeAllAnnotations()
   {
      annotations.removeAllAnnotations(member);
   }

   @Override
   public String toString()
   {
      return member.toString();
   }

   /*
    * AnnotationElement methods
    */

   @Override
   public String getName()
   {
      return member.getName().toString();
   }

   @Override
   public AnnotationElementSource setName(final String name)
   {
      member.setName(ast.newSimpleName(name));
      return this;
   }

   @Override
   public Type<JavaAnnotationSource> getType()
   {
      return new TypeImpl<>(parent,
               member.getStructuralProperty(AnnotationTypeMemberDeclaration.TYPE_PROPERTY));
   }

   @Override
   public AnnotationElementSource setType(final Class<?> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return setType(clazz.getSimpleName());
   }

   @Override
   public AnnotationElementSource setType(final JavaType<?> source)
   {
      return setType(source.getQualifiedName());
   }

   @Override
   public AnnotationElementSource setType(final String typeName)
   {
      String simpleName = Types.toSimpleName(typeName);

      JavaAnnotationSource origin = getOrigin();
      if (!Strings.areEqual(typeName, simpleName) && origin.requiresImport(typeName))
      {
         origin.addImport(typeName);
      }

      Code primitive = PrimitiveType.toCode(typeName);

      org.eclipse.jdt.core.dom.Type type = null;
      if (primitive != null)
      {
         type = ast.newPrimitiveType(primitive);
      }
      else
      {
         if (!origin.requiresImport(typeName))
         {
            if (Types.isArray(typeName))
            {
               String arrayType = Types.stripArray(typeName);
               int arrayDimension = Types.getArrayDimension(typeName);
               if (Types.isPrimitive(arrayType))
               {
                  type = ast.newArrayType(ast.newPrimitiveType(PrimitiveType.toCode(arrayType)), arrayDimension);
               }
               else
               {
                  type = ast.newArrayType(ast.newSimpleType(ast.newSimpleName(arrayType)), arrayDimension);
               }
            }
            else
            {
               type = ast.newSimpleType(ast.newSimpleName(simpleName));
            }
         }
         else
         {
            String[] className = Types.tokenizeClassName(typeName);
            Name name = ast.newName(className);
            type = ast.newSimpleType(name);
         }
      }
      member.setType(type);
      return this;
   }

   @Override
   public DefaultValue getDefaultValue()
   {
      return new DefaultValueImpl();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((member == null) ? 0 : member.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      AnnotationElementImpl other = (AnnotationElementImpl) obj;
      if (member == null)
      {
         if (other.member != null)
         {
            return false;
         }
      }
      else if (!member.equals(other.member))
      {
         return false;
      }
      return true;
   }
}