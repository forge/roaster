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
 * Represent an infix operator expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface OperatorExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,OperatorExpression<O,P>>,
      NonPrimitiveExpression<O,P,OperatorExpression<O,P>>,
      ArgumentHolder<O,P,OperatorExpression<O,P>>
{

   /**
    * Adds an argument to this operation, as the result of evaluating an expression
    * @param arg  The argument to be added
    * @return this <code>OperatorExpression</code> itself
    */
   public OperatorExpression<O,P> addArgument(Argument<?,?,?> arg);

   /**
    * Returns the infix operator used to combine the arguments
    * @return the infix operator used to combine the arguments
    * @see org.jboss.forge.roaster.model.expressions.Op
    */
   public String getOperator();

}
