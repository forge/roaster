/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InstanceofExpression;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class InstanceofImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,InstanceofExpression<O,P>,org.eclipse.jdt.core.dom.InstanceofExpression>
      implements InstanceofExpression<O,P>
{

   private org.eclipse.jdt.core.dom.InstanceofExpression isA;

   private String typeName;
   private ExpressionSource<O,InstanceofExpression<O,P>,?> expression;

   public InstanceofImpl(String klass, ExpressionSource<O,?,?> expression)
   {
      this.typeName = klass;
      this.expression = (ExpressionSource<O,InstanceofExpression<O,P>,?>) expression;
   }

   @Override
   public org.eclipse.jdt.core.dom.InstanceofExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return isA;
      }
      isA = ast.newInstanceofExpression();
      isA.setRightOperand(JDTHelper.getType(typeName, ast));
      if (expression != null)
      {
         org.eclipse.jdt.core.dom.Expression expr = wireAndGetExpression(expression, this, ast);
         isA.setLeftOperand(expr);
      }
      return isA;
   }

   @Override
   public org.eclipse.jdt.core.dom.InstanceofExpression getInternal()
   {
      return isA;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.InstanceofExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.isA = jdtNode;
   }

   public String getTypeName()
   {
      return typeName;
   }

   @Override
   public ExpressionSource<O,InstanceofExpression<O,P>,?> getExpression()
   {
      return expression;
   }
}
