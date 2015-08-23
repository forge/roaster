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
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.StatementSource;
import org.jboss.forge.roaster.model.statements.SwitchCaseStatement;
import org.jboss.forge.roaster.model.statements.SwitchStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SwitchStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,SwitchStatement<O,P>,org.eclipse.jdt.core.dom.SwitchStatement>
      implements SwitchStatement<O,P>
{

   private org.eclipse.jdt.core.dom.SwitchStatement opts;

   private ExpressionSource<O,SwitchStatement<O,P>,?> expression;
   private List<StatementSource<O,SwitchStatement<O,P>,?>> statements = new LinkedList<StatementSource<O,SwitchStatement<O,P>,?>>();

   public SwitchStatementImpl()
   {
   }


   @Override
   public org.eclipse.jdt.core.dom.SwitchStatement materialize(AST ast)
   {
      if (opts != null)
      {
         return opts;
      }
      opts = ast.newSwitchStatement();
      opts.setExpression(wireAndGetExpression(expression, this, ast));

      for (Statement<O,SwitchStatement<O,P>,?> stat : statements)
      {
         if (SwitchCaseStatementImpl.class.isInstance(stat))
         {
            SwitchCaseStatementImpl mock = (SwitchCaseStatementImpl) stat;
            mock.materialize(ast);
            opts.statements().add(mock.getInternal());
         } else
         {
            opts.statements().add(wireAndGetStatement(stat, this, ast));
         }
      }
      return opts;
   }

   @Override
   public org.eclipse.jdt.core.dom.SwitchStatement getInternal()
   {
      return opts;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.SwitchStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.opts = jdtNode;
   }

   @Override
   public List<ExpressionSource<O,SwitchStatement<O,P>,?>> getCaseExpressions()
   {
      List<ExpressionSource<O,SwitchStatement<O,P>,?>> cases = new ArrayList<ExpressionSource<O,SwitchStatement<O,P>,?>>();
      for (StatementSource<O,SwitchStatement<O,P>,?> stat : statements)
      {
         if (SwitchCaseStatementImpl.class.isInstance(stat))
         {
            cases.add(((SwitchCaseStatementImpl) stat).getCaseExpression());
         }
      }
      return cases;
   }

   @Override
   public SwitchStatement<O,P> addCase(ExpressionSource<?,?,?> option)
   {
      ExpressionSource<O,SwitchStatement<O,P>,?> cast = (ExpressionSource<O,SwitchStatement<O,P>,?>) option;
      statements.add(new SwitchCaseStatementImpl().setCaseExpression(cast));
      return this;
   }

   @Override
   public SwitchStatement<O,P> addCase(SwitchCaseStatement<?,?> option)
   {
      statements.add((StatementSource<O,SwitchStatement<O,P>,?>) option);
      return this;
   }

   @Override
   public List<StatementSource<O,SwitchStatement<O,P>,?>> getStatements()
   {
      return Collections.unmodifiableList(statements);
   }

   @Override
   public SwitchStatement<O,P> addDefault()
   {
      statements.add(new SwitchCaseStatementImpl());
      return this;
   }

   @Override
   public SwitchStatement<O,P> addStatement(StatementSource<?,?,?> arg)
   {
      StatementSource<O,SwitchStatement<O,P>,?> cast = (StatementSource<O,SwitchStatement<O,P>,?>) arg;
      if (statements.isEmpty())
      {
         statements = new LinkedList<StatementSource<O,SwitchStatement<O,P>,?>>();
      }
      statements.add(cast);
      return this;
   }

   @Override
   public ExpressionSource<O,SwitchStatement<O,P>,?> getSwitch()
   {
      return this.expression;
   }

   @Override
   public org.jboss.forge.roaster.model.statements.SwitchStatement<O,P> setSwitch(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,SwitchStatement<O,P>,?> cast = (ExpressionSource<O,SwitchStatement<O,P>,?>) expr;
      this.expression = cast;
      return this;
   }

   @Override
   public BlockStatement<O,SwitchStatement<O,P>> getBody()
   {
      return null;
   }

   @Override
   public SwitchStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      addDefault();
      return addStatement((BlockStatement<O,P>) body);
   }

   @Override
   public SwitchStatement<O,P> setBody(StatementSource<?,?,?> body)
   {
      addDefault();
      return addStatement(wrap(body));
   }


}


