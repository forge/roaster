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
import org.jboss.forge.roaster.model.statements.IfStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;

public class IfStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,IfStatement<O,P>,org.eclipse.jdt.core.dom.IfStatement>
      implements IfStatement<O,P>
{

   private org.eclipse.jdt.core.dom.IfStatement iff;

   private ExpressionSource<O,IfStatement<O,P>,?> condition;
   private BlockStatement<O,IfStatement<O,P>> thenBody;
   private BlockStatement<O,IfStatement<O,P>> elseBody;

   public IfStatementImpl()
   {
   }

   @Override
   public ExpressionSource<O,IfStatement<O,P>,?> getCondition()
   {
      return condition;
   }

   @Override
   public IfStatement<O,P> setCondition(ExpressionSource<?,?,?> condition)
   {
      ExpressionSource<O,IfStatement<O,P>,?> cast = (ExpressionSource<O,IfStatement<O,P>,?>) condition;
      this.condition = cast;
      return this;
   }

   @Override
   public BlockStatement<O,IfStatement<O,P>> getThen()
   {
      return thenBody;
   }

   @Override
   public IfStatement<O,P> setThen(BlockSource<?,?,?> body)
   {
      BlockStatement<O,IfStatement<O,P>> cast = (BlockStatement<O,IfStatement<O,P>>) body;
      this.thenBody = cast;
      return this;
   }

   @Override
   public BlockStatement<O,IfStatement<O,P>> getElse()
   {
      return elseBody;
   }

   @Override
   public IfStatement<O,P> setThen(StatementSource<?,?,?> statement)
   {
      this.thenBody = wrap(statement);
      return this;
   }

   @Override
   public BlockStatement<O,IfStatement<O,P>> getBody()
   {
      return thenBody;
   }

   @Override
   public IfStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      return setThen(body);
   }

   @Override
   public IfStatement<O,P> setBody(StatementSource<?,?,?> statement)
   {
      return setThen(statement);
   }

   @Override
   public IfStatement<O,P> setElse(BlockSource<?,?,?> body)
   {
      BlockStatement<O,IfStatement<O,P>> cast = (BlockStatement<O,IfStatement<O,P>>) body;
      this.elseBody = cast;
      return this;
   }

   @Override
   public IfStatement<O,P> setElse(StatementSource<?,?,?> statement)
   {
      this.elseBody = wrap(statement);
      return this;
   }


   @Override
   public org.eclipse.jdt.core.dom.IfStatement materialize(AST ast)
   {
      if (iff != null)
      {
         return iff;
      }
      iff = ast.newIfStatement();

      if (condition != null)
      {
         iff.setExpression(wireAndGetExpression(condition, this, ast));
      }
      if (thenBody != null)
      {
         iff.setThenStatement(wireAndGetStatement(thenBody, this, ast));
      }
      if (elseBody != null)
      {
         iff.setElseStatement(wireAndGetStatement(elseBody, this, ast));
      }
      return iff;
   }

   @Override
   public org.eclipse.jdt.core.dom.IfStatement getInternal()
   {
      return iff;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.IfStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.iff = jdtNode;
   }
}
