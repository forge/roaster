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
public interface ArrayInit<O extends JavaSource<O>, P extends ExpressionHolder<O>>
      extends ExpressionSource<O,P,ArrayInit<O,P>>,
      NonPrimitiveExpression<O,P,ArrayInit<O,P>>
{

   /**
    * Adds a sub-array literal to this array constructing expression
    * @param subRow  The sub-array
    * @return  The <code>ArrayInit</code> itself
    */
   public ArrayInit<O,P> addElement(ArrayInit<?,?> subRow);

   /**
    * Adds an element to this array constructing expression
    * @param subElement The expression returning the element to be addeed to the array
    * @return  The <code>ArrayInit</code> itself
    */
   public ArrayInit<O,P> addElement(ExpressionSource<?,?,?> subElement);

   /**
    * Returns the current elements used to initialize the array
    * @return  An immutable list containing the element expressions
    */
   public List<ExpressionSource<O,ArrayInit<O,P>,?>> getElements();

   /**
    * Counts and returns the number of elements in this array
    * @return
    */
   public int size();

   /**
    * Returns the number of dimensions in the array, as inferred by the init expressions
    * Example : { {1}, {2}, {3} } size() is 3, but getDimension() is 2
    * @return the dimension
    */
   public int getDimension();

}
