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
import org.jboss.forge.roaster.model.expressions.CastExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class CastImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,CastExpression<O,P>,org.eclipse.jdt.core.dom.CastExpression>
      implements CastExpression<O,P>
{

   private org.eclipse.jdt.core.dom.CastExpression cast;

   private String type;
   private ExpressionSource<O,CastExpression<O,P>,?> expression;

   public CastImpl(String klass, ExpressionSource<O,?,?> expression)
   {
      this.type = klass;
      this.expression = (ExpressionSource<O,CastExpression<O,P>,?>) expression;
   }

   @Override
   public org.eclipse.jdt.core.dom.CastExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return cast;
      }
      cast = ast.newCastExpression();
      cast.setType(JDTHelper.getType(type, ast));
      if (expression != null)
      {
         org.eclipse.jdt.core.dom.Expression expr = wireAndGetExpression(expression, this, ast);
         ParenthesizedExpression paren = ast.newParenthesizedExpression();
         paren.setExpression(expr);
         cast.setExpression(paren);
      }
      return cast;
   }

   @Override
   public org.eclipse.jdt.core.dom.CastExpression getInternal()
   {
      return cast;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.CastExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.cast = jdtNode;
   }

   @Override
   public String getType()
   {
      return type;
   }

   @Override
   public ExpressionSource<O,CastExpression<O,P>,?> getExpression()
   {
      return expression;
   }

   @Override
   public CastExpression<O,P> setExpression(ExpressionSource<?,?,?> expr)
   {
      this.expression = (ExpressionSource<O,CastExpression<O,P>,?>) expr;
      return this;
   }
}
