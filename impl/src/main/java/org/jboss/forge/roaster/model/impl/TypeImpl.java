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
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
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
   private O origin = null;
   private final Type<O> parent;

   @SuppressWarnings("unused")
   private AST ast = null;

   private CompilationUnit cu = null;
   private final org.eclipse.jdt.core.dom.Type type;

   private void init(final O origin)
   {
      this.origin = origin;
      cu = (CompilationUnit) origin.getInternal();
      ast = cu.getAST();
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
      org.eclipse.jdt.core.dom.Type type = this.type;

      if (type.isArrayType())
      {
         type = ((ArrayType) type).getElementType();
      }

      if (type instanceof ParameterizedType)
      {
         List<Type<O>> result = new ArrayList<Type<O>>();
         List<org.eclipse.jdt.core.dom.Type> arguments = ((ParameterizedType) type).typeArguments();
         for (org.eclipse.jdt.core.dom.Type t : arguments)
         {
            result.add(new TypeImpl<O>(origin, this, t));
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
      if (type.isArrayType())
      {
         return ((ArrayType) type).getElementType().isQualifiedType();
      }
      return type.isQualifiedType();
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
      final String qualifiedName = getQualifiedName();

      if (Strings.areEqual(type.getName(), qualifiedName))
      {
         return true;
      }

      final String simpleName = type.getSimpleName();

      if (isPrimitive() && type.isPrimitive() && simpleName.equals(getName()))
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
      ASTNode parent = type.getParent();
      if (parent instanceof FieldDeclaration)
      {
         for (Object f : ((FieldDeclaration) parent).fragments())
         {
            if (f instanceof VariableDeclarationFragment)
            {
               return ((VariableDeclarationFragment) f).getExtraDimensions();
            }
         }
      }
      return 0;
   }
}
