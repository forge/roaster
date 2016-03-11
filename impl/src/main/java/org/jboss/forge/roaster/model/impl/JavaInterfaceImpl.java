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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.ast.MethodFinderVisitor;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JavaInterfaceImpl extends AbstractGenericCapableJavaSource<JavaInterfaceSource> implements
         JavaInterfaceSource
{

   public JavaInterfaceImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   protected JavaInterfaceSource updateTypeNames(final String name)
   {
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<JavaInterfaceSource> addMethod()
   {
      MethodSource<JavaInterfaceSource> m = new JavaInterfaceMethodImpl(this);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<JavaInterfaceSource> addMethod(final String method)
   {
      MethodSource<JavaInterfaceSource> m = new JavaInterfaceMethodImpl(this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<JavaInterfaceSource> addMethod(java.lang.reflect.Method method)
   {
      MethodSource<JavaInterfaceSource> m = new JavaInterfaceMethodImpl(this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<JavaInterfaceSource> addMethod(Method<?, ?> method)
   {
      MethodSource<JavaInterfaceSource> m = new JavaInterfaceMethodImpl(this, method.toString());
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }
   
   @Override
   public List<MethodSource<JavaInterfaceSource>> getMethods()
   {
      List<MethodSource<JavaInterfaceSource>> result = new ArrayList<MethodSource<JavaInterfaceSource>>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      body.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new JavaInterfaceMethodImpl(this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }
   

}
