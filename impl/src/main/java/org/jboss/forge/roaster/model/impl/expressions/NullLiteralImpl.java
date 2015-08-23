/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.NullLiteral;
import org.jboss.forge.roaster.model.source.JavaSource;

public class NullLiteralImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends LiteralImpl<O,P,NullLiteral<O,P>,org.eclipse.jdt.core.dom.NullLiteral>
      implements org.jboss.forge.roaster.model.expressions.NullLiteral<O,P>
{

   private org.eclipse.jdt.core.dom.NullLiteral literal;

   public NullLiteralImpl()
   {
   }

   @Override
   public org.eclipse.jdt.core.dom.NullLiteral materialize(AST ast)
   {
      if (isMaterialized())
      {
         return literal;
      }
      literal = ast.newNullLiteral();
      return literal;
   }

   @Override
   public org.eclipse.jdt.core.dom.NullLiteral getInternal()
   {
      return literal;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.NullLiteral jdtNode)
   {
      super.setInternal(jdtNode);
      this.literal = jdtNode;
   }

   public Object getValue()
   {
      return null;
   }
}
