/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadAnnotation;
import org.jboss.forge.parser.java.ReadAnnotation.Annotation;
import org.jboss.forge.parser.java.ReadEnumConstant.EnumConstant;
import org.jboss.forge.parser.java.ReadJavaEnum.JavaEnum;
import org.jboss.forge.parser.java.ast.AnnotationAccessor;
import org.jboss.forge.parser.java.util.Strings;

public class EnumConstantImpl implements EnumConstant
{
   private final AnnotationAccessor<JavaEnum, EnumConstant> annotations = new AnnotationAccessor<JavaEnum, EnumConstant>();
   private JavaEnum parent;
   private AST ast;
   private EnumConstantDeclaration enumConstant;

   private void init(final JavaEnum parent)
   {
      this.parent = parent;
      this.ast = ((ASTNode) parent.getInternal()).getAST();
   }
   
   public EnumConstantImpl(final JavaEnum parent) {
      init(parent);
      this.enumConstant = ast.newEnumConstantDeclaration();
   }
   
   public EnumConstantImpl(final JavaEnum parent, final String declaration)
   {
      init(parent);

      String stub = "public enum Stub { " + declaration + " }";
      JavaEnum temp = (JavaEnum) JavaParser.parse(stub);
      List<EnumConstant> constants = temp.getEnumConstants();
      EnumConstantDeclaration newField = (EnumConstantDeclaration) constants.get(0).getInternal();
      EnumConstantDeclaration subtree = (EnumConstantDeclaration) ASTNode.copySubtree(ast, newField);
      this.enumConstant = subtree;
   }
   
   public EnumConstantImpl(final JavaEnum parent, final Object internal)
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
   public EnumConstant setName(String name)
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
   public JavaEnum getOrigin()
   {
      return parent;
   }

   @Override
   public List<String> getConstructorArguments()
   {
      final List<String> result = new ArrayList<String>();
      for (Object o : enumConstant.arguments()) {
         result.add(o.toString());
      }
      return Collections.unmodifiableList(result);
   }

   @SuppressWarnings("unchecked")
   @Override
   public EnumConstant setConstructorArguments(String... literalArguments)
   {
      enumConstant.arguments().clear();
      if (literalArguments != null && literalArguments.length > 0)
      {
         final String stub = "public enum Stub { FOO(" + Strings.join(Arrays.asList(literalArguments), ", ") + "); }";
         final JavaEnum temp = JavaParser.parse(JavaEnum.class, stub);
         final List<EnumConstant> constants = temp.getEnumConstants();
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
   public EnumConstant removeBody()
   {
      enumConstant.setAnonymousClassDeclaration(null);
      return this;
   }

   /*
    * Annotation<O> Modifiers
    */

   @Override
   public Annotation<JavaEnum> addAnnotation()
   {
      return annotations.addAnnotation(this, enumConstant);
   }

   @Override
   public Annotation<JavaEnum> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (!parent.hasImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, enumConstant, clazz.getSimpleName());
   }

   @Override
   public Annotation<JavaEnum> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, enumConstant, className);
   }

   @Override
   public List<Annotation<JavaEnum>> getAnnotations()
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
   public EnumConstant removeAnnotation(final ReadAnnotation<JavaEnum> annotation)
   {
      return annotations.removeAnnotation(this, enumConstant, annotation);
   }

   @Override
   public Annotation<JavaEnum> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, enumConstant, type);
   }

   @Override
   public Annotation<JavaEnum> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, enumConstant, type);
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
