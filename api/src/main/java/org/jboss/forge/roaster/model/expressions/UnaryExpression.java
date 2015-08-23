/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represent a prexif, unary expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface UnaryExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,UnaryExpression<O,P>>,
      NonPrimitiveExpression<O,P,UnaryExpression<O,P>>
{

   /**
    * Returns the single argument of this expression
    * @return  the <code>Argument</code> of this <code>UnaryExpression</code>
    */
   public Argument<O,UnaryExpression<O,P>,?> getExpression();

   /**
    * Sets the argument of this expression
    * @param arg  the argument
    * @return this <code>UnaryExpression</code> itself
    */
   public UnaryExpression<O,P> setExpression( ExpressionSource<?,?,?> arg );

   /**
    * Returns the operator used in this expression
    * @return the operator used in this expression
    * @see org.jboss.forge.roaster.model.expressions.PrefixOp
    */
   public String getOperator();
}
