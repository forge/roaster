/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.NodeImpl;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;

public abstract class StatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>,
      S extends Statement<O,P,S>,
      J extends org.eclipse.jdt.core.dom.Statement>
      extends NodeImpl<O,P,J>
      implements Statement<O,P,S>
{

   protected StatementImpl()
   {
   }

   @Override
   public S setLabel(String label)
   {
      this.label = label;
      return (S) this;
   }

   protected <X extends ExpressionHolder<O>> org.eclipse.jdt.core.dom.Expression wireAndGetExpression(ExpressionSource<O,X,?> expression, X parent, AST ast)
   {
      ASTNode<? extends org.eclipse.jdt.core.dom.Expression> node = (ASTNode<? extends org.eclipse.jdt.core.dom.Expression>) expression;
      expression.setOrigin(parent);
      node.setAst(ast);
      return node.materialize(ast);
   }


   @Override
   public boolean isMaterialized()
   {
      return getInternal() != null;
   }

}
