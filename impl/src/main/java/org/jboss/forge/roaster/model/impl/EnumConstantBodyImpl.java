/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

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
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.Parameter;
import org.jboss.forge.roaster.model.SyntaxError;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.ast.MethodFinderVisitor;
import org.jboss.forge.roaster.model.ast.TypeDeclarationFinderVisitor;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.EnumConstantSource.Body;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.model.util.Strings;
import org.jboss.forge.roaster.model.util.Types;
import org.jboss.forge.roaster.spi.JavaParserImpl;

@SuppressWarnings("unchecked")
class EnumConstantBodyImpl implements EnumConstantSource.Body
{
   private static final String ENUM_CONSTANTS_BODY_ALLOW_ONLY_CLASSES_TO_BE_ADDED = "Enum constants body allow only classes to be added ";
   private final EnumConstantSource enumConstant;
   private final JavaEnumSource javaEnum;

   EnumConstantBodyImpl(EnumConstantSource enumConstant)
   {
      this.enumConstant = enumConstant;
      this.javaEnum = enumConstant.getOrigin();
      getBody();
   }

   @Override
   public int getStartPosition()
   {
      return getBody().getStartPosition();
   }

   @Override
   public int getColumnNumber()
   {
      return ((JavaEnumImpl) javaEnum).unit.getColumnNumber(getStartPosition());
   }

   @Override
   public int getLineNumber()
   {
      return ((JavaEnumImpl) javaEnum).unit.getLineNumber(getStartPosition());
   }

   @Override
   public int getEndPosition()
   {
      return (getStartPosition() == -1) ? -1 : getStartPosition() + getBody().getLength();
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
   public <T extends JavaType<?>> Import addImport(T type)
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
   public <T extends JavaType<T>> boolean hasImport(T type)
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
   public <T extends JavaType<?>> Import getImport(T type)
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
   public <T extends JavaType<?>> Body removeImport(T type)
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
   public AnnotationSource<Body> addAnnotation()
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      throw new UnsupportedOperationException();
   }

