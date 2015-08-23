/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.StringLiteral;
import org.jboss.forge.roaster.model.source.JavaSource;

public class StringLiteralImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AccessorImpl<O,P,StringLiteral<O,P>,org.eclipse.jdt.core.dom.StringLiteral>
      implements StringLiteral<O,P>
{

   private String val;

   private org.eclipse.jdt.core.dom.StringLiteral literal;

   public StringLiteralImpl(String val)
   {
      this.val = val;
   }

   @Override
   public org.eclipse.jdt.core.dom.StringLiteral materialize(AST ast)
   {
      if (isMaterialized())
      {
         return literal;
      }
      literal = ast.newStringLiteral();
      literal.setLiteralValue(val);
      return literal;
   }

   @Override
   public org.eclipse.jdt.core.dom.StringLiteral getInternal()
   {
      return literal;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.StringLiteral jdtNode)
   {
      super.setInternal(jdtNode);
      this.literal = jdtNode;
   }

   public String getValue()
   {
      return val;
   }
}
