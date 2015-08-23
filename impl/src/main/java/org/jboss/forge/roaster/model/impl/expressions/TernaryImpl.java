/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.TernaryExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class TernaryImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,TernaryExpression<O,P>,ConditionalExpression>
      implements TernaryExpression<O,P>
{

   private ConditionalExpression ternary;

   private ExpressionSource<O,TernaryExpression<O,P>,?> condition;
   private ExpressionSource<O,TernaryExpression<O,P>,?> onTrue;
   private ExpressionSource<O,TernaryExpression<O,P>,?> onFalse;

   public TernaryImpl()
   {
   }

   @Override
   public TernaryExpression<O,P> setCondition(ExpressionSource<?,?,?> expression)
   {
      this.condition = (ExpressionSource<O,TernaryExpression<O,P>,?>) expression;
      return this;
   }

   @Override
   public TernaryExpression<O,P> setThenExpression(ExpressionSource<?,?,?> onTrue)
   {
      this.onTrue = (ExpressionSource<O,TernaryExpression<O,P>,?>) onTrue;
      return this;
   }

   @Override
   public TernaryExpression<O,P> setElseExpression(ExpressionSource<?,?,?> onFalse)
   {
      this.onFalse = (ExpressionSource<O,TernaryExpression<O,P>,?>) onFalse;
      return this;
   }

   @Override
   public ExpressionSource<O,TernaryExpression<O,P>,?> getCondition()
   {
      return condition;
   }

   @Override
   public ExpressionSource<O,TernaryExpression<O,P>,?> getThenExpression()
   {
      return onTrue;
   }

   @Override
   public ExpressionSource<O,TernaryExpression<O,P>,?> getElseExpression()
   {
      return onFalse;
   }

   @Override
   public ConditionalExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return ternary;
      }
      ternary = ast.newConditionalExpression();

      if (condition != null)
      {
         ternary.setExpression(wireAndGetExpression(condition, this, ast));
      }
      if (onTrue != null)
      {
         ternary.setThenExpression(wireAndGetExpression(onTrue, this, ast));
      }
      if (onFalse != null)
      {
         ternary.setElseExpression(wireAndGetExpression(onFalse, this, ast));
      }
      return ternary;
   }

   @Override
   public ConditionalExpression getInternal()
   {
      return ternary;
   }

   @Override
   public void setInternal(ConditionalExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.ternary = jdtNode;
   }
}
