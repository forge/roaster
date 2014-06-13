/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.NonPrimitiveExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class NonPrimitiveExpressionImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends NonPrimitiveExpression<O,P,E>,
      J extends org.eclipse.jdt.core.dom.Expression>
      extends ExpressionImpl<O,P,E,J>
      implements NonPrimitiveExpression<O,P,E>,
      JdtExpressionWrapper<O,E,J>
{

   private ExpressionHolder<O> parent;

   public NonPrimitiveExpressionImpl()
   {
   }

   public ExpressionHolder<O> getParent()
   {
      return parent;
   }

   public void linkParent(ExpressionHolder<O> parent)
   {
      this.parent = parent;
   }

   public org.eclipse.jdt.core.dom.Expression wireAndGetExpression(ExpressionSource<O,E,?> expression, E parent, AST ast)
   {
      ASTNode<J> node = (ASTNode<J>) expression;
      expression.setOrigin(parent);
      node.setAst(ast);
      return node.materialize(ast);
   }
}
