/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;
import org.jboss.forge.roaster.model.statements.SynchStatement;

public class SynchStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,SynchStatement<O,P>,SynchronizedStatement>
      implements SynchStatement<O,P>
{

   private SynchronizedStatement synch;

   private ExpressionSource<O,SynchStatement<O,P>,?> expr;
   private BlockStatement<O,SynchStatement<O,P>> body;

   public SynchStatementImpl()
   {
   }

   @Override
   public SynchronizedStatement materialize(AST ast)
   {
      if (synch != null)
      {
         return synch;
      }
      synch = ast.newSynchronizedStatement();

      if (expr != null)
      {
         synch.setExpression(wireAndGetExpression(expr, this, ast));
      }
      if (body != null)
      {
         synch.setBody((org.eclipse.jdt.core.dom.Block) wireAndGetStatement(body, this, ast));
      }
      return synch;
   }

   @Override
   public SynchronizedStatement getInternal()
   {
      return synch;
   }

   @Override
   public void setInternal(SynchronizedStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.synch = jdtNode;
   }

   @Override
   public ExpressionSource<O,SynchStatement<O,P>,?> getSynchOn()
   {
      return expr;
   }

   @Override
   public SynchStatement<O,P> setSynchOn(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,SynchStatement<O,P>,?> cast = (ExpressionSource<O,SynchStatement<O,P>,?>) expr;
      this.expr = cast;
      return this;
   }

   @Override
   public BlockStatement<O,SynchStatement<O,P>> getBody()
   {
      return body;
   }

   @Override
   public SynchStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      BlockStatement<O,SynchStatement<O,P>> cast = (BlockStatement<O,SynchStatement<O,P>>) body;
      this.body = cast;
      return this;
   }

   @Override
   public SynchStatement<O,P> setBody(StatementSource<?,?,?> body)
   {
      this.body = wrap(body);
      return this;
   }
}
