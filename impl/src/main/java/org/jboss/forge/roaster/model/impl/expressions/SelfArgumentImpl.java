/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.ThisLiteral;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SelfArgumentImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AccessorImpl<O,P,ThisLiteral<O,P>,ThisExpression>
      implements ThisLiteral<O,P>,
      ExpressionChainLink<O,P>
{

   public SelfArgumentImpl()
   {
   }

   private ThisExpression self;

   @Override
   public ThisExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return self;
      }
      self = ast.newThisExpression();
      return self;
   }

   @Override
   public ThisExpression getInternal()
   {
      return self;
   }

   @Override
   public void setInternal(ThisExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.self = jdtNode;
   }

   @Override
   public ExpressionSource<O,P,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      return (ExpressionSource<O,P,?>) child;
   }
}
