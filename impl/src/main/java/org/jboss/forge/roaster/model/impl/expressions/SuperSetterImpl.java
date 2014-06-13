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
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperSetterImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AbstractMethodInvokeImpl<O,P,Setter<O,P>,SuperMethodInvocation>
      implements Setter<O,P>
{

   private SuperMethodInvocation setter;

   private ExpressionSource<O,Setter<O,P>,?> value;

   public SuperSetterImpl(String fieldName, String type, ExpressionSource<?,?,?> value)
   {
      super(JDTHelper.setter(fieldName, type));
      this.value = (ExpressionSource<O,Setter<O,P>,?>) value;
   }

   @Override
   public SuperMethodInvocation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return setter;
      }
      setter = ast.newSuperMethodInvocation();
      setter.setName(ast.newSimpleName(method));
      if (value != null)
      {
         setter.arguments().add(wireAndGetExpression(value, this, ast));
      }
      return setter;
   }

   @Override
   public SuperMethodInvocation getInternal()
   {
      return setter;
   }

   @Override
   public void setInternal(SuperMethodInvocation jdtNode)
   {
      super.setInternal(jdtNode);
      this.setter = jdtNode;
   }

   @Override
   public ExpressionSource<O,Setter<O,P>,?> getValue()
   {
      if (getArguments().isEmpty())
      {
         return null;
      }
      return getArguments().get(0);
   }
}
