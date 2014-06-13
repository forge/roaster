/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.DoStatement;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.DoWhileStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;

public class DoWhileStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,DoWhileStatement<O,P>,DoStatement>
      implements DoWhileStatement<O,P>
{

   private DoStatement rep;

   private BlockStatement<O,DoWhileStatement<O,P>> body;
   private ExpressionSource<O,DoWhileStatement<O,P>,?> condition;

   public DoWhileStatementImpl()
   {
   }

   @Override
   public BlockStatement<O,DoWhileStatement<O,P>> getBody()
   {
      return body;
   }

   @Override
   public DoWhileStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      BlockStatement<O,DoWhileStatement<O,P>> cast = (BlockStatement<O,DoWhileStatement<O,P>>) body;
      this.body = cast;
      return this;
   }

   @Override
   public DoWhileStatement<O,P> setBody(StatementSource<?,?,?> statement)
   {
      this.body = wrap(statement);
      return this;
   }

   @Override
   public ExpressionSource<O,DoWhileStatement<O,P>,?> getCondition()
   {
      return condition;
   }

   @Override
   public DoWhileStatement<O,P> setCondition(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,DoWhileStatement<O,P>,?> cast = (ExpressionSource<O,DoWhileStatement<O,P>,?>) expr;
      this.condition = cast;
      return this;
   }

   @Override
   public DoStatement materialize(AST ast)
   {
      if (rep != null)
      {
         return rep;
      }
      rep = ast.newDoStatement();

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
   public DoStatement getInternal()
   {
      return rep;
   }

   @Override
   public void setInternal(DoStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.rep = jdtNode;
   }
}
