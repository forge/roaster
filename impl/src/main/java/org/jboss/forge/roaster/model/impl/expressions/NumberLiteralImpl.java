/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.NumberLiteral;
import org.jboss.forge.roaster.model.expressions.PrimitiveLiteral;
import org.jboss.forge.roaster.model.source.JavaSource;

public class NumberLiteralImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends LiteralImpl<O,P,PrimitiveLiteral<O,P>,org.eclipse.jdt.core.dom.NumberLiteral>
      implements NumberLiteral<O,P>
{

   private Number val;

   private org.eclipse.jdt.core.dom.NumberLiteral literal;

   public NumberLiteralImpl(Number val)
   {
      this.val = val;
   }

   @Override
   public org.eclipse.jdt.core.dom.NumberLiteral materialize(AST ast)
   {
      if (isMaterialized())
      {
         return literal;
      }
      literal = ast.newNumberLiteral(val.toString());
      return literal;
   }

   @Override
   public org.eclipse.jdt.core.dom.NumberLiteral getInternal()
   {
      return literal;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.NumberLiteral jdtNode)
   {
      super.setInternal(jdtNode);
      this.literal = jdtNode;
   }

   public Number getValue()
   {
      return val;
   }
}
