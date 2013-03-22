/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.parser.java.Type;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class TypeImpl<O extends JavaSource<O>> implements Type<O>
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
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
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
      List<Type<O>> result = new ArrayList<Type<O>>();
      org.eclipse.jdt.core.dom.Type type = this.type;

      if (type instanceof ArrayType)
      {
         type = ((ArrayType) type).getComponentType();
      }

      if (type instanceof ParameterizedType)
      {
         List<org.eclipse.jdt.core.dom.Type> arguments = ((ParameterizedType) type).typeArguments();
         for (org.eclipse.jdt.core.dom.Type t : arguments)
         {
            result.add(new TypeImpl<O>(origin, this, t));
         }
      }
      return result;
   }

   @Override
   public boolean isArray()
   {
      return type.isArrayType();
   }

   @Override
   public boolean isParameterized()
   {
      if (type instanceof ArrayType)
      {
         return ((ArrayType) type).getComponentType().isParameterizedType();
      }
      return type.isParameterizedType();
   }

   @Override
   public boolean isPrimitive()
   {
      if (type instanceof ArrayType)
      {
         return ((ArrayType) type).getComponentType().isPrimitiveType();
      }
      return type.isPrimitiveType();
   }

   @Override
   public boolean isQualified()
   {
      if (type instanceof ArrayType)
      {
         return ((ArrayType) type).getComponentType().isQualifiedType();
      }
      return type.isQualifiedType();
   }

   @Override
   public boolean isWildcard()
   {
      if (type instanceof ArrayType)
      {
         return ((ArrayType) type).getComponentType().isWildcardType();
      }
      return type.isWildcardType();
   }

   @Override
   public String getName()
   {
      String result = type.toString();
      if (isParameterized())
      {
         if (isArray())
         {
            result = Types.stripArray(result);
         }
         result = Types.stripGenerics(result);
         if (isArray())
         {
            result += "[]";
         }
      }
      return result;
   }

   @Override
   public String getQualifiedName()
   {
      String result = type.toString();
      return origin.resolveType(result);
   }

   @Override
   public Type<O> getParentType()
   {
      return parent;
   }

   @Override
   public int getArrayDimensions()
   {
      if (isArray())
      {
         return ((ArrayType) type).getDimensions();
      }
      return -1;
   }

   @Override
   public String toString()
   {
      return type.toString();
   }

}
