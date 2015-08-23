/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperGetterImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends InvokeableImpl<O,P,Getter<O,P>,SuperMethodInvocation>
      implements Getter<O,P>
{

   protected SuperMethodInvocation invoke;

   private String fieldName;

   public SuperGetterImpl(String fieldName, String type)
   {
      super(JDTHelper.getter(fieldName, type));
      target = new SuperImpl<O,Getter<O,P>>();
      this.fieldName = fieldName;
   }

   public ExpressionSource<O,Getter<O,P>,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      ExpressionSource<O,Getter<O,P>,?> cast = (ExpressionSource<O,Getter<O,P>,?>) child;
      setInvocationTarget(cast);
      return cast;
   }


   @Override
   public Getter<O,P> setInvocationTarget(ExpressionSource<?,?,?> target)
   {
      chainExpression((ExpressionSource<O,?,?>) target);
      return this;
   }

   @Override
   public ExpressionSource<O,Getter<O,P>,?> getInvocationTarget()
   {
      return target;
   }

   @Override
   public SuperMethodInvocation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return invoke;
      }
      invoke = ast.newSuperMethodInvocation();
      invoke.setName(ast.newSimpleName(method));
      return invoke;
   }

   @Override
   public SuperMethodInvocation getInternal()
   {
      return invoke;
   }

   @Override
   public void setInternal(SuperMethodInvocation jdtNode)
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
