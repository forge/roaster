/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Problem;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.TypeVariable;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.ast.AnnotationAccessor;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodImpl<O extends JavaSource<O>> implements MethodSource<O>
{
   private final AnnotationAccessor<O, MethodSource<O>> annotations = new AnnotationAccessor<>();
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

   public MethodImpl(final O parent, final Method reflectMethod)
   {
      this(parent);
      // Set method visibility
      int mod = reflectMethod.getModifiers();
      if (Modifier.isPublic(mod))
      {
         setPublic();
      }
      else if (Modifier.isProtected(mod))
      {
         setProtected();
      }
      else if (Modifier.isPrivate(mod))
      {
         setPrivate();
      }
      setAbstract(Modifier.isAbstract(mod));
      setSynchronized(Modifier.isSynchronized(mod));
      setNative(Modifier.isNative(mod));
      // Set method return type
      if (reflectMethod.getReturnType() == Void.TYPE)
      {
         setReturnTypeVoid();
      }
      else
      {
         setReturnType(reflectMethod.getReturnType());
      }
      // Set method name
      setName(reflectMethod.getName());
      // Set method parameters
      Class<?>[] paramTypes = reflectMethod.getParameterTypes();
      String[] paramNames = Methods.generateParameterNames(paramTypes);
      for (int i = 0; i < paramTypes.length; i++)
      {
         addParameter(paramTypes[i], paramNames[i]);
      }
      // Set method body
      if (!isAbstract())
      {
         Methods.implementMethod(this);
      }
   }

   public MethodImpl(final O parent, final String method)
   {
      init(parent);

      String stub = "public class Stub { " + method + " }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<MethodSource<JavaClassSource>> methods = temp.getMethods();
      MethodDeclaration newMethod = (MethodDeclaration) methods.get(0).getInternal();
      MethodDeclaration subtree = (MethodDeclaration) ASTNode.copySubtree(cu.getAST(), newMethod);
      this.method = subtree;
   }

   @Override
   public String toSignature()
   {
      StringBuilder signature = new StringBuilder();
      signature.append(Visibility.PACKAGE_PRIVATE.equals(this.getVisibility().scope()) ? ""
               : this.getVisibility()
                        .scope());
      signature.append(" ");
      signature.append(this.getName()).append("(");
      List<ParameterSource<O>> parameters = this.getParameters();
      for (ParameterSource<O> p : parameters)
      {
         signature.append(p.getType().getName());
         if (parameters.indexOf(p) < (parameters.size() - 1))
         {
            signature.append(", ");
         }
      }

      signature.append(") : ").append((this.getReturnType() == null ? "void" : this.getReturnType().getName()));
      return signature.toString();
   }

   /*
    * Annotation<O> Modifiers
    */

   @Override
   public AnnotationSource<O> addAnnotation()
   {
      return annotations.addAnnotation(this, method);
   }

   @Override
   public AnnotationSource<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, method, clazz.getSimpleName());
   }

   @Override
   public AnnotationSource<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, method, className);
   }

   @Override
   public List<AnnotationSource<O>> getAnnotations()
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
   public MethodSource<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, method, annotation);
   }

   @Override
   public void removeAllAnnotations()
   {
      annotations.removeAllAnnotations(method);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, method, type);
   }

   @Override
   public AnnotationSource<O> getAnnotation(final String type)
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
      Block body = method.getBody();
      if (body != null)
      {
         StringBuilder result = new StringBuilder();
         List<Statement> statements = (List<Statement>) body.getStructuralProperty(Block.STATEMENTS_PROPERTY);
         for (Statement statement : statements)
         {
            result.append(statement).append(" ");
         }
         return result.toString().trim();
      }
      // No body found, probably because it's a native or an abstract method
      return null;
   }

   @Override
   public MethodSource<O> setBody(final String body)
   {
      if (body == null)
      {
         method.setBody(null);
      }
      else
      {
         List<Problem> problems = Roaster.validateSnippet(body);
         if (problems.size() > 0)
         {
            throw new ParserException(problems);
         }
         String stub = "public class Stub { public void method() {" + body + "} }";
         JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
         List<MethodSource<JavaClassSource>> methods = temp.getMethods();
         Block block = ((MethodDeclaration) methods.get(0).getInternal()).getBody();
         block = (Block) ASTNode.copySubtree(method.getAST(), block);
         method.setBody(block);
      }
      return this;
   }

   @Override
   public MethodSource<O> setConstructor(final boolean constructor)
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
   public Type<O> getReturnType()
   {
      if (isConstructor())
      {
         return null;
      }
      return new TypeImpl<>(parent, method.getReturnType2());
   }

   @Override
   public boolean isReturnTypeVoid()
   {
      return getReturnType() == null || getReturnType().isType(Void.TYPE);
   }

   @Override
   public MethodSource<O> setReturnType(Type<?> type)
   {
      return setReturnType(type.getQualifiedNameWithGenerics());
   }

   @Override
   public MethodSource<O> setReturnType(final Class<?> type)
   {
      return setReturnType(type.getCanonicalName());
   }

   @Override
   public MethodSource<O> setReturnTypeVoid()
   {
      return setReturnType(Void.TYPE);
   }

   @Override
   public MethodSource<O> setReturnType(final String typeName)
   {
      String simpleName = Types.toSimpleName(typeName);

      O origin = getOrigin();
      if (!hasTypeVariable(typeName) && !Strings.areEqual(typeName, simpleName)
               && origin.requiresImport(typeName))
      {
         origin.addImport(typeName);
      }
      for (String genericType : Types.splitGenerics(typeName))
      {
         if (!hasTypeVariable(genericType) && origin.requiresImport(genericType))
         {
            origin.addImport(genericType);
         }
      }
      String stub = "public class Stub { public " + simpleName + " method() {} }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<MethodSource<JavaClassSource>> methods = temp.getMethods();
      org.eclipse.jdt.core.dom.Type returnType = ((MethodDeclaration) methods.get(0).getInternal()).getReturnType2();

      returnType = (org.eclipse.jdt.core.dom.Type) ASTNode.copySubtree(method.getAST(), returnType);
      method.setReturnType2(returnType);

      return this;
   }

   @Override
   public MethodSource<O> setReturnType(final JavaType<?> type)
   {
      return setReturnType(type.getName());
   }

   @Override
   public boolean isAbstract()
   {
      return modifiers.hasModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public MethodSource<O> setAbstract(final boolean abstrct)
   {
      if (abstrct)
      {
         modifiers.addModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
         // Abstract methods do not specify a body
         setBody((String) null);
      }
      else
      {
         modifiers.removeModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
         if (getBody() == null)
         {
            setBody("");
         }
      }
      return this;
   }

   @Override
   public boolean isFinal()
   {
      return modifiers.hasModifier(method, ModifierKeyword.FINAL_KEYWORD);
   }

   @Override
   public MethodSource<O> setFinal(final boolean finl)
   {
      if (finl)
         modifiers.addModifier(method, ModifierKeyword.FINAL_KEYWORD);
      else
         modifiers.removeModifier(method, ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public boolean isDefault()
   {
      return modifiers.hasModifier(method, ModifierKeyword.DEFAULT_KEYWORD);
   }

   @Override
   public MethodSource<O> setDefault(boolean value)
   {
      if (value)
         modifiers.addModifier(method, ModifierKeyword.DEFAULT_KEYWORD);
      else
         modifiers.removeModifier(method, ModifierKeyword.DEFAULT_KEYWORD);
      return this;
   }

   @Override
   public boolean isStatic()
   {
      return modifiers.hasModifier(method, ModifierKeyword.STATIC_KEYWORD);
   }

   @Override
   public MethodSource<O> setStatic(final boolean statc)
   {
      if (statc)
         modifiers.addModifier(method, ModifierKeyword.STATIC_KEYWORD);
      else
         modifiers.removeModifier(method, ModifierKeyword.STATIC_KEYWORD);
      return this;
   }

   @Override
   public MethodSource<O> setSynchronized(boolean value)
   {
      if (value)
      {
         modifiers.addModifier(method, ModifierKeyword.SYNCHRONIZED_KEYWORD);
      }
      else
      {
         modifiers.removeModifier(method, ModifierKeyword.SYNCHRONIZED_KEYWORD);
      }
      return this;
   }

   @Override
   public boolean isSynchronized()
   {
      return modifiers.hasModifier(method, ModifierKeyword.SYNCHRONIZED_KEYWORD);
   }

   @Override
   public MethodSource<O> setNative(boolean value)
   {
      if (value)
      {
         modifiers.addModifier(method, ModifierKeyword.NATIVE_KEYWORD);
         // Native methods do not specify a body
         setBody((String) null);
      }
      else
      {
         modifiers.removeModifier(method, ModifierKeyword.NATIVE_KEYWORD);
         if (getBody() == null)
         {
            setBody("");
         }
      }
      return this;
   }

   @Override
   public boolean isNative()
   {
      return modifiers.hasModifier(method, ModifierKeyword.NATIVE_KEYWORD);
   }

   @Override
   public String getName()
   {
      return method.getName().getFullyQualifiedName();
   }

   @Override
   public MethodSource<O> setName(final String name)
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
   public MethodSource<O> setParameters(final String parameters)
   {
      String stub = "public class Stub { public void method( " + parameters + " ) {} }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<MethodSource<JavaClassSource>> methods = temp.getMethods();
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
   public List<ParameterSource<O>> getParameters()
   {
      List<ParameterSource<O>> results = new ArrayList<>();
      @SuppressWarnings("unchecked")
      List<SingleVariableDeclaration> parameters = method.parameters();
      for (SingleVariableDeclaration param : parameters)
      {
         results.add(new ParameterImpl<>(parent, param));
      }
      return Collections.unmodifiableList(results);
   }

   /*
    * Visibility Modifiers
    */

   @Override
   public boolean isPackagePrivate()
   {
      return !isPublic() && !isPrivate() && !isProtected();
   }

   @Override
   public MethodSource<O> setPackagePrivate()
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
   public MethodSource<O> setPublic()
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
   public MethodSource<O> setPrivate()
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
   public MethodSource<O> setProtected()
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
   public MethodSource<O> setVisibility(final Visibility scope)
   {
      return Visibility.set(this, scope);
   }

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
   public MethodSource<O> addThrows(final Class<? extends Exception> type)
   {
      return addThrows(type.getCanonicalName());
   }

   @Override
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public MethodSource<O> addThrows(final String type)
   {
      String simpleTypeName = Types.toSimpleName(type);

      O origin = getOrigin();
      if (!Strings.areEqual(type, simpleTypeName) && origin.requiresImport(type))
      {
         origin.addImport(type);
      }

      SimpleName simpleName = method.getAST().newSimpleName(simpleTypeName);

      List list = (List) method.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTION_TYPES_PROPERTY);
      list.add(method.getAST().newSimpleType(simpleName));

      return this;
   }

   @Override
   public List<String> getThrownExceptions()
   {
      ArrayList<String> result = new ArrayList<>();
      List<?> list = (List<?>) method.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTION_TYPES_PROPERTY);

      for (Object object : list)
      {
         result.add(object.toString());
      }

      return result;
   }

   @Override
   public MethodSource<O> removeThrows(final Class<? extends Exception> type)
   {
      return removeThrows(type.getName());
   }

   @Override
   public MethodSource<O> removeThrows(final String type)
   {
      List<?> list = (List<?>) method.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTION_TYPES_PROPERTY);

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

   @Override
   @SuppressWarnings("unchecked")
   public List<TypeVariableSource<O>> getTypeVariables()
   {
      List<TypeVariableSource<O>> result = new ArrayList<>();
      List<TypeParameter> typeParameters = method.typeParameters();
      if (typeParameters != null)
      {
         for (TypeParameter typeParameter : typeParameters)
         {
            result.add(new TypeVariableImpl<>(parent, typeParameter));
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   @SuppressWarnings("unchecked")
   public TypeVariableSource<O> getTypeVariable(String name)
   {
      List<TypeParameter> typeParameters = method.typeParameters();
      for (TypeParameter typeParameter : typeParameters)
      {
         if (Strings.areEqual(name, typeParameter.getName().getIdentifier()))
         {
            return new TypeVariableImpl<>(parent, typeParameter);
         }
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean hasTypeVariable(String name)
   {
      List<TypeParameter> typeParameters = method.typeParameters();
      for (TypeParameter typeParameter : typeParameters)
      {
         if (Strings.areEqual(name, typeParameter.getName().getIdentifier()))
         {
            return true;
         }
      }
      return false;
   }

   @SuppressWarnings("unchecked")
   @Override
   public TypeVariableSource<O> addTypeVariable()
   {
      TypeParameter tp2 = method.getAST().newTypeParameter();
      method.typeParameters().add(tp2);
      return new TypeVariableImpl<>(parent, tp2);
   }

   @Override
   public TypeVariableSource<O> addTypeVariable(String name)
   {
      return addTypeVariable().setName(name);
   }

   @Override
   public MethodSource<O> removeTypeVariable(String name)
   {
      @SuppressWarnings("unchecked")
      List<TypeParameter> typeParameters = method.typeParameters();
      for (Iterator<TypeParameter> iter = typeParameters.iterator(); iter.hasNext();)
      {
         if (Strings.areEqual(name, iter.next().getName().getIdentifier()))
         {
            iter.remove();
            break;
         }
      }
      return this;
   }

   @Override
   public MethodSource<O> removeTypeVariable(TypeVariable<?> typeVariable)
   {
      return removeTypeVariable(typeVariable.getName());
   }

   @Override
   public ParameterSource<O> addParameter(Class<?> type, String name)
   {
      return addParameter(type.getCanonicalName(), name);
   }

   @Override
   public ParameterSource<O> addParameter(JavaType<?> type, String name)
   {
      return addParameter(type.getQualifiedName(), name);
   }

   @SuppressWarnings("unchecked")
   @Override
   public ParameterSource<O> addParameter(String type, String name)
   {
      String resolvedType = type;
      if (!hasTypeVariable(type) && getOrigin().requiresImport(type))
      {
         Type<?> innerType = new TypeImpl<>(getOrigin(), null, type);
         Import imprt = getOrigin().addImport(innerType);
         resolvedType = imprt != null ? Types.rebuildGenericNameWithArrays(imprt.getSimpleName(), innerType)
                  : Types.toSimpleName(type);
      }

      String stub = "public class Stub { public void method( " + resolvedType + " " + name + " ) {} }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<MethodSource<JavaClassSource>> methods = temp.getMethods();
      List<VariableDeclaration> astParameters = ((MethodDeclaration) methods.get(0).getInternal()).parameters();

      ParameterSource<O> param = null;
      for (VariableDeclaration declaration : astParameters)
      {
         VariableDeclaration copy = (VariableDeclaration) ASTNode.copySubtree(method.getAST(), declaration);
         method.parameters().add(copy);
         param = new ParameterImpl<>(parent, copy);
      }
      return param;
   }

   @Override
   public MethodSource<O> removeParameter(ParameterSource<O> parameter)
   {
      method.parameters().remove(parameter.getInternal());
      return this;
   }

   @Override
   public MethodSource<O> removeParameter(Class<?> type, String name)
   {
      ParameterSource<O> parameter = null;
      for (ParameterSource<O> param : getParameters())
      {
         if (param.getType().isType(type) && param.getName().equals(name))
         {
            parameter = param;
            break;
         }
      }
      if (parameter != null)
      {
         removeParameter(parameter);
      }
      return this;
   }

   @Override
   public MethodSource<O> removeParameter(JavaType<?> type, String name)
   {
      ParameterSource<O> parameter = null;
      for (ParameterSource<O> param : getParameters())
      {
         if (param.getType().isType(type.getQualifiedName()) && param.getName().equals(name))
         {
            parameter = param;
            break;
         }
      }
      if (parameter != null)
      {
         removeParameter(parameter);
      }
      return this;
   }

   @Override
   public MethodSource<O> removeParameter(String type, String name)
   {
      ParameterSource<O> parameter = null;
      for (ParameterSource<O> param : getParameters())
      {
         if (param.getType().isType(type) && param.getName().equals(name))
         {
            parameter = param;
            break;
         }
      }
      if (parameter != null)
      {
         removeParameter(parameter);
      }
      return this;
   }

   @Override
   public boolean hasJavaDoc()
   {
      return method.getJavadoc() != null;
   }

   @Override
   public MethodSource<O> removeJavaDoc()
   {
      method.setJavadoc(null);
      return this;
   }

   @Override
   public JavaDocSource<MethodSource<O>> getJavaDoc()
   {
      Javadoc javadoc = method.getJavadoc();
      if (javadoc == null)
      {
         javadoc = method.getAST().newJavadoc();
         method.setJavadoc(javadoc);
      }
      return new JavaDocImpl<>(this, javadoc);
   }

   @Override
   public int getStartPosition()
   {
      return method.getStartPosition();
   }

   @Override
   public int getEndPosition()
   {
      int startPosition = getStartPosition();
      return (startPosition == -1) ? -1 : startPosition + method.getLength();
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