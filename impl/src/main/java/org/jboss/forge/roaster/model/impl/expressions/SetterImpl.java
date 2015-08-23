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
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SetterImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AbstractMethodInvokeImpl<O,P,Setter<O,P>,MethodInvocation>
      implements Setter<O,P>
{

   protected MethodInvocation invoke;

   public SetterImpl(String fieldName, String type, ExpressionSource<?,?,?> value)
   {
      super(JDTHelper.setter(fieldName, type));
      addArgument((Argument<O,Setter<O,P>,?>) value);
   }

   @Override
   public Setter<O,P> setInvocationTarget(ExpressionSource<?,?,?> target)
   {
      this.target = chainExpression((ExpressionSource<O,?,?>) target);
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

      for (Argument<O,Setter<O,P>,?> arg : arguments)
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
   public ExpressionSource<O,Setter<O,P>,?> getValue()
   {
      if (getArguments().isEmpty())
      {
         return null;
      }
      return getArguments().get(0);
   }
}
