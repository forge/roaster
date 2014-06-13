/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.Assignment;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.expressions.AssignImpl;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.AssignmentStatement;

public class AssignStatementImpl<O extends JavaSource<O>,
      P extends BlockSource<O,? extends BlockHolder<O>,P>>
      extends StatementImpl<O,P,AssignmentStatement<O,P>,ExpressionStatement>
      implements AssignmentStatement<O,P>
{

   private ExpressionStatement statement;

   private AssignExpression<O,AssignmentStatement<O,P>> inner;

   public AssignStatementImpl()
   {
      this.inner = new AssignImpl<O,AssignmentStatement<O,P>>(Assignment.ASSIGN);
   }

   public AssignStatementImpl(AssignExpression<?,?> axx)
   {
      this.inner = (AssignExpression<O,AssignmentStatement<O,P>>) axx;
   }

   @Override
   public AssignmentStatement<O,P> setRight(ExpressionSource<?,?,?> right)
   {
      inner.setRight(right);
      return this;
   }

   @Override
   public AssignmentStatement<O,P> setLeft(Accessor<?,?,?> left)
   {
      inner.setLeft(left);
      return this;
   }

   @Override
   public Accessor<O,AssignExpression<O,AssignmentStatement<O,P>>,?> getLeft()
   {
      return inner.getLeft();
   }

   @Override
   public ExpressionSource<O,AssignExpression<O,AssignmentStatement<O,P>>,?> getRight()
   {
      return inner.getRight();
   }

   @Override
   public String getAssignmentOperator()
   {
      return inner.getOperator();
   }

   @Override
   public AssignmentStatement<O,P> setAssignmentOperator(Assignment op)
   {
      inner.setOperator(op);
      return this;
   }

   public ExpressionStatement materialize(AST ast)
   {
      if (statement != null)
      {
         return statement;
      }
      statement = ast.newExpressionStatement(wireAndGetExpression(inner, this, ast));
      return statement;
   }

   @Override
   public ExpressionStatement getInternal()
   {
      return statement;
   }

   @Override
   public void setInternal(ExpressionStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.statement = jdtNode;
   }

   @Override
   public ExpressionSource<O,AssignmentStatement<O,P>,?> getExpr()
   {
      return inner;
   }
}
