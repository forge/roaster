/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class MethodInvokeImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AbstractMethodInvokeImpl<O,P,MethodCallExpression<O,P>,MethodInvocation>
      implements MethodCallExpression<O,P>,
      ExpressionChainLink<O,MethodCallExpression<O,P>>
{

   protected MethodInvocation invoke;

   public MethodInvokeImpl(String method)
   {
      super(method);
   }

   @Override
   public MethodCallExpression<O,P> setMethodName(String name)
   {
      this.method = name;
      return this;
   }

   @Override
   public MethodInvocation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return invoke;
      }
      invoke = ast.newMethodInvocation();

      invoke.setName(ast.newSimpleName(method));
      if (target != null)
      {
         invoke.setExpression(wireAndGetExpression(target, this, ast));
      }
      for (Argument<O,MethodCallExpression<O,P>,?> arg : arguments)
      {
         invoke.arguments().add(wireAndGetExpression(arg, this, ast));
      }
      return invoke;
   }

   @Override
   public MethodInvocation getInternal()
   {
      return invoke;
   }

   @Override
   public void setInternal(MethodInvocation jdtNode)
   {
      super.setInternal(jdtNode);
      this.invoke = jdtNode;
   }

}
