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
import org.jboss.forge.roaster.model.statements.EvalExpressionStatement;

public class ExpressionStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,EvalExpressionStatement<O,P>,org.eclipse.jdt.core.dom.ExpressionStatement>
      implements EvalExpressionStatement<O,P>
{

   private org.eclipse.jdt.core.dom.ExpressionStatement exprStatement;

   private ExpressionSource<O,EvalExpressionStatement<O,P>,?> expression;

   public ExpressionStatementImpl(ExpressionSource<O,?,?> expr)
   {
      setExpr(expr);
   }


   public ExpressionStatementImpl()
   {
   }

   @Override
   public org.eclipse.jdt.core.dom.ExpressionStatement materialize(AST ast)
   {
      if (isMaterialized())
      {
         return exprStatement;
      }
      exprStatement = ast.newExpressionStatement(wireAndGetExpression(expression, this, getAst()));
      return exprStatement;
   }

   @Override
   public org.eclipse.jdt.core.dom.ExpressionStatement getInternal()
   {
      return exprStatement;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.ExpressionStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.exprStatement = jdtNode;
   }

   @Override
   public ExpressionSource<O,EvalExpressionStatement<O,P>,?> getExpr()
   {
      return expression;
   }

   @Override
   public EvalExpressionStatement<O,P> setExpr(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,EvalExpressionStatement<O,P>,?> cast = (ExpressionSource<O,EvalExpressionStatement<O,P>,?>) expr;
      this.expression = cast;
      cast.setOrigin(this);
      return this;
   }

}
