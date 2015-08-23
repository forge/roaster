/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OperatorImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,OperatorExpression<O,P>,InfixExpression>
      implements OperatorExpression<O,P>
{

   private InfixExpression expr;

   private Op operator;
   private List<Argument<O,OperatorExpression<O,P>,?>> argumentList = Collections.EMPTY_LIST;

   public OperatorImpl(Op op)
   {
      this.operator = op;
   }

   @Override
   public OperatorExpression<O,P> addArgument(Argument<?,?,?> arg)
   {
      if (argumentList.isEmpty())
      {
         argumentList = new LinkedList<Argument<O,OperatorExpression<O,P>,?>>();
      }
      argumentList.add((Argument<O,OperatorExpression<O,P>,?>) arg);
      return this;
   }

   public List<Argument<O,OperatorExpression<O,P>,?>> getArguments()
   {
      return Collections.unmodifiableList(argumentList);
   }

   public String getOperator()
   {
      return operator.getOp();
   }

   @Override
   public InfixExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return expr;
      }
      expr = ast.newInfixExpression();

      expr.setOperator(InfixExpression.Operator.toOperator(this.operator.getOp()));

      for (Argument<O,OperatorExpression<O,P>,?> arg : argumentList)
      {
         wireArg(arg, ast);
      }
      return expr;
   }

   @Override
   public InfixExpression getInternal()
   {
      return expr;
   }

   @Override
   public void setInternal(InfixExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.expr = jdtNode;
   }

   protected void wireArg(Argument<O,OperatorExpression<O,P>,?> arg, AST ast)
   {
      Expression child = wireAndGetExpression(arg, this, ast);

      if (child.getNodeType() == ASTNode.INFIX_EXPRESSION)
      {
         if (!expr.getOperator().equals(((InfixExpression) child).getOperator()))
         {
            ParenthesizedExpression par = ast.newParenthesizedExpression();
            par.setExpression(child);
            child = par;
         }
      }

      InfixExpression infix = expr;
      if ("MISSING".equals(infix.getLeftOperand().toString()))
      {
         infix.setLeftOperand(child);
      } else if ("MISSING".equals(infix.getRightOperand().toString()))
      {
         infix.setRightOperand(child);
      } else
      {
         org.eclipse.jdt.core.dom.Expression prev = infix.getRightOperand();
         InfixExpression more = ast.newInfixExpression();
         more.setOperator(infix.getOperator());
         infix.setRightOperand(more);
         prev.delete();
         more.setLeftOperand(prev);
         more.setRightOperand(child);
      }
   }
}
