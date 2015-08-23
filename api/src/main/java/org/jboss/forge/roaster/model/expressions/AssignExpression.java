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
 * Represent an assignment expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface AssignExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpression<O,P,AssignExpression<O,P>>,
      Argument<O,P,AssignExpression<O,P>>
{

   /**
    * Returns the assignment operator
    * @return  A string representing the assignment operator used in this expression
    * @see org.jboss.forge.roaster.model.expressions.Assignment
    */
   public String getOperator();

   /**
    * Sets the assignment operator
    * @param op   The assignment operator
    * @return  This <code>AssignExpression</code> expression
    */
   public AssignExpression<O,P> setOperator(Assignment op);

   /**
    * Returns the left accessor expression
    * @return The LHS of the assignment
    */
   public Accessor<O,AssignExpression<O,P>,?> getLeft();

   /**
    * Sets the left expression returning the variable to be assigned
    * @param left   The LHS of the assignment
    * @return  This <code>AssignExpression</code> expression
    */
   public AssignExpression<O,P> setLeft(Accessor<?,?,?> left);

   /**
    * Returns the right value expression
    * @return  The RHS of the assignment
    */
   public ExpressionSource<O,AssignExpression<O,P>,?> getRight();

   /**
    * Sets the right expression, whose value will be assigned
    * @param right   The RHS of the assignment
    * @return  This <code>AssignExpression</code> expression
    */
   public AssignExpression<O,P> setRight(ExpressionSource<?,?,?> right);
}
