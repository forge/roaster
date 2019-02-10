/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.ast.AnnotationAccessor;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.util.Strings;

public class EnumConstantImpl implements EnumConstantSource
{
   private final AnnotationAccessor<JavaEnumSource, EnumConstantSource> annotations = new AnnotationAccessor<>();
   private JavaEnumSource parent;
   private AST ast;
   private final EnumConstantDeclaration enumConstant;

   private void init(final JavaEnumSource parent)
   {
      this.parent = parent;
      this.ast = ((ASTNode) parent.getInternal()).getAST();

   }

   public EnumConstantImpl(final JavaEnumSource parent)
   {
      init(parent);
      this.enumConstant = ast.newEnumConstantDeclaration();
   }

   public EnumConstantImpl(final JavaEnumSource parent, final String declaration)
   {
      init(parent);

      String stub = "public enum Stub { " + declaration + " }";
      JavaEnumSource temp = (JavaEnumSource) Roaster.parse(stub);
      List<EnumConstantSource> constants = temp.getEnumConstants();
      EnumConstantDeclaration newField = (EnumConstantDeclaration) constants.get(0).getInternal();
      EnumConstantDeclaration subtree = (EnumConstantDeclaration) ASTNode.copySubtree(ast, newField);
      this.enumConstant = subtree;
   }

   public EnumConstantImpl(final JavaEnumSource parent, final Object internal)
   {
      init(parent);
      this.enumConstant = (EnumConstantDeclaration) internal;
   }

   @Override
   public String getName()
   {
      return this.enumConstant.getName().getFullyQualifiedName();
   }

   @Override
   public EnumConstantSource setName(String name)
   {
      this.enumConstant.setName(ast.newSimpleName(name));
      return this;
   }

   @Override
   public Object getInternal()
   {
      return enumConstant;
   }

   @Override
   public JavaEnumSource getOrigin()
   {
      return parent;
   }

   @Override
   public List<String> getConstructorArguments()
   {
      final List<String> result = new ArrayList<>();
      for (Object o : enumConstant.arguments())
      {
         result.add(o.toString());
      }
      return Collections.unmodifiableList(result);
   }

   @SuppressWarnings("unchecked")
   @Override
   public EnumConstantSource setConstructorArguments(String... literalArguments)
   {
      enumConstant.arguments().clear();
      if (literalArguments != null && literalArguments.length > 0)
      {
         final String stub = "public enum Stub { FOO(" + Strings.join(Arrays.asList(literalArguments), ", ") + "); }";
         final JavaEnumSource temp = Roaster.parse(JavaEnumSource.class, stub);
         final List<EnumConstantSource> constants = temp.getEnumConstants();
         final EnumConstantDeclaration newConstant = (EnumConstantDeclaration) constants.get(0).getInternal();
         final List<Expression> arguments = newConstant.arguments();
         for (Expression argument : arguments)
         {
            final Expression subtree = (Expression) ASTNode.copySubtree(ast, argument);
            enumConstant.arguments().add(subtree);
         }
      }
      return this;
   }

   @Override
   public Body getBody()
   {
      return new EnumConstantBodyImpl(this);
   }

   @Override
   public EnumConstantSource removeBody()
   {
      enumConstant.setAnonymousClassDeclaration(null);
      return this;
   }

   /*
    * Annotation<O> Modifiers
    */

   @Override
   public AnnotationSource<JavaEnumSource> addAnnotation()
   {
      return annotations.addAnnotation(this, enumConstant);
   }

   @Override
   public AnnotationSource<JavaEnumSource> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (parent.requiresImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, enumConstant, clazz.getSimpleName());
   }

   @Override
   public AnnotationSource<JavaEnumSource> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, enumConstant, className);
   }

   @Override
   public List<AnnotationSource<JavaEnumSource>> getAnnotations()
   {
      return annotations.getAnnotations(this, enumConstant);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, enumConstant, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, enumConstant, type);
   }

   @Override
   public EnumConstantSource removeAnnotation(final Annotation<JavaEnumSource> annotation)
   {
      return annotations.removeAnnotation(this, enumConstant, annotation);
   }

   @Override
   public void removeAllAnnotations()
   {
      annotations.removeAllAnnotations(enumConstant);
   }

   @Override
   public AnnotationSource<JavaEnumSource> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, enumConstant, type);
   }

   @Override
   public AnnotationSource<JavaEnumSource> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, enumConstant, type);
   }

   @Override
   public JavaDocSource<EnumConstantSource> getJavaDoc()
   {
      Javadoc javadoc = enumConstant.getJavadoc();
      if (javadoc == null)
      {
         javadoc = enumConstant.getAST().newJavadoc();
         enumConstant.setJavadoc(javadoc);
      }
      return new JavaDocImpl<>(this, javadoc);
   }

   @Override
   public boolean hasJavaDoc()
   {
      return enumConstant.getJavadoc() != null;
   }

   @Override
   public EnumConstantSource removeJavaDoc()
   {
      enumConstant.setJavadoc(null);
      return this;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (!obj.getClass().equals(getClass()))
      {
         return false;
      }
      final EnumConstantImpl other = (EnumConstantImpl) obj;
      return other.enumConstant == enumConstant || other.enumConstant != null
               && other.enumConstant.equals(enumConstant);
   }

   @Override
   public int hashCode()
   {
      int result = 57 << 4;
      return result | (enumConstant == null ? 0 : enumConstant.hashCode());
   }

   @Override
   public String toString()
   {
      return enumConstant.toString();
   }
}