/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.source.JavaSource;

public class FieldImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends SimpleAccessorImpl<O,P,Field<O,P>,FieldAccess>
      implements Field<O,P>,
      ExpressionChainLink<O,Field<O,P>>
{

   private FieldAccess fld;

   private String fieldName;
   private ExpressionSource<O,Field<O,P>,?> expression;

   public FieldImpl(String fieldName)
   {
      this.fieldName = fieldName;
   }

   @Override
   public FieldAccess materialize(AST ast)
   {
      if (isMaterialized())
      {
         return fld;
      }
      fld = ast.newFieldAccess();
      fld.setName(ast.newSimpleName(fieldName));
      if (expression == null)
      {
         fld.setExpression(ast.newThisExpression());
      } else
      {
         fld.setExpression(wireAndGetExpression(expression, this, ast));
      }
      return fld;
   }

   @Override
   public FieldAccess getInternal()
   {
      return fld;
   }

   @Override
   public void setInternal(FieldAccess jdtNode)
   {
      super.setInternal(jdtNode);
      this.fld = jdtNode;
   }

   @Override
   public ExpressionSource<O,Field<O,P>,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      ExpressionSource<O,Field<O,P>,?> cast = (ExpressionSource<O,Field<O,P>,?>) child;
      this.expression = cast;
      return cast;
   }

   @Override
   public String getFieldName()
   {
      return fieldName;
   }

   @Override
   public ExpressionSource<O,Field<O,P>,?> getInvocationTarget()
   {
      return expression;
   }

   @Override
   public Field<O,P> setInvocationTarget(ExpressionSource<?,?,?> target)
   {
      this.expression = (ExpressionSource<O,Field<O,P>,?>) target;
      return this;
   }
}
