/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.ParenExpression;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactoryImpl;

public class ParenImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends ExpressionFactoryImpl<O,P,ParenExpression<O,P>,ParenthesizedExpression>
      implements ParenExpression<O,P>
{

   ParenthesizedExpression paren;

   private ExpressionSource<O,ParenExpression<O,P>,?> inner;

   public ParenImpl(ExpressionSource<O,?,?> child)
   {
      inner = (ExpressionSource<O,ParenExpression<O,P>,?>) child;
   }

   @Override
   public ParenthesizedExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return paren;
      }
      paren = ast.newParenthesizedExpression();

      if (inner != null)
      {
         paren.setExpression(wireAndGetExpression(inner, this, ast));
      }
      return paren;
   }

   @Override
   public ParenthesizedExpression getInternal()
   {
      return paren;
   }

   @Override
   public void setInternal(ParenthesizedExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.paren = jdtNode;
   }

   @Override
   public ExpressionSource<O,ParenExpression<O,P>,?> getExpression()
   {
      return inner;
   }

   public org.eclipse.jdt.core.dom.Expression wireAndGetExpression(ExpressionSource<O,ParenExpression<O,P>,?> expression, ParenExpression<O,P> parent, AST ast)
   {
      JdtExpressionWrapper<O,ParenExpression<O,P>,org.eclipse.jdt.core.dom.Expression> node = (JdtExpressionWrapper<O,ParenExpression<O,P>,org.eclipse.jdt.core.dom.Expression>) expression;
      expression.setOrigin(parent);
      node.setAst(ast);
      return node.materialize(ast);
   }

}
