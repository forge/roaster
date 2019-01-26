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

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a Java Source File containing an Enum Type.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumImpl extends AbstractJavaSourceMemberHolder<JavaEnumSource>implements JavaEnumSource
{
   public JavaEnumImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   public List<EnumConstantSource> getEnumConstants()
   {
      List<EnumConstantSource> result = new ArrayList<>();

      for (Object o : ((EnumDeclaration) getDeclaration()).enumConstants())
      {
         EnumConstantDeclaration constant = (EnumConstantDeclaration) o;
         result.add(new EnumConstantImpl(this, constant));
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   @SuppressWarnings("unchecked")
   public EnumConstantSource addEnumConstant()
   {
      EnumConstantImpl enumConst = new EnumConstantImpl(this);
      EnumDeclaration enumDeclaration = (EnumDeclaration) getDeclaration();
      List<EnumConstantDeclaration> constants = enumDeclaration.enumConstants();
      constants.add((EnumConstantDeclaration) enumConst.getInternal());

      return enumConst;
   }

   @Override
   @SuppressWarnings("unchecked")
   public EnumConstantSource addEnumConstant(final String declaration)
   {
      EnumConstantImpl enumConst = new EnumConstantImpl(this, declaration);

      EnumDeclaration enumDeclaration = (EnumDeclaration) getDeclaration();
      List<EnumConstantDeclaration> constants = enumDeclaration.enumConstants();
      constants.add((EnumConstantDeclaration) enumConst.getInternal());

      return enumConst;
   }

   @Override
   public EnumConstantSource getEnumConstant(String name)
   {
      for (EnumConstantSource enumConst : getEnumConstants())
      {
         if (enumConst.getName().equals(name))
         {
            return enumConst;
         }
      }
      return null;
   }

   @Override
   protected JavaEnumSource updateTypeNames(final String newName)
   {
      return this;
   }

}
