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
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;
import org.jboss.forge.roaster.model.statements.WhileStatement;

public class WhileStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,WhileStatement<O,P>,org.eclipse.jdt.core.dom.WhileStatement>
      implements WhileStatement<O,P>
{

   private org.eclipse.jdt.core.dom.WhileStatement rep;

   private BlockStatement<O,WhileStatement<O,P>> body;
   private ExpressionSource<O,WhileStatement<O,P>,?> condition;

   public WhileStatementImpl()
   {
   }

   @Override
   public ExpressionSource<O,WhileStatement<O,P>,?> getCondition()
   {
      return condition;
   }

   @Override
   public WhileStatement<O,P> setCondition(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,WhileStatement<O,P>,?> cast = (ExpressionSource<O,WhileStatement<O,P>,?>) expr;
      this.condition = cast;
      return this;
   }

   @Override
   public org.eclipse.jdt.core.dom.WhileStatement materialize(AST ast)
   {
      if (rep != null)
      {
         return rep;
      }
      rep = ast.newWhileStatement();

      if (body != null)
      {
         rep.setBody(wireAndGetStatement(body, this, ast));
      }
      if (condition != null)
      {
         rep.setExpression(wireAndGetExpression(condition, this, ast));
      }
      return rep;
   }

   @Override
   public org.eclipse.jdt.core.dom.WhileStatement getInternal()
   {
      return rep;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.WhileStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.rep = jdtNode;
   }

   @Override
   public BlockStatement<O,WhileStatement<O,P>> getBody()
   {
      return body;
   }

   @Override
   public WhileStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      BlockStatement<O,WhileStatement<O,P>> cast = (BlockStatement<O,WhileStatement<O,P>>) body;
      this.body = cast;
      return this;
   }

   @Override
   public WhileStatement<O,P> setBody(StatementSource<?,?,?> body)
   {
      this.body = wrap(body);
      return this;
   }
}
