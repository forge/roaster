/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class AssignImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,AssignExpression<O,P>,Assignment>
      implements AssignExpression<O,P>
{

   private Assignment axx;

   private Accessor<O,AssignExpression<O,P>,?> left;
   private ExpressionSource<O,AssignExpression<O,P>,?> right;
   private org.jboss.forge.roaster.model.expressions.Assignment op;

   public AssignImpl(org.jboss.forge.roaster.model.expressions.Assignment op)
   {
      this.op = op;
   }

   @Override
   public AssignExpression<O,P> setLeft(Accessor<?,?,?> left)
   {
      this.left = (Accessor<O,AssignExpression<O,P>,?>) left;
      return this;
   }

   @Override
   public AssignExpression<O,P> setRight(ExpressionSource<?,?,?> right)
   {
      this.right = (ExpressionSource<O,AssignExpression<O,P>,?>) right;
      return this;
   }

   @Override
   public Accessor<O,AssignExpression<O,P>,?> getLeft()
   {
      return left;
   }

   @Override
   public String getOperator()
   {
      return op.toString();
   }

   public AssignExpression<O,P> setOperator(org.jboss.forge.roaster.model.expressions.Assignment op)
   {
      this.op = op;
      return this;
   }

   @Override
   public ExpressionSource<O,AssignExpression<O,P>,?> getRight()
   {
      return right;
   }

   @Override
   public Assignment materialize(AST ast)
   {
      if (isMaterialized())
      {
         return axx;
      }
      axx = ast.newAssignment();
      axx.setOperator(Assignment.Operator.toOperator(op.getOp()));

      if (left != null)
      {
         axx.setLeftHandSide(wireAndGetExpression(left, this, ast));
      }
      if (right != null)
      {
         axx.setRightHandSide(wireAndGetExpression(right, this, ast));
      }
      return axx;
   }

   @Override
   public Assignment getInternal()
   {
      return axx;
   }

   @Override
   public void setInternal(Assignment jdtNode)
   {
      super.setInternal(jdtNode);
      this.axx = jdtNode;
   }
}
