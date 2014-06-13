/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperFieldImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends SimpleAccessorImpl<O,P,Field<O,P>,SuperFieldAccess>
      implements Field<O,P>,
      ExpressionChainLink<O,Field<O,P>>
{

   private SuperFieldAccess fld;

   private String fieldName;
   private ExpressionSource<O,Field<O,P>,?> expression = new SuperImpl<O,Field<O,P>>();

   public SuperFieldImpl(String fieldName)
   {
      this.fieldName = fieldName;
   }

   @Override
   public SuperFieldAccess materialize(AST ast)
   {
      if (isMaterialized())
      {
         return fld;
      }
      fld = ast.newSuperFieldAccess();
      fld.setName(ast.newSimpleName(fieldName));
      return fld;
   }

   @Override
   public SuperFieldAccess getInternal()
   {
      return fld;
   }

   @Override
   public void setInternal(SuperFieldAccess jdtNode)
   {
      super.setInternal(jdtNode);
      this.fld = jdtNode;
   }

   @Override
   public ExpressionSource<O,Field<O,P>,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      ExpressionSource<O,Field<O,P>,?> cast = (ExpressionSource<O,Field<O,P>,?>) child;
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
