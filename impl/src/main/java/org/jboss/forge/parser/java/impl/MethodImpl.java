/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.parser.java.Parameter;
import org.jboss.forge.parser.java.Type;
import org.jboss.forge.parser.java.Visibility;
import org.jboss.forge.parser.java.ast.AnnotationAccessor;
import org.jboss.forge.parser.java.ast.ModifierAccessor;
import org.jboss.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodImpl<O extends JavaSource<O>> implements Method<O>
{
   private final AnnotationAccessor<O, Method<O>> annotations = new AnnotationAccessor<O, Method<O>>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private O parent = null;
   private AST ast = null;
   private CompilationUnit cu = null;
   private final MethodDeclaration method;

   private void init(final O parent)
   {
      this.parent = parent;
      cu = (CompilationUnit) parent.getInternal();
      ast = cu.getAST();
   }

   public MethodImpl(final O parent)
   {
      init(parent);
      method = ast.newMethodDeclaration();
      method.setConstructor(false);
   }

   public MethodImpl(final O parent, final Object internal)
   {
      init(parent);
      method = (MethodDeclaration) internal;
   }

   public MethodImpl(final O parent, final String method)
   {
      init(parent);

      String stub = "public class Stub { " + method + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      MethodDeclaration newMethod = (MethodDeclaration) methods.get(0).getInternal();
      MethodDeclaration subtree = (MethodDeclaration) ASTNode.copySubtree(cu.getAST(), newMethod);
      this.method = subtree;
   }

   @Override
   public String toSignature()
   {
      String signature = (Visibility.PACKAGE_PRIVATE.equals(this.getVisibility().scope()) ? "" : this.getVisibility()
               .scope()) + " ";
      signature += this.getName() + "(";

      List<Parameter<O>> parameters = this.getParameters();
      for (Parameter<O> p : parameters)
      {
         signature += p.getType();
         if (parameters.indexOf(p) < (parameters.size() - 1))
         {
            signature += ", ";
         }
      }

      signature += ") : " + (this.getReturnType() == null ? "void" : this.getReturnType());
      return signature;
   }

   /*
    * Annotation<O> Modifiers
    */

   @Override
   public Annotation<O> addAnnotation()
   {
      return annotations.addAnnotation(this, method);
   }

   @Override
   public Annotation<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (!parent.hasImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, method, clazz.getSimpleName());
   }

   @Override
   public Annotation<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, method, className);
   }

   @Override
   public List<Annotation<O>> getAnnotations()
   {
      return annotations.getAnnotations(this, method);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, method, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, method, type);
   }

   @Override
   public Method<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, method, annotation);
   }

   @Override
   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, method, type);
   }

   @Override
   public Annotation<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, method, type);
   }

   /*
    * Method<O> Modifiers
    */

   @Override
   @SuppressWarnings("unchecked")
   public String getBody()
   {
      String result = "";

      List<Statement> statements = (List<Statement>) method.getBody().getStructuralProperty(Block.STATEMENTS_PROPERTY);
      for (Statement statement : statements)
      {
         result += statement + " ";
      }

      return result;
   }

   @Override
   public Method<O> setBody(final String body)
   {
      String stub = "public class Stub { public void method() {" + body + "} }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      Block block = ((MethodDeclaration) methods.get(0).getInternal()).getBody();

      block = (Block) ASTNode.copySubtree(method.getAST(), block);
      method.setBody(block);

      return this;
   }

   @Override
   public Method<O> setConstructor(final boolean constructor)
   {
      method.setConstructor(constructor);
      if (isConstructor())
      {
         method.setName(ast.newSimpleName(parent.getName()));
      }
      return this;
   }

   @Override
   public boolean isConstructor()
   {
      return method.isConstructor();
   }

   @Override
   public String getReturnType()
   {
      return Types.toSimpleName(getQualifiedReturnType());
   }

   @Override
   public String getQualifiedReturnType()
   {
      String result = null;
      org.eclipse.jdt.core.dom.Type returnType = method.getReturnType2();

      if (returnType != null)
      {
         if ("void".equals(returnType.toString()))
         {
            return null;
         }
         else if (!isConstructor())
         {
            result = returnType.toString();
         }
      }

      result = parent.resolveType(result);

      return result;
   }

   @Override
   public Type<O> getReturnTypeInspector()
   {
      return new TypeImpl<O>(parent, method.getReturnType2());
   }

   @Override
   public Method<O> setReturnType(final Class<?> type)
   {
      return setReturnType(type.getSimpleName());
   }

   @Override
   public Method<O> setReturnType(final String typeName)
   {
      String stub = "public class Stub { public " + typeName + " method() {} }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      org.eclipse.jdt.core.dom.Type returnType = ((MethodDeclaration) methods.get(0).getInternal()).getReturnType2();

      returnType = (org.eclipse.jdt.core.dom.Type) ASTNode.copySubtree(method.getAST(), returnType);
      method.setReturnType2(returnType);

      return this;
   }

   @Override
   public Method<O> setReturnType(final JavaSource<?> type)
   {
      return setReturnType(type.getName());
   }

   @Override
   public boolean isReturnTypeVoid()
   {
      return getReturnType() == null;
   }

   @Override
   public Method<O> setReturnTypeVoid()
   {
      method.setReturnType2(null);
      return this;
   }

   /*
    * Abstract Modifiers
    */

   @Override
   public boolean isAbstract()
   {
      return modifiers.hasModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public Method<O> setAbstract(final boolean abstrct)
   {
      if (abstrct)
      {
         modifiers.addModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
      }
      else
      {
         modifiers.removeModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
      }
      return this;
   }

   @Override
   public boolean isFinal()
   {
      return modifiers.hasModifier(method, ModifierKeyword.FINAL_KEYWORD);
   }

   @Override
   public Method<O> setFinal(final boolean finl)
   {
      if (finl)
         modifiers.addModifier(method, ModifierKeyword.FINAL_KEYWORD);
      else
         modifiers.removeModifier(method, ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public boolean isStatic()
   {
      return modifiers.hasModifier(method, ModifierKeyword.STATIC_KEYWORD);
   }

   @Override
   public Method<O> setStatic(final boolean statc)
   {
      if (statc)
         modifiers.addModifier(method, ModifierKeyword.STATIC_KEYWORD);
      else
         modifiers.removeModifier(method, ModifierKeyword.STATIC_KEYWORD);
      return this;
   }

   @Override
   public String getName()
   {
      return method.getName().getFullyQualifiedName();
   }

   @Override
   public Method<O> setName(final String name)
   {
      if (method.isConstructor())
      {
         throw new IllegalStateException("Cannot set the name of a constructor.");
      }
      method.setName(ast.newSimpleName(name));
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<O> setParameters(final String parameters)
   {
      String stub = "public class Stub { public void method( " + parameters + " ) {} }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      List<VariableDeclaration> astParameters = ((MethodDeclaration) methods.get(0).getInternal()).parameters();

      method.parameters().clear();
      for (VariableDeclaration declaration : astParameters)
      {
         VariableDeclaration copy = (VariableDeclaration) ASTNode.copySubtree(method.getAST(), declaration);
         method.parameters().add(copy);
      }

      return this;
   }

   @Override
   public List<Parameter<O>> getParameters()
   {
      List<Parameter<O>> results = new ArrayList<Parameter<O>>();
      @SuppressWarnings("unchecked")
      List<SingleVariableDeclaration> parameters = method.parameters();
      for (SingleVariableDeclaration param : parameters)
      {
         results.add(new ParameterImpl<O>(parent, param));
      }
      return Collections.unmodifiableList(results);
   }

   /*
    * Visibility Modifiers
    */

   @Override
   public boolean isPackagePrivate()
   {
      return (!isPublic() && !isPrivate() && !isProtected());
   }

   @Override
   public Method<O> setPackagePrivate()
   {
      modifiers.clearVisibility(method);
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(method, ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public Method<O> setPublic()
   {
      modifiers.clearVisibility(method);
      modifiers.addModifier(method, ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(method, ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public Method<O> setPrivate()
   {
      modifiers.clearVisibility(method);
      modifiers.addModifier(method, ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(method, ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public Method<O> setProtected()
   {
      modifiers.clearVisibility(method);
      modifiers.addModifier(method, ModifierKeyword.PROTECTED_KEYWORD);
      return this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public Method<O> setVisibility(final Visibility scope)
   {
      return Visibility.set(this, scope);
   }

   /*
    * Interfaces
    */

   @Override
   public String toString()
   {
      return method.toString();
   }

   @Override
   public Object getInternal()
   {
      return method;
   }

   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((method == null) ? 0 : method.hashCode());
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
      MethodImpl<?> other = (MethodImpl<?>) obj;
      if (method == null)
      {
         if (other.method != null)
         {
            return false;
         }
      }
      else if (!method.equals(other.method))
      {
         return false;
      }
      return true;
   }

   @Override
   public Method<O> addThrows(final Class<? extends Exception> type)
   {
      return addThrows(type.getName());
   }

   @Override
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Method<O> addThrows(final String type)
   {
      String packg = Types.getPackage(type);
      String name = Types.toSimpleName(type);

      if (!packg.isEmpty())
      {
         getOrigin().addImport(type);
      }

      SimpleName simpleName = method.getAST().newSimpleName(name);

      List list = (List) method.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTIONS_PROPERTY);
      list.add(simpleName);

      return this;
   }

   @Override
   public List<String> getThrownExceptions()
   {
      ArrayList<String> result = new ArrayList<String>();
      List<?> list = (List<?>) method.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTIONS_PROPERTY);

      for (Object object : list)
      {
         result.add(object.toString());
      }

      return result;
   }

   @Override
   public Method<O> removeThrows(final Class<? extends Exception> type)
   {
      return removeThrows(type.getName());
   }

   @Override
   public Method<O> removeThrows(final String type)
   {
      List<?> list = (List<?>) method.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTIONS_PROPERTY);

      for (Object object : list)
      {
         String thrown = object.toString();
         if (type.equals(thrown))
         {
            list.remove(object);
            return this;
         }
         else if (Types.areEquivalent(type, thrown))
         {
            if (!Types.isQualified(type) && getOrigin().hasImport(thrown))
            {
               list.remove(object);
               return this;
            }
            else if (!Types.isQualified(thrown) && getOrigin().hasImport(type))
            {
               list.remove(object);
               return this;
            }
            else if (!getOrigin().hasImport(type) && !getOrigin().hasImport(thrown))
            {
               list.remove(object);
               return this;
            }
         }
      }

      return this;
   }
}
