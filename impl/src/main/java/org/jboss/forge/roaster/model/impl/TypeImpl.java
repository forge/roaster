/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.Importer;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class TypeImpl<O extends JavaType<O>> implements Type<O>
{
   private O origin;
   private CompilationUnit cu;

   private final Type<O> parent;
   private final org.eclipse.jdt.core.dom.Type type;

   private void init(final O origin)
   {
      this.origin = origin;
      cu = (CompilationUnit) origin.getInternal();
   }

   public TypeImpl(final O origin, final Object internal)
   {
      init(origin);
      type = (org.eclipse.jdt.core.dom.Type) internal;
      parent = null;
   }

   public TypeImpl(final O origin, final Type<O> parent, final String type)
   {
      init(origin);
      this.parent = parent;

      String stub = "public class Stub { private " + type + " getType(){return null;} }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<MethodSource<JavaClassSource>> methods = temp.getMethods();
      MethodDeclaration newMethod = (MethodDeclaration) methods.get(0).getInternal();
      org.eclipse.jdt.core.dom.Type subtree = (org.eclipse.jdt.core.dom.Type) ASTNode.copySubtree(cu.getAST(),
               newMethod.getReturnType2());
      this.type = subtree;
   }

   public TypeImpl(final O origin, final Type<O> parent, final Object internal)
   {
      init(origin);
      this.parent = parent;
      type = (org.eclipse.jdt.core.dom.Type) internal;
   }

   @Override
   public O getOrigin()
   {
      return origin;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Type<O>> getTypeArguments()
   {
      org.eclipse.jdt.core.dom.Type typeLocal = this.type;

      if (typeLocal.isArrayType())
      {
         typeLocal = ((ArrayType) typeLocal).getElementType();
      }

      if (typeLocal instanceof ParameterizedType)
      {
         List<Type<O>> result = new ArrayList<>();
         List<org.eclipse.jdt.core.dom.Type> arguments = ((ParameterizedType) typeLocal).typeArguments();
         for (org.eclipse.jdt.core.dom.Type t : arguments)
         {
            result.add(new TypeImpl<>(origin, this, t));
         }
         return Collections.unmodifiableList(result);
      }
      return Collections.emptyList();
   }

   @Override
   public boolean isArray()
   {
      return getArrayDimensions() > 0;
   }

   @Override
   public boolean isParameterized()
   {
      if (type.isArrayType())
      {
         return ((ArrayType) type).getElementType().isParameterizedType();
      }
      return type.isParameterizedType();
   }

   @Override
   public boolean isPrimitive()
   {
      if (type.isArrayType())
      {
         return ((ArrayType) type).getElementType().isPrimitiveType();
      }
      return type.isPrimitiveType();
   }

   @Override
   public boolean isQualified()
   {
      return type.toString().contains(".");
   }

   @Override
   public boolean isWildcard()
   {
      if (type.isArrayType())
      {
         return ((ArrayType) type).getElementType().isWildcardType();
      }
      return type.isWildcardType();
   }

   @Override
   public String getName()
   {
      String result = type.toString();
      if (isParameterized())
      {
         // strip type parameters after stripping array dimensions
         if (isArray())
         {
            result = Types.stripArray(result);
         }
         result = Types.stripGenerics(result);
         // restore array dimensions
         for (int i = 0, dim = getArrayDimensions(); i < dim; i++)
         {
            result += "[]";
         }
         return result;
      }
      for (int i = 0, dim = getExtraDimensions(); i < dim; i++)
      {
         result += "[]";
      }
      return result;
   }

   @Override
   public String getQualifiedName()
   {
      String result = type.toString();
      if (origin instanceof Importer<?>)
      {
         return ((Importer<?>) origin).resolveType(result);
      }
      return result;
   }

   @Override
   public String getSimpleName()
   {
      return Types.toSimpleName(getQualifiedName());
   }

   @Override
   public String getQualifiedNameWithGenerics()
   {
      String result = type.toString();
      if (origin instanceof Importer<?>)
      {
         return Types.rebuildGenericNameWithArrays(((Importer<?>) origin).resolveType(result), this);
      }
      return result;
   }

   @Override
   public Type<O> getParentType()
   {
      return parent;
   }

   @Override
   public int getArrayDimensions()
   {
      int result = 0;

      if (type.isArrayType())
      {
         result += ((ArrayType) type).getDimensions();
      }
      result += getExtraDimensions();
      return result;
   }

   @Override
   public boolean isType(final Class<?> type)
   {
      final String simpleName = type.getSimpleName();
      if (!getName().contains(simpleName))
      {
         return false;
      }

      if (isPrimitive() && type.isPrimitive() && simpleName.equals(getName()))
      {
         return true;
      }

      final String qualifiedName = getQualifiedName();

      if (Strings.areEqual(type.getName(), qualifiedName))
      {
         return true;
      }

      if (getOrigin() instanceof Importer<?> && Strings.areEqual(simpleName, qualifiedName))
      {
         return !((Importer<?>) getOrigin()).requiresImport(type);
      }
      return false;
   }

   @Override
   public boolean isType(final String name)
   {
      if (Strings.areEqual(name, getQualifiedName()))
      {
         return true;
      }

      if (Types.areEquivalent(name, getQualifiedName()))
      {
         if (!Types.isQualified(name))
         {
            return true;
         }
         return getOrigin() instanceof Importer<?> && !((Importer<?>) getOrigin()).requiresImport(name);
      }
      return false;
   }

   @Override
   public String toString()
   {
      return type.toString();
   }

   private int getExtraDimensions()
   {
      ASTNode parentLocal = type.getParent();
      if (parentLocal instanceof FieldDeclaration)
      {
         for (Object f : ((FieldDeclaration) parentLocal).fragments())
         {
            if (f instanceof VariableDeclarationFragment)
            {
               return ((VariableDeclarationFragment) f).getExtraDimensions();
            }
         }
      }
      if (parentLocal instanceof SingleVariableDeclaration)
      {
         return ((SingleVariableDeclaration) parentLocal).getExtraDimensions();
      }
      return 0;
   }

   public static org.eclipse.jdt.core.dom.Type fromString(String resolvedType, AST ast)
   {
      String stub = "public class Stub { " + resolvedType + " field; }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<FieldSource<JavaClassSource>> fields = temp.getFields();
      org.eclipse.jdt.core.dom.Type fieldType = ((FieldDeclaration) ((VariableDeclarationFragment) fields.get(0)
               .getInternal()).getParent()).getType();
      return (org.eclipse.jdt.core.dom.Type) ASTNode.copySubtree(ast, fieldType);
   }
}
