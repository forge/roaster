/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class UnaryImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,UnaryExpression<O,P>,PrefixExpression>
      implements UnaryExpression<O,P>
{

   PrefixExpression expr;

   private Argument<O,UnaryExpression<O,P>,?> arg;
   private PrefixOp op;

   public UnaryImpl(PrefixOp op)
   {
      this.op = op;
   }

   @Override
   public PrefixExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return expr;
      }
      expr = ast.newPrefixExpression();
      expr.setOperator(PrefixExpression.Operator.toOperator(op.getOp()));

      if (arg != null)
      {
         expr.setOperand(wireAndGetExpression(arg, this, ast));
      }
      return expr;
   }

   @Override
   public PrefixExpression getInternal()
   {
      return expr;
   }

   @Override
   public void setInternal(PrefixExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.expr = jdtNode;
   }

   @Override
   public Argument<O,UnaryExpression<O,P>,?> getExpression()
   {
      return arg;
   }

   @Override
   public UnaryExpression<O,P> setExpression(ExpressionSource<?,?,?> arg)
   {
      this.arg = (Argument<O,UnaryExpression<O,P>,?>) arg;
      return this;
   }

   @Override
   public String getOperator()
   {
      return op.getOp();
   }
}