   @Override
   public AnnotationSource<Body> addAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      throw new UnsupportedOperationException();
   }

   @Override
   public AnnotationSource<Body> addAnnotation(String className)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      throw new UnsupportedOperationException();
   }

   @Override
   public List<AnnotationSource<Body>> getAnnotations()
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
   public AnnotationSource<Body> getAnnotation(Class<? extends java.lang.annotation.Annotation> type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return null;
   }

   @Override
   public AnnotationSource<Body> getAnnotation(String type)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return null;
   }

   @Override
   public Body removeAnnotation(Annotation<Body> annotation)
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
      return this;
   }

   @Override
   public void removeAllAnnotations()
   {
      // could pass through to enumConstant, but would require then pretending its Annotation was ours
      // which should cause no problem at the moment, but could theoretically do so in the future
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
            final JavaEnumSource temp = Roaster.parse(JavaEnumSource.class, stub);
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
   public List<MemberSource<Body, ?>> getMembers()
   {
      final List<MemberSource<Body, ?>> result = new ArrayList<>();
      result.addAll(getFields());
      result.addAll(getMethods());
      return Collections.unmodifiableList(result);
   }

   @Override
   public FieldSource<Body> addField()
   {
      FieldSource<Body> field = new FieldImpl<>(this);
      addField(field);
      return field;
   }

   @Override
   public FieldSource<Body> addField(final String declaration)
   {
      String stub = "public class Stub { " + declaration + " }";
      JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
      List<FieldSource<JavaClassSource>> fields = temp.getFields();
      FieldSource<Body> result = null;
      for (FieldSource<JavaClassSource> stubField : fields)
      {
         Object variableDeclaration = stubField.getInternal();
         FieldSource<Body> field = new FieldImpl<>(this, variableDeclaration, true);
         addField(field);
         if (result == null)
         {
            result = field;
         }
      }
      return result;
   }

   private void addField(Field<Body> field)
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
   public List<FieldSource<Body>> getFields()
   {
      final List<FieldSource<Body>> result = new ArrayList<>();

      final List<BodyDeclaration> bodyDeclarations = getBody().bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof FieldDeclaration)
         {
            FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
            List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();
            for (VariableDeclarationFragment fragment : fragments)
            {
               result.add(new FieldImpl<>(this, fragment));
            }
         }
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public FieldSource<Body> getField(final String name)
   {
      for (FieldSource<Body> field : getFields())
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
      for (Field<Body> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final Field<Body> field)
   {
      return getFields().contains(field);
   }

   @Override
   public Body removeField(final Field<Body> field)
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
   public boolean hasMethod(final Method<Body, ?> method)
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
   public MethodSource<Body> getMethod(final String name)
   {
      for (MethodSource<Body> method : getMethods())
      {
         if (method.getName().equals(name) && (method.getParameters().isEmpty()))
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public MethodSource<Body> getMethod(final String name, final String... paramTypes)
   {
      for (MethodSource<Body> local : getMethods())
      {
         if (local.getName().equals(name))
         {
            final List<ParameterSource<Body>> localParams = local.getParameters();
            if (paramTypes != null)
            {
               if (localParams.isEmpty() || (localParams.size() == paramTypes.length))
               {
                  boolean matches = true;
                  for (int i = 0; i < localParams.size(); i++)
                  {
                     if (!Types.areEquivalent(localParams.get(i).getType().getName(), paramTypes[i]))
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
   public MethodSource<Body> getMethod(final String name, Class<?>... paramTypes)
   {
      final String[] types = new String[paramTypes == null ? 0 : paramTypes.length];
      for (int i = 0; i < types.length; i++)
      {
         types[i] = paramTypes[i].getName();
      }

      return getMethod(name, types);
   }

   @Override
   public boolean hasMethodSignature(final Method<?, ?> method)
   {
      for (MethodSource<Body> local : getMethods())
      {
         if (local.getName().equals(method.getName()))
         {
            final Iterator<ParameterSource<Body>> localParams = local.getParameters().iterator();
            for (Parameter<? extends JavaType<?>> methodParam : method.getParameters())
            {
               if (localParams.hasNext()
                        && Strings.areEqual(localParams.next().getType().getName(), methodParam.getType().getName()))
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
   public Body removeMethod(final Method<Body, ?> method)
   {
      getBody().bodyDeclarations().remove(method.getInternal());
      return this;
   }

   @Override
   public MethodSource<Body> addMethod()
   {
      final MethodSource<Body> m = new MethodImpl<>(this);
      getBody().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public MethodSource<Body> addMethod(final String method)
   {
      final MethodSource<Body> m = new MethodImpl<>(this, method);
      getBody().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public MethodSource<Body> addMethod(java.lang.reflect.Method method)
   {
      final MethodSource<Body> m = new MethodImpl<>(this, method);
      getBody().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public MethodSource<Body> addMethod(Method<?, ?> method)
   {
      MethodSource<Body> m = new MethodImpl<>(this, method.toString());
      getBody().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public List<MethodSource<Body>> getMethods()
   {
      final List<MethodSource<Body>> result = new ArrayList<>();

      final MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      getBody().accept(methodFinderVisitor);

      for (MethodDeclaration methodDeclaration : methodFinderVisitor.getMethods())
      {
         result.add(new MethodImpl<>(this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public JavaSource<?> getEnclosingType()
   {
      return javaEnum;
   }

   @Override
   public List<JavaSource<?>> getNestedTypes()
   {
      final JavaEnumImpl parentImpl = (JavaEnumImpl) javaEnum;
      Document document = parentImpl.document;
      CompilationUnit unit = parentImpl.unit;

      final List<JavaSource<?>> result = new ArrayList<>();

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

   private List<AbstractTypeDeclaration> getNestedDeclarations(BodyDeclaration body)
   {
      final TypeDeclarationFinderVisitor typeDeclarationFinder = new TypeDeclarationFinderVisitor();
      body.accept(typeDeclarationFinder);
      final List<AbstractTypeDeclaration> declarations = typeDeclarationFinder.getTypeDeclarations();

      final List<AbstractTypeDeclaration> result = new ArrayList<>(declarations);
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

   @Override
   public boolean hasNestedType(String name)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (nested.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasNestedType(JavaType<?> type)
   {
      if (!type.isClass())
      {
         return false;
      }
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getQualifiedName(), type.getQualifiedName())
                  || Strings.areEqual(nested.getName(), type.getName()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public JavaSource<?> getNestedType(String name)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getName(), name) || Strings.areEqual(nested.getQualifiedName(), name))
         {
            return nested;
         }
      }
      return null;
   }

   @Override
   public boolean hasNestedType(Class<?> type)
   {
      for (JavaSource<?> nested : getNestedTypes())
      {
         if (Strings.areEqual(nested.getName(), type.getSimpleName())
                  || Strings.areEqual(nested.getQualifiedName(), type.getName()))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(Class<NESTED_TYPE> type)
   {
      if (type != JavaClassSource.class)
      {
         throw new IllegalArgumentException(ENUM_CONSTANTS_BODY_ALLOW_ONLY_CLASSES_TO_BE_ADDED);
      }
      JavaSource<?> nestedType = Roaster.create(type);
      return (NESTED_TYPE) addNestedType(nestedType);
   }

   @Override
   public <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(String declaration)
   {
      JavaType<?> source = Roaster.parse(declaration);
      if (!source.isClass())
      {
         throw new IllegalArgumentException(ENUM_CONSTANTS_BODY_ALLOW_ONLY_CLASSES_TO_BE_ADDED);
      }
      JavaSource<?> nestedType = Roaster.parse(JavaSource.class, declaration);
      return (NESTED_TYPE) addNestedType(nestedType);
   }

   @Override
   public Body removeNestedType(JavaSource<?> type)
   {
      if (type instanceof AbstractJavaSource)
      {
         BodyDeclaration bodyDeclaration = ((AbstractJavaSource<?>) type).body;
         List<Object> bodyDeclarations = getBody().bodyDeclarations();
         bodyDeclarations.remove(bodyDeclaration);
      }
      return this;
   }

   @Override
   public <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(NESTED_TYPE type)
   {
      if (!type.isClass())
      {
         throw new IllegalArgumentException(ENUM_CONSTANTS_BODY_ALLOW_ONLY_CLASSES_TO_BE_ADDED);
      }
      if (type instanceof AbstractJavaSource)
      {
         List<Object> bodyDeclarations = getBody().bodyDeclarations();
         BodyDeclaration nestedBody = ((AbstractJavaSource<?>) type).body;
         bodyDeclarations.add(ASTNode.copySubtree(getBody().getAST(), nestedBody));
      }
      else
      {
         throw new IllegalArgumentException("type must be an AbstractJavaSource instance");
      }
      return (NESTED_TYPE) getNestedType(type.getName());
   }

   @Override
   public Import addImport(Type<?> type)
   {
      return getEnclosingType().addImport(type);
   }

   @Override
   public JavaDocSource<Body> getJavaDoc()
   {
      BodyDeclaration body = getFirstBodyDeclaration();
      Javadoc javadoc = body.getJavadoc();
      if (javadoc == null)
      {
         javadoc = body.getAST().newJavadoc();
         body.setJavadoc(javadoc);
      }
      return new JavaDocImpl<>(this, javadoc);
   }

   @Override
   public Body removeJavaDoc()
   {
      getFirstBodyDeclaration().setJavadoc(null);
      return this;
   }

   @Override
   public boolean hasJavaDoc()
   {
      return getFirstBodyDeclaration().getJavadoc() != null;
   }

   private BodyDeclaration getFirstBodyDeclaration()
   {
      return (BodyDeclaration) getBody().bodyDeclarations().get(0);
   }

   /**
    * Why? Is this a JavaType?
    */
   @Override
   public String toUnformattedString()
   {
      // do something unknown...
      return toString();
   }

}