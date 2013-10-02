/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadAnnotation;
import org.jboss.forge.parser.java.ReadAnnotation.Annotation;
import org.jboss.forge.parser.java.ReadEnumConstant.EnumConstant;
import org.jboss.forge.parser.java.ReadEnumConstant.EnumConstant.Body;
import org.jboss.forge.parser.java.ReadField;
import org.jboss.forge.parser.java.ReadField.Field;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.jboss.forge.parser.java.ReadJavaEnum.JavaEnum;
import org.jboss.forge.parser.java.ReadJavaSource;
import org.jboss.forge.parser.java.ReadMember.Member;
import org.jboss.forge.parser.java.ReadMethod;
import org.jboss.forge.parser.java.ReadMethod.Method;
import org.jboss.forge.parser.java.ReadParameter;
import org.jboss.forge.parser.java.ReadParameter.Parameter;
import org.jboss.forge.parser.java.SourceType;
import org.jboss.forge.parser.java.SyntaxError;
import org.jboss.forge.parser.java.Visibility;
import org.jboss.forge.parser.java.ast.MethodFinderVisitor;
import org.jboss.forge.parser.java.ast.TypeDeclarationFinderVisitor;
import org.jboss.forge.parser.java.util.Strings;
import org.jboss.forge.parser.java.util.Types;
import org.jboss.forge.parser.spi.JavaParserImpl;

class EnumConstantBodyImpl implements EnumConstant.Body
{
   private final EnumConstant enumConstant;
   private final JavaEnum javaEnum;

   EnumConstantBodyImpl(EnumConstant enumConstant)
   {
      this.enumConstant = enumConstant;
      this.javaEnum = enumConstant.getOrigin();
      getBody();
   }

   @Override
   public String getCanonicalName()
   {
      return javaEnum.getCanonicalName() + "." + enumConstant.getName();
   }

   @Override
   public String getQualifiedName()
   {
      return javaEnum.getQualifiedName() + "." + enumConstant.getName();
   }

   @Override
   public List<SyntaxError> getSyntaxErrors()
   {
      return javaEnum.getSyntaxErrors();
   }

   @Override
   public boolean hasSyntaxErrors()
   {
      return javaEnum.hasSyntaxErrors();
   }

   @Override
   public boolean isClass()
   {
      return true;
   }

   @Override
   public boolean isEnum()
   {
      return false;
   }

   @Override
   public boolean isInterface()
   {
      return false;
   }

   @Override
   public boolean isAnnotation()
   {
      return false;
   }

   @Override
   public JavaSource<?> getEnclosingType()
   {
      return javaEnum;
   }

   @Override
   public List<JavaSource<?>> getNestedClasses()
   {
      final JavaEnumImpl parentImpl = (JavaEnumImpl) javaEnum;
      Document document = parentImpl.document;
      CompilationUnit unit = parentImpl.unit;

      final List<JavaSource<?>> result = new ArrayList<JavaSource<?>>();

      @SuppressWarnings("unchecked")
      final List<BodyDeclaration> bodyDeclarations = getBody().bodyDeclarations();
      for (BodyDeclaration body : bodyDeclarations)
      {
         final List<AbstractTypeDeclaration> declarations = getNestedDeclarations(body);
         for (AbstractTypeDeclaration declaration : declarations)
         {
            result.add(JavaParserImpl.getJavaSource(this, document, unit, declaration));
         }
      }
      return result;
   }

   @Override
   public SourceType getSourceType()
   {
      return SourceType.CLASS;
   }

   @Override
   public String getPackage()
   {
      return javaEnum.getPackage();
   }

   @Override
   public Body setPackage(String name)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public Body setDefaultPackage()
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isDefaultPackage()
   {
      return javaEnum.isDefaultPackage();
   }

   @Override
   public Import addImport(String className)
   {
      return javaEnum.addImport(className);
   }

   @Override
   public Import addImport(Class<?> type)
   {
      return javaEnum.addImport(type);
   }

   @Override
   public Import addImport(Import imprt)
   {
      return javaEnum.addImport(imprt);
   }

   @Override
   public <T extends ReadJavaSource<?>> Import addImport(T type)
   {
      return javaEnum.addImport(type);
   }

   @Override
   public boolean hasImport(Class<?> type)
   {
      return javaEnum.hasImport(type);
   }

   @Override
   public boolean hasImport(String type)
   {
      return javaEnum.hasImport(type);
   }

   @Override
   public boolean requiresImport(Class<?> type)
   {
      return javaEnum.requiresImport(type);
   }

   @Override
   public boolean requiresImport(String type)
   {
      return javaEnum.requiresImport(type);
   }

   @Override
   public <T extends ReadJavaSource<T>> boolean hasImport(T type)
   {
      return javaEnum.hasImport(type);
   }

   @Override
   public boolean hasImport(Import imprt)
   {
      return javaEnum.hasImport(imprt);
   }

   @Override
   public Import getImport(String literalValue)
   {
      return javaEnum.getImport(literalValue);
   }

   @Override
   public Import getImport(Class<?> type)
   {
      return javaEnum.getImport(type);
   }

