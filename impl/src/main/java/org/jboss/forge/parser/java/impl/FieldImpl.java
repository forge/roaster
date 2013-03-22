/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.Visibility;
import org.jboss.forge.parser.java.ast.AnnotationAccessor;
import org.jboss.forge.parser.java.ast.ModifierAccessor;
import org.jboss.forge.parser.java.util.Strings;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldImpl<O extends JavaSource<O>> implements Field<O>
{
   private final AnnotationAccessor<O, Field<O>> annotations = new AnnotationAccessor<O, Field<O>>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private O parent;
   private AST ast;
   private final FieldDeclaration field;

   private void init(final O parent)
   {
      this.parent = parent;
      ast = ((ASTNode) parent.getInternal()).getAST();
   }

   public FieldImpl(final O parent)
   {
      init(parent);
      this.field = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());
   }

   public FieldImpl(final O parent, final String declaration)
   {
      init(parent);

      String stub = "public class Stub { " + declaration + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Field<JavaClass>> fields = temp.getFields();
      FieldDeclaration newField = (FieldDeclaration) fields.get(0).getInternal();
      FieldDeclaration subtree = (FieldDeclaration) ASTNode.copySubtree(ast, newField);
      this.field = subtree;
   }

   public FieldImpl(final O parent, final Object internal)
   {
      init(parent);
      this.field = (FieldDeclaration) internal;
   }

   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return field;
   }

   /*
    * Annotation<O> Modifiers
    */
   @Override
   public Annotation<O> addAnnotation()
   {
      return annotations.addAnnotation(this, field);
   }

   @Override
   public Annotation<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, field, clazz.getSimpleName());
   }

   @Override
   public Annotation<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, field, className);
   }

   @Override
   public List<Annotation<O>> getAnnotations()
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
   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, field, type);
   }

   @Override
   public Annotation<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, field, type);
   }

   @Override
   public Field<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, field, annotation);
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
   public Field<O> setFinal(final boolean finl)
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
      return modifiers.hasModifier(field, ModifierKeyword.STATIC_KEYWORD);
   }

   @Override
   public Field<O> setStatic(final boolean statc)
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
      return (!isPublic() && !isPrivate() && !isProtected());
   }

   @Override
   public Field<O> setPackagePrivate()
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
   public Field<O> setPublic()
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
   public Field<O> setPrivate()
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
   public Field<O> setProtected()
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
   public Field<O> setVisibility(final Visibility scope)
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
   public Field<O> setName(final String name)
   {
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            frag.setName(ast.newSimpleName(name));
            break;
         }
      }
      return this;
   }

   @Override
   public String getType()
   {
      return Types.toSimpleName(getQualifiedType());
   }

   @Override
   public String getQualifiedType()
   {
      Object type = field.getStructuralProperty(FieldDeclaration.TYPE_PROPERTY);
      return parent.resolveType(type.toString());
   }

   @Override
   public org.jboss.forge.parser.java.Type<O> getTypeInspector()
   {
      return new TypeImpl<O>(parent, field.getStructuralProperty(FieldDeclaration.TYPE_PROPERTY));
   }

   @Override
   public boolean isType(final Class<?> type)
   {
      if (Strings.areEqual(type.getName(), getQualifiedType()))
      {
         return true;
      }

      if (isPrimitive() && type.getSimpleName().equals(getType()))
      {
         return true;
      }

      String simpleName = type.getSimpleName();
      if (Strings.areEqual(simpleName, getQualifiedType())
               && (getOrigin().hasImport(type) || !getOrigin().requiresImport(type)))
      {
         return true;
      }
      return false;
   }

   @Override
   public boolean isType(final String name)
   {
      if (Strings.areEqual(name, getQualifiedType()))
      {
         return true;
      }

      if ((!Types.isQualified(name) || getOrigin().hasImport(name) || !getOrigin().requiresImport(name))
               && Types.areEquivalent(name, getQualifiedType()))
      {
         return true;
      }
      return false;
   }

   @Override
   public Field<O> setType(final Class<?> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return setType(clazz.getSimpleName());
   }

   @Override
   public Field<O> setType(final JavaSource<?> source)
   {
      return setType(source.getQualifiedName());
   }

   @Override
   public Field<O> setType(final String typeName)
   {
      String simpleName = Types.toSimpleName(typeName);

      O origin = getOrigin();
      if (!Strings.areEqual(typeName, simpleName) && origin.requiresImport(typeName))
      {
         origin.addImport(typeName);
      }

      Code primitive = PrimitiveType.toCode(typeName);

      Type type = null;
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
      field.setType(type);
      return this;
   }

   @Override
   public String getLiteralInitializer()
   {
      String result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            result = frag.getInitializer().toString();
            break;
         }
      }
      return result;
   }

   @Override
   public String getStringInitializer()
   {
      String result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            result = Strings.unquote(frag.getInitializer().toString());
            break;
         }
      }
      return result;
   }

   @Override
   public Field<O> setLiteralInitializer(final String value)
   {
      String stub = "public class Stub { private String stub = " + value + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      FieldDeclaration internal = (FieldDeclaration) temp.getFields().get(0).getInternal();

      for (Object f : internal.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment tempFrag = (VariableDeclarationFragment) f;
            VariableDeclarationFragment localFrag = getFragment(field);
            localFrag.setInitializer((Expression) ASTNode.copySubtree(ast, tempFrag.getInitializer()));
            break;
         }
      }

      return this;
   }

   @Override
   public Field<O> setStringInitializer(final String value)
   {
      return setLiteralInitializer(Strings.enquote(value));
   }

   private VariableDeclarationFragment getFragment(final FieldDeclaration field)
   {
      VariableDeclarationFragment result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            result = (VariableDeclarationFragment) f;
            break;
         }
      }
      return result;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((field == null) ? 0 : field.hashCode());
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
      FieldImpl<?> other = (FieldImpl<?>) obj;
      if (field == null)
      {
         if (other.field != null)
         {
            return false;
         }
      }
      else if (!field.equals(other.field))
      {
         return false;
      }
      return true;
   }

   /**
    * TODO: Should we deprecate this method in favor of {@link Field#getTypeInspector()#isPrimitive()} ?
    */
   @Override
   public boolean isPrimitive()
   {
      boolean result = false;
      Type type = field.getType();
      if (type != null)
      {
         result = type.isPrimitiveType();
      }
      return result;
   }
}
