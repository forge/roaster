/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.ForStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ForStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,ForStatement<O,P>,org.eclipse.jdt.core.dom.ForStatement>
      implements ForStatement<O,P>
{

   private org.eclipse.jdt.core.dom.ForStatement iter;

   private DeclareExpression<O,ForStatement<O,P>> declaration;
   private List<ExpressionSource<O,ForStatement<O,P>,?>> updaters = Collections.EMPTY_LIST;
   private ExpressionSource<O,ForStatement<O,P>,?> condition;
   private BlockStatement<O,ForStatement<O,P>> body;

   public ForStatementImpl()
   {
   }

   @Override
   public DeclareExpression<O,ForStatement<O,P>> getDeclaration()
   {
      return declaration;
   }

   @Override
   public ForStatement<O,P> setDeclaration(DeclareExpression<?,?> declaration)
   {
      DeclareExpression<O,ForStatement<O,P>> cast = (DeclareExpression<O,ForStatement<O,P>>) declaration;
      this.declaration = cast;
      return this;
   }

   @Override
   public ExpressionSource<O,ForStatement<O,P>,?> getCondition()
   {
      return condition;
   }

   @Override
   public ForStatement<O,P> addUpdate(ExpressionSource<?,?,?> expression)
   {
      ExpressionSource<O,ForStatement<O,P>,?> cast = (ExpressionSource<O,ForStatement<O,P>,?>) expression;
      if (updaters.isEmpty())
      {
         updaters = new LinkedList<ExpressionSource<O,ForStatement<O,P>,?>>();
      }
      updaters.add(cast);
      return this;
   }

   @Override
   public ForStatement<O,P> setCondition(ExpressionSource<?,?,?> booleanExpression)
   {
      ExpressionSource<O,ForStatement<O,P>,?> cast = (ExpressionSource<O,ForStatement<O,P>,?>) booleanExpression;
      this.condition = cast;
      return this;
   }

   @Override
   public List<ExpressionSource<O,ForStatement<O,P>,?>> getUpdates()
   {
      return Collections.unmodifiableList(updaters);
   }

   @Override
   public BlockStatement<O,ForStatement<O,P>> getBody()
   {
      return body;
   }

   @Override
   public ForStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      BlockStatement<O,ForStatement<O,P>> cast = (BlockStatement<O,ForStatement<O,P>>) body;
      this.body = cast;
      return this;
   }

   @Override
   public ForStatement<O,P> setBody(StatementSource<?,?,?> statement)
   {
      this.body = wrap(statement);
      return this;
   }

   @Override
   public org.eclipse.jdt.core.dom.ForStatement materialize(AST ast)
   {
      if (iter != null)
      {
         return iter;
      }

      iter = ast.newForStatement();

      if (declaration != null)
      {
         iter.initializers().add(wireAndGetExpression(declaration, this, ast));
      }

      if (condition != null)
      {
         iter.setExpression(wireAndGetExpression(condition, this, ast));
      }

      for (ExpressionSource<O,ForStatement<O,P>,?> updater : updaters)
      {
         iter.updaters().add(wireAndGetExpression(updater, this, ast));
      }

      if (body != null)
      {
         iter.setBody(wireAndGetStatement(body, this, ast));
      }

      return iter;
   }

   @Override
   public org.eclipse.jdt.core.dom.ForStatement getInternal()
   {
      return iter;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.ForStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.iter = jdtNode;
   }
}
