/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class GetterImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends InvokeableImpl<O,P,Getter<O,P>,MethodInvocation>
      implements Getter<O,P>,
      ExpressionChainLink<O,Getter<O,P>>
{

   protected MethodInvocation invoke;

   private String fieldName;

   public GetterImpl(String fieldName, String type)
   {
      super(JDTHelper.getter(fieldName, type));
      this.fieldName = fieldName;
   }

   @Override
   public ExpressionSource<O,Getter<O,P>,?> getInvocationTarget()
   {
      return target;
   }

   @Override
   public Getter<O,P> setInvocationTarget(ExpressionSource<?,?,?> target)
   {
      this.target = chainExpression((ExpressionSource<O,?,?>) target);
      return this;
   }

   @Override
   public MethodInvocation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return invoke;
      }
      invoke = ast.newMethodInvocation();

      invoke.setName(ast.newSimpleName(method));
      if (target != null)
      {
         invoke.setExpression(wireAndGetExpression(target, this, ast));
      }
      return invoke;
   }

   @Override
   public MethodInvocation getInternal()
   {
      return invoke;
   }

   @Override
   public void setInternal(MethodInvocation jdtNode)
   {
      super.setInternal(jdtNode);
      this.invoke = jdtNode;
   }

   @Override
   public String getFieldName()
   {
      return fieldName;
   }
}
