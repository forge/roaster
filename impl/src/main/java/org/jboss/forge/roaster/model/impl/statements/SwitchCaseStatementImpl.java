/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.SwitchCaseStatement;
import org.jboss.forge.roaster.model.statements.SwitchStatement;

public class SwitchCaseStatementImpl<O extends JavaSource<O>, P extends BlockHolder<O>>
      implements SwitchCaseStatement<O,SwitchStatement<O,P>>,
      ASTNode<org.eclipse.jdt.core.dom.SwitchCase>
{

   private ExpressionSource<O,SwitchCaseStatement<O,SwitchStatement<O,P>>,?> option;

   private SwitchCase inner;
   private AST ast;

   public SwitchCaseStatementImpl()
   {
   }

   public SwitchCaseStatementImpl setLabel(String label)
   {
      return null;
   }

   public String getLabel()
   {
      return null;
   }

   public boolean hasLabel()
   {
      return false;
   }

   public void setOrigin(SwitchStatement<O,P> origin)
   {
   }

   public SwitchStatement<O,P> getOrigin()
   {
      return null;
   }

   @Override
   public ExpressionSource<O,SwitchCaseStatement<O,SwitchStatement<O,P>>,?> getCaseExpression()
   {
      return option;
   }

   @Override
   public SwitchCaseStatement<O,SwitchStatement<O,P>> setCaseExpression(ExpressionSource<?,?,?> src)
   {
      this.option = (ExpressionSource<O,SwitchCaseStatement<O,SwitchStatement<O,P>>,?>) src;
      return this;
   }

   @Override
   public AST getAst()
   {
      return null;
   }

   @Override
   public org.eclipse.jdt.core.dom.SwitchCase materialize(AST ast)
   {
      if (inner != null)
      {
         return inner;
      }
      inner = ast.newSwitchCase();
      if (option != null)
      {
         // case ... :
         inner.setExpression(wireAndGetExpression(option, this, ast));
      } else
      {
         // default:
         inner.setExpression(null);
      }
      return inner;
   }

   @Override
   public org.eclipse.jdt.core.dom.SwitchCase getInternal()
   {
      return inner;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.SwitchCase jdtNode)
   {
      this.inner = jdtNode;
   }

   @Override
   public boolean isMaterialized()
   {
      return inner != null;
   }

   @Override
   public void setAst(AST ast)
   {
      this.ast = ast;
   }

   protected <X extends ExpressionHolder<O>> org.eclipse.jdt.core.dom.Expression wireAndGetExpression(ExpressionSource<O,X,?> expression, X parent, AST ast)
   {
      ASTNode<? extends org.eclipse.jdt.core.dom.Expression> node = (ASTNode<? extends org.eclipse.jdt.core.dom.Expression>) expression;
      expression.setOrigin(parent);
      node.setAst(ast);
      return node.materialize(ast);
   }

}