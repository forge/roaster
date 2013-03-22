/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.EnumConstant;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.JavaSource;

public class EnumConstantImpl<O extends JavaSource<O>> implements EnumConstant<O>
{
   private O parent;
   private AST ast;
   private EnumConstantDeclaration enumConstant;

   private void init(final O parent)
   {
      this.parent = parent;
      this.ast = ((ASTNode)parent.getInternal()).getAST();
   }
   
   public EnumConstantImpl(final O parent) {
      init(parent);
      this.enumConstant = ast.newEnumConstantDeclaration();
   }
   
   public EnumConstantImpl(final O parent, final String declaration)
   {
      init(parent);

      String stub = "public enum Stub { " + declaration + " }";
      JavaEnum temp = (JavaEnum) JavaParser.parse(stub);
      List<EnumConstant<JavaEnum>> constants = temp.getEnumConstants();
      EnumConstantDeclaration newField = (EnumConstantDeclaration) constants.get(0).getInternal();
      EnumConstantDeclaration subtree = (EnumConstantDeclaration) ASTNode.copySubtree(ast, newField);
      this.enumConstant = subtree;
   }
   
   public EnumConstantImpl(final O parent, final Object internal)
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
   public EnumConstant<O> setName(String name)
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
   public O getOrigin()
   {
      return parent;
   }
}
