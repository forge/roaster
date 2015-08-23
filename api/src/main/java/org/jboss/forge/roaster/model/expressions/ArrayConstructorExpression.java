/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;

/**
 * Represent an array constructor expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ArrayConstructorExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,ArrayConstructorExpression<O,P>>,
      NonPrimitiveExpression<O,P,ArrayConstructorExpression<O,P>>
{

   /**
    * Adds a dimension to the array, allocating <code>dim</code> slots
    * @param dim  the number of elements in the new array dimension
    * @return  The <code>ArrayConstructorExpression/code> itself
    */
   public ArrayConstructorExpression<O,P> addDimension(ExpressionSource<?,?,?> dim);

   /**
    * Initializes the array using an <code>ArrayInit</code> expressoin
    * @param array  the initial value for the array variable
    * @return  The <code>ArrayConstructorExpression/code> itself
    */
   public ArrayConstructorExpression<O,P> init(ArrayInit<?,?> array);

   /**
    * Returns the array initialization expression, or null if none has been set
    * @return An <code>ArrayInit</code> expression
    */
   public ArrayInit<O,ArrayConstructorExpression<O,P>> getInit();

   /**
    * Returns the expressions defining the number of slots for each array dimension
    * @return An immutable list containing the expressions initializing each dimension
    */
   public List<ExpressionSource<O,ArrayConstructorExpression<O,P>,?>> getDimensions();

   /**
    * Returns the number of dimensions of the array being constructed
    * @return the number of dimensions for this array
    */
   public int getDimension();
}
