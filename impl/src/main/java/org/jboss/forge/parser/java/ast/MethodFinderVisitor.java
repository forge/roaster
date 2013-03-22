/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodFinderVisitor extends ASTVisitor
{
   private final List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
   private ASTNode parent;

   @Override
   public boolean visit(final TypeDeclaration node)
   {
      parent = node;
      addMethods(node);
      return super.visit(node);
   }
   
   @Override
   public boolean visit(EnumDeclaration node)
   {
      parent = node;
      addMethods(node);
      return super.visit(node);
   }
   
   @Override
   public boolean visit(AnnotationTypeDeclaration node)
   {
      parent = node;
      addMethods(node);
      return super.visit(node);
   }

   public List<MethodDeclaration> getMethods()
   {
      return Collections.unmodifiableList(methods);
   }

   public TypeDeclaration getParent()
   {
      return (TypeDeclaration) parent;
   }

   private void addMethods(AbstractTypeDeclaration node)
   {
      @SuppressWarnings("unchecked")
      final List<BodyDeclaration> bodyDeclarations = node.bodyDeclarations();
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof MethodDeclaration) {
            methods.add((MethodDeclaration) bodyDeclaration);
         }
      }
   }
}