   @Override
   public <T extends ReadJavaSource<?>> Import getImport(T type)
   {
      return javaEnum.getImport(type);
   }

   @Override
   public Import getImport(Import imprt)
   {
      return javaEnum.getImport(imprt);
   }

   @Override
   public Body removeImport(String name)
   {
      javaEnum.removeImport(name);
      return this;
   }

   @Override
   public Body removeImport(Class<?> type)
   {
      javaEnum.removeImport(type);
      return this;
   }

   @Override
   public <T extends ReadJavaSource<?>> Body removeImport(T type)
   {
      javaEnum.removeImport(type);
      return this;
   }

   @Override
   public Body removeImport(Import imprt)
   {
      javaEnum.removeImport(imprt);
      return this;
   }

   @Override
   public List<Import> getImports()
   {
      return javaEnum.getImports();
   }

   @Override
   public String resolveType(String type)
   {
      return javaEnum.resolveType(type);
   }

   @Override
   public String getName()
   {
      return enumConstant.getName();
   }

   @Override
   public Body setName(String name)
   {
      enumConstant.setName(name);
      return this;
   }

   @Override
   public boolean isPackagePrivate()
   {
      return false;
   }

   @Override
   public Body setPackagePrivate()
   {
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return false;
   }

   @Override
   public Body setPublic()
   {
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return true;
   }

   @Override
   public Body setPrivate()
   {
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return false;
   }

   @Override
   public Body setProtected()
   {
      return this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.PRIVATE;
   }

   @Override
   public Body setVisibility(Visibility scope)
   {
      return this;
   }

