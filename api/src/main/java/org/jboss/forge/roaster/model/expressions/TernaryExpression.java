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
 * Represent a ternary expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface TernaryExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,TernaryExpression<O,P>>,
      NonPrimitiveExpression<O,P,TernaryExpression<O,P>>
{

   /**
    * Returns the expression defining the condition of this ternary expression
    * @return the condition part of this <code>TernaryExpression</code>
    */
   public ExpressionSource<O,TernaryExpression<O,P>,?> getCondition();

   /**
    * Sets the condition of this ternary expression
    * @param expression The condition expression
    * @return  this <code>TernaryExpression</code> itself
    */
   public TernaryExpression<O,P> setCondition(ExpressionSource<?,?,?> expression);


   /**
    * Returns the expression defining the then of this ternary expression
    * @return the then part of this <code>TernaryExpression</code>
    */
   public ExpressionSource<O,TernaryExpression<O,P>,?> getThenExpression();

   /**
    * Sets the then of this ternary expression
    * @param onTrue The then expression
    * @return  this <code>TernaryExpression</code> itself
    */
   public TernaryExpression<O,P> setThenExpression(ExpressionSource<?,?,?> onTrue);


   /**
    * Returns the expression defining the else of this ternary expression
    * @return the else part of this <code>TernaryExpression</code>
    */
   public ExpressionSource<O,TernaryExpression<O,P>,?> getElseExpression();

   /**
    * Sets the else of this ternary expression
    * @param onFalse The else expression
    * @return  this <code>TernaryExpression</code> itself
    */
   public TernaryExpression<O,P> setElseExpression(ExpressionSource<?,?,?> onFalse);


}
