/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperMethodInvokeImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AbstractMethodInvokeImpl<O,P,MethodCallExpression<O,P>,SuperMethodInvocation>
      implements MethodCallExpression<O,P>
{

   protected SuperMethodInvocation invoke;

   public SuperMethodInvokeImpl(String method)
   {
      super(method);
      target = new SuperImpl<O,MethodCallExpression<O,P>>();
   }

   @Override
   public SuperMethodInvocation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return invoke;
      }
      invoke = ast.newSuperMethodInvocation();

      invoke.setName(ast.newSimpleName(method));
      for (Argument<O,MethodCallExpression<O,P>,?> arg : arguments)
      {
         invoke.arguments().add(wireAndGetExpression(arg, this, ast));
      }
      return invoke;
   }

   @Override
   public SuperMethodInvocation getInternal()
   {
      return invoke;
   }

   @Override
   public MethodCallExpression<O,P> setMethodName(String name)
   {
      this.method = name;
      return this;
   }

   @Override
   public void setInternal(SuperMethodInvocation jdtNode)
   {
      super.setInternal(jdtNode);
      this.invoke = jdtNode;
   }
}