   @Override
   public Annotation<Body> addAnnotation()
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      throw new UnsupportedOperationException();
   }

   @Override
   public Annotation<Body> addAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      throw new UnsupportedOperationException();
   }

   @Override
   public Annotation<Body> addAnnotation(String className)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      throw new UnsupportedOperationException();
   }

   @Override
   public List<Annotation<Body>> getAnnotations()
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return Collections.emptyList();
   }

   @Override
   public boolean hasAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return false;
   }

   @Override
   public boolean hasAnnotation(String type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return false;
   }

   @Override
   public Annotation<Body> getAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return null;
   }

   @Override
   public Annotation<Body> getAnnotation(String type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return null;
   }

   @Override
   public Body removeAnnotation(ReadAnnotation<Body> annotation)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return this;
   }

   @Override
   public Object getInternal()
   {
      return javaEnum.getInternal();
   }

   AnonymousClassDeclaration getBody()
   {
      final EnumConstantDeclaration enumConstantDeclaration = (EnumConstantDeclaration) enumConstant.getInternal();
      synchronized (enumConstantDeclaration)
      {
         AnonymousClassDeclaration result = enumConstantDeclaration.getAnonymousClassDeclaration();
         if (result == null)
         {
            final String stub = "enum StubEnum { FOO() {}; }";
            final JavaEnum temp = JavaParser.parse(JavaEnum.class, stub);
            final AnonymousClassDeclaration body = ((EnumConstantBodyImpl) temp.getEnumConstants().get(0).getBody())
                     .getBody();
            final AST ast = ((ASTNode) javaEnum.getInternal()).getAST();
            result = (AnonymousClassDeclaration) ASTNode.copySubtree(ast, body);
            enumConstantDeclaration.setAnonymousClassDeclaration(result);
         }
         return result;
      }
   }

   @Override
   public Body getOrigin()
   {
      return this;
   }

   @Override
   public List<Member<Body, ?>> getMembers()
   {
      final List<Member<Body, ?>> result = new ArrayList<Member<Body, ?>>();
      result.addAll(getFields());
      result.addAll(getMethods());
      return Collections.unmodifiableList(result);
   }

   @Override
   public Field<Body> addField()
   {
      Field<Body> field = new FieldImpl<Body>(this);
      addField(field);
      return field;
   }

   @Override
   public Field<Body> addField(final String declaration)
   {
      String stub = "public class Stub { " + declaration + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Field<JavaClass>> fields = temp.getFields();
      Field<Body> result = null;
      for (Field<JavaClass> stubField : fields)
      {
         Object variableDeclaration = stubField.getInternal();
         Field<Body> field = new FieldImpl<Body>(this, variableDeclaration, true);
         addField(field);
         if (result == null)
         {
            result = field;
         }
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   private void addField(ReadField<Body> field)
   {
      final List<BodyDeclaration> bodyDeclarations = getBody().bodyDeclarations();
      int idx = 0;
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (!(bodyDeclaration instanceof FieldDeclaration))
         {
            break;
         }
         idx++;
      }
      bodyDeclarations.add(idx, (BodyDeclaration) ((VariableDeclarationFragment) field.getInternal()).getParent());
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Field<Body>> getFields()
   {
      final List<Field<Body>> result = new ArrayList<Field<Body>>();

      final List<BodyDeclaration> bodyDeclarations = getBody().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof FieldDeclaration)
         {
            FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
            List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();
            for (VariableDeclarationFragment fragment : fragments)
            {
               result.add(new FieldImpl<Body>(this, fragment));
            }
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public Field<Body> getField(final String name)
   {
      for (Field<Body> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return field;
         }
      }
      return null;
   }

   @Override
   public boolean hasField(final String name)
   {
      for (ReadField<Body> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final ReadField<Body> field)
   {
      return getFields().contains(field);
   }

   @SuppressWarnings("unchecked")
   @Override
   public Body removeField(final ReadField<Body> field)
   {
      VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.getInternal();
      Iterator<Object> declarationsIterator = getBody().bodyDeclarations().iterator();
      while (declarationsIterator.hasNext())
      {
         Object next = declarationsIterator.next();
         if (next instanceof FieldDeclaration)
         {
            FieldDeclaration declaration = (FieldDeclaration) next;
            if (declaration.equals(fragment.getParent()))
            {
               List<VariableDeclarationFragment> fragments = declaration.fragments();
               if (fragments.contains(fragment))
               {
                  if (fragments.size() == 1)
                  {
                     declarationsIterator.remove();
                  }
                  else
                  {
                     fragments.remove(fragment);
                  }
                  break;
               }
            }
         }
      }
      return this;
   }

   @Override
   public boolean hasMethod(final ReadMethod<Body, ?> method)
   {
      return getMethods().contains(method);
   }

   @Override
   public boolean hasMethodSignature(final String name)
   {
      return hasMethodSignature(name, new String[] {});
   }

   @Override
   public boolean hasMethodSignature(final String name, final String... paramTypes)
   {
      return getMethod(name, paramTypes) != null;
   }

   @Override
   public boolean hasMethodSignature(final String name, Class<?>... paramTypes)
   {
      final String[] types = new String[paramTypes == null ? 0 : paramTypes.length];
      for (int i = 0; i < types.length; i++)
      {
         types[i] = paramTypes[i].getName();
      }

      return hasMethodSignature(name, types);
   }

   @Override
   public Method<Body> getMethod(final String name)
   {
      for (Method<Body> method : getMethods())
      {
         if (method.getName().equals(name) && (method.getParameters().isEmpty()))
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public Method<Body> getMethod(final String name, final String... paramTypes)
   {
      for (Method<Body> local : getMethods())
      {
         if (local.getName().equals(name))
         {
            final List<Parameter<Body>> localParams = local.getParameters();
            if (paramTypes != null)
            {
               if (localParams.isEmpty() || (localParams.size() == paramTypes.length))
               {
                  boolean matches = true;
                  for (int i = 0; i < localParams.size(); i++)
                  {
                     if (!Types.areEquivalent(localParams.get(i).getType(), paramTypes[i]))
                     {
                        matches = false;
                     }
                  }
                  if (matches)
                  {
                     return local;
                  }
               }
            }
         }
      }
      return null;
   }

   @Override
   public Method<Body> getMethod(final String name, Class<?>... paramTypes)
   {
      final String[] types = new String[paramTypes == null ? 0 : paramTypes.length];
      for (int i = 0; i < types.length; i++)
      {
         types[i] = paramTypes[i].getName();
      }

      return getMethod(name, types);
   }

   @Override
   public boolean hasMethodSignature(final ReadMethod<?, ?> method)
   {
      for (Method<Body> local : getMethods())
      {
         if (local.getName().equals(method.getName()))
         {
            final Iterator<Parameter<Body>> localParams = local.getParameters().iterator();
            for (ReadParameter<? extends ReadJavaSource<?>> methodParam : method.getParameters())
            {
               if (localParams.hasNext() && Strings.areEqual(localParams.next().getType(), methodParam.getType()))
               {
                  continue;
               }
               return false;
            }
            return !localParams.hasNext();
         }
      }
      return false;
   }

   @Override
   public Body removeMethod(final ReadMethod<Body, ?> method)
   {
      getBody().bodyDeclarations().remove(method.getInternal());
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<Body> addMethod()
   {
      final Method<Body> m = new MethodImpl<Body>(this);
      getBody().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<Body> addMethod(final String method)
   {
      final Method<Body> m = new MethodImpl<Body>(this, method);
      getBody().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public List<Method<Body>> getMethods()
   {
      final List<Method<Body>> result = new ArrayList<Method<Body>>();

      final MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      getBody().accept(methodFinderVisitor);

      for (MethodDeclaration methodDeclaration : methodFinderVisitor.getMethods())
      {
         result.add(new MethodImpl<Body>(this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   private List<AbstractTypeDeclaration> getNestedDeclarations(BodyDeclaration body)
   {
      final TypeDeclarationFinderVisitor typeDeclarationFinder = new TypeDeclarationFinderVisitor();
      body.accept(typeDeclarationFinder);
      final List<AbstractTypeDeclaration> declarations = typeDeclarationFinder.getTypeDeclarations();

      final List<AbstractTypeDeclaration> result = new ArrayList<AbstractTypeDeclaration>(declarations);
      if (!declarations.isEmpty())
      {
         // We don't want to return the current enum constant body's declaration.
         final AbstractTypeDeclaration first = declarations.remove(0);
         result.remove(first);
         for (AbstractTypeDeclaration declaration : declarations)
         {
            result.removeAll(getNestedDeclarations(declaration));
         }
      }
      return result;
   }

}