/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ThrowStatement;

public class ThrowStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,ThrowStatement<O,P>,org.eclipse.jdt.core.dom.ThrowStatement>
      implements ThrowStatement<O,P>
{

   private ExpressionSource<O,ThrowStatement<O,P>,?> expression;

   private org.eclipse.jdt.core.dom.ThrowStatement throwStatement;

   public ThrowStatementImpl()
   {
   }

   @Override
   public org.eclipse.jdt.core.dom.ThrowStatement materialize(AST ast)
   {
      if (throwStatement != null)
      {
         return throwStatement;
      }
      throwStatement = ast.newThrowStatement();
      if (expression != null)
      {
         throwStatement.setExpression(wireAndGetExpression(expression, this, getAst()));
      }
      return throwStatement;
   }

   @Override
   public ExpressionSource<O,ThrowStatement<O,P>,?> getThrowable()
   {
      return expression;
   }

   @Override
   public ThrowStatement<O,P> setThrowable(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,ThrowStatement<O,P>,?> cast = (ExpressionSource<O,ThrowStatement<O,P>,?>) expr;
      this.expression = cast;
      return this;
   }

   @Override
   public org.eclipse.jdt.core.dom.ThrowStatement getInternal()
   {
      return throwStatement;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.ThrowStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.throwStatement = jdtNode;
   }
}
