/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.OrdinalArgument;
import org.jboss.forge.roaster.model.expressions.PostFixExpression;
import org.jboss.forge.roaster.model.expressions.PostfixOp;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class PostFixImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,UnaryExpression<O,P>,PostfixExpression>
      implements PostFixExpression<O,P>
{

   private PostfixExpression post;

   private PostfixOp op;
   private OrdinalArgument<O,UnaryExpression<O,P>,?> arg;

   public PostFixImpl(PostfixOp op)
   {
      this.op = op;
   }

   @Override
   public PostfixExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return post;
      }
      post = ast.newPostfixExpression();
      post.setOperator(PostfixExpression.Operator.toOperator(op.getOp()));

      if (arg != null)
      {
         post.setOperand(wireAndGetExpression(arg, this, ast));
      }
      return post;
   }

   @Override
   public PostfixExpression getInternal()
   {
      return post;
   }

   @Override
   public void setInternal(PostfixExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.post = jdtNode;
   }

   @Override
   public Argument<O,UnaryExpression<O,P>,?> getExpression()
   {
      return arg;
   }

   @Override
   public PostFixExpression<O,P> setExpression(ExpressionSource<?,?,?> arg)
   {
      this.arg = (OrdinalArgument<O,UnaryExpression<O,P>,?>) arg;
      return this;
   }

   @Override
   public String getOperator()
   {
      return op.getOp();
   }
}
