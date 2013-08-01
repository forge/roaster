/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.parser.java.EnumConstant;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.SourceType;

/**
 * Represents a Java Source File containing an Enum Type.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumImpl extends AbstractJavaSourceMemberHolder<JavaEnum> implements JavaEnum
{
   public JavaEnumImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   public List<EnumConstant<JavaEnum>> getEnumConstants()
   {
      List<EnumConstant<JavaEnum>> result = new ArrayList<EnumConstant<JavaEnum>>();

      for (Object o : (((EnumDeclaration) getBodyDeclaration()).enumConstants()))
      {
         EnumConstantDeclaration constant = (EnumConstantDeclaration) o;
         result.add(new EnumConstantImpl<JavaEnum>(this, constant));
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   @SuppressWarnings("unchecked")
   public EnumConstant<JavaEnum> addEnumConstant()
   {
      EnumConstantImpl<JavaEnum> enumConst = new EnumConstantImpl<JavaEnum>(this);
      EnumConstantImpl enumConst = new EnumConstantImpl(this);
      EnumDeclaration enumDeclaration = (EnumDeclaration) getBodyDeclaration();
      List<EnumConstantDeclaration> constants = enumDeclaration.enumConstants();
      constants.add((EnumConstantDeclaration) enumConst.getInternal());

      return enumConst;
   }

   @Override
   @SuppressWarnings("unchecked")
   public EnumConstant<JavaEnum> addEnumConstant(final String declaration)
   {
      EnumConstantImpl<JavaEnum> enumConst = new EnumConstantImpl<JavaEnum>(this, declaration);

      EnumDeclaration enumDeclaration = (EnumDeclaration) getBodyDeclaration();
      List<EnumConstantDeclaration> constants = enumDeclaration.enumConstants();
      constants.add((EnumConstantDeclaration) enumConst.getInternal());

      return enumConst;
   }

   @Override
   public EnumConstant<JavaEnum> getEnumConstant(String name)
   {
      for (EnumConstant<JavaEnum> enumConst : getEnumConstants())
      {
         if (enumConst.getName().equals(name))
         {
            return enumConst;
         }
      }
      return null;
   }

   @Override
   protected JavaEnum updateTypeNames(final String newName)
   {
      return this;
   }

   @Override
   public SourceType getSourceType()
   {
      return SourceType.ENUM;
   }
}
