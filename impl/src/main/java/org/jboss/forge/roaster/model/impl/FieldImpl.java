/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.ast.AnnotationAccessor;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldImpl<O extends JavaSource<O>> implements FieldSource<O>
{
   private final AnnotationAccessor<O, FieldSource<O>> annotations = new AnnotationAccessor<>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private final O parent;
   private final AST ast;
   private final FieldDeclaration field;
   private final VariableDeclarationFragment fragment;
   private final CompilationUnit cu;

   public FieldImpl(final O parent)
   {
      this.parent = parent;
      this.cu = (CompilationUnit) parent.getInternal();
      this.ast = cu.getAST();
      this.fragment = ast.newVariableDeclarationFragment();
      this.field = ast.newFieldDeclaration(this.fragment);
   }

   public FieldImpl(final O parent, final Object internal)
   {
      this(parent, internal, false);
   }

   @SuppressWarnings("unchecked")
   public FieldImpl(final O parent, final Object internal, boolean copy)
   {
      this.parent = parent;
      this.cu = (CompilationUnit) parent.getInternal();
      this.ast = cu.getAST();
      if (copy)
      {
         VariableDeclarationFragment newFieldFragment = (VariableDeclarationFragment) internal;
         FieldDeclaration newFieldDeclaration = (FieldDeclaration) newFieldFragment.getParent();
         this.field = (FieldDeclaration) ASTNode.copySubtree(ast, newFieldDeclaration);
         Iterator<VariableDeclarationFragment> fragmentsIterator = this.field.fragments().iterator();
         VariableDeclarationFragment temp = null;
         while (fragmentsIterator.hasNext())
         {
            VariableDeclarationFragment variableDeclarationFragment = fragmentsIterator.next();
            if (newFieldFragment.getName().getFullyQualifiedName()
                     .equals(variableDeclarationFragment.getName().getFullyQualifiedName()))
            {
               temp = variableDeclarationFragment;
            }
            else
            {
               fragmentsIterator.remove();
            }
         }
         this.fragment = temp;
      }
      else
      {
         this.fragment = (VariableDeclarationFragment) internal;
         this.field = (FieldDeclaration) this.fragment.getParent();
      }
   }

   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return fragment;
   }

   /*
    * Annotation<O> Modifiers
    */
   @Override
   public AnnotationSource<O> addAnnotation()
   {
      return annotations.addAnnotation(this, field);
   }

   @Override
   public AnnotationSource<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, field, clazz.getSimpleName());
   }

   @Override
   public AnnotationSource<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, field, className);
   }

   @Override
   public List<AnnotationSource<O>> getAnnotations()
   {
      return annotations.getAnnotations(this, field);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, field, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, field, type);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, field, type);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, field, type);
   }

   @Override
   public FieldSource<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, field, annotation);
   }

   @Override
   public void removeAllAnnotations()
   {
      annotations.removeAllAnnotations(field);
   }

   @Override
   public String toString()
   {
      return field.toString();
   }

   /*
    * Visibility Modifiers
    */

   @Override
   public boolean isFinal()
   {
      return modifiers.hasModifier(field, ModifierKeyword.FINAL_KEYWORD);
   }

   @Override
   public FieldSource<O> setFinal(final boolean finl)
   {
      if (finl)
         modifiers.addModifier(field, ModifierKeyword.FINAL_KEYWORD);
      else
         modifiers.removeModifier(field, ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public boolean isStatic()
   {
      return getOrigin().isInterface() || modifiers.hasModifier(field, ModifierKeyword.STATIC_KEYWORD);
   }

   @Override
   public FieldSource<O> setStatic(final boolean statc)
   {
      if (statc)
         modifiers.addModifier(field, ModifierKeyword.STATIC_KEYWORD);
      else
         modifiers.removeModifier(field, ModifierKeyword.STATIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPackagePrivate()
   {
      return !isPublic() && !isPrivate() && !isProtected();
   }

   @Override
   public FieldSource<O> setPackagePrivate()
   {
      modifiers.clearVisibility(field);
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(field, ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public FieldSource<O> setPublic()
   {
      modifiers.clearVisibility(field);
      modifiers.addModifier(field, ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(field, ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public FieldSource<O> setPrivate()
   {
      modifiers.clearVisibility(field);
      modifiers.addModifier(field, ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(field, ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public FieldSource<O> setProtected()
   {
      modifiers.clearVisibility(field);
      modifiers.addModifier(field, ModifierKeyword.PROTECTED_KEYWORD);
      return this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public FieldSource<O> setVisibility(final Visibility scope)
   {
      return Visibility.set(this, scope);
   }

   /*
    * Field<O> methods
    */

   @Override
   public String getName()
   {
      String result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            result = frag.getName().getFullyQualifiedName();
            break;
         }
      }
      return result;
   }

   @Override
   public FieldSource<O> setName(final String name)
   {
      fragment.setName(ast.newSimpleName(name));
      return this;
   }

   @Override
   public Type<O> getType()
   {
      return new TypeImpl<>(parent, field.getStructuralProperty(FieldDeclaration.TYPE_PROPERTY));
   }

   @Override
   public FieldSource<O> setType(final Class<?> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return setType(clazz.getSimpleName());
   }

   @Override
   public FieldSource<O> setType(final JavaType<?> source)
   {
      return setType(source.getQualifiedName());
   }

   @Override
   public FieldSource<O> setType(String typeName)
   {
      O origin = getOrigin();
      Type<O> innerType = new TypeImpl<>(origin, null, typeName);
      Import imprt = getOrigin().addImport(innerType);
      String resolvedType = (imprt != null) ? Types.rebuildGenericNameWithArrays(imprt.getSimpleName(), innerType)
               : typeName;
      if (Types.isJavaLang(resolvedType))
      {
         resolvedType = Types.toSimpleName(typeName);
      }
      org.eclipse.jdt.core.dom.Type fieldType = TypeImpl.fromString(resolvedType, this.ast);
      field.setType(fieldType);

      return this;
   }

   @Override
   public String getLiteralInitializer()
   {
      Expression expression = fragment.getInitializer();
      return expression != null ? expression.toString() : null;
   }

   @Override
   public String getStringInitializer()
   {
      Expression expression = fragment.getInitializer();
      return expression != null ? Strings.unquote(expression.toString()) : null;
   }

   @Override
   public FieldSource<O> setLiteralInitializer(final String value)
   {
      String stub = "public class Stub { private Object stub = " + value + " }";
      JavaClass<?> temp = Roaster.parse(JavaClass.class, stub);
      VariableDeclarationFragment tempFrag = (VariableDeclarationFragment) temp.getFields().get(0).getInternal();
      fragment.setInitializer((Expression) ASTNode.copySubtree(ast, tempFrag.getInitializer()));
      return this;
   }

   @Override
   public FieldSource<O> setStringInitializer(final String value)
   {
      return setLiteralInitializer(Strings.enquote(value));
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((field == null) ? 0 : field.hashCode());
      result = prime * result + ((fragment == null) ? 0 : fragment.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      FieldImpl<?> other = (FieldImpl<?>) obj;
      if (field == null)
      {
         if (other.field != null)
            return false;
      }
      else if (!field.equals(other.field))
         return false;
      if (fragment == null)
      {
         if (other.fragment != null)
            return false;
      }
      else if (!fragment.equals(other.fragment))
         return false;
      return true;
   }

   @Override
   public boolean isTransient()
   {
      return modifiers.hasModifier(field, ModifierKeyword.TRANSIENT_KEYWORD);
   }

   @Override
   public FieldSource<O> setTransient(boolean value)
   {
      if (value)
         modifiers.addModifier(field, ModifierKeyword.TRANSIENT_KEYWORD);
      else
         modifiers.removeModifier(field, ModifierKeyword.TRANSIENT_KEYWORD);
      return this;
   }

   @Override
   public boolean isVolatile()
   {
      return modifiers.hasModifier(field, ModifierKeyword.VOLATILE_KEYWORD);
   }

   @Override
   public FieldSource<O> setVolatile(boolean value)
   {
      if (value)
         modifiers.addModifier(field, ModifierKeyword.VOLATILE_KEYWORD);
      else
         modifiers.removeModifier(field, ModifierKeyword.VOLATILE_KEYWORD);
      return this;
   }

   @Override
   public boolean hasJavaDoc()
   {
      return field.getJavadoc() != null;
   }

   @Override
   public FieldSource<O> removeJavaDoc()
   {
      field.setJavadoc(null);
      return this;
   }

   @Override
   public JavaDocSource<FieldSource<O>> getJavaDoc()
   {
      Javadoc javadoc = field.getJavadoc();
      if (javadoc == null)
      {
         javadoc = field.getAST().newJavadoc();
         field.setJavadoc(javadoc);
      }
      return new JavaDocImpl<>(this, javadoc);
   }

   @Override
   public int getStartPosition()
   {
      return field.getStartPosition();
   }

   @Override
   public int getEndPosition()
   {
      int startPosition = getStartPosition();
      return (startPosition == -1) ? -1 : startPosition + field.getLength();
   }

   @Override
   public int getLineNumber()
   {
      return cu.getLineNumber(getStartPosition());
   }

   @Override
   public int getColumnNumber()
   {
      return cu.getColumnNumber(getStartPosition());
   }
}