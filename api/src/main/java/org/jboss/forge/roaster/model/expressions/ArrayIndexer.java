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
 * Represent an array indexing expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ArrayIndexer<O extends JavaSource<O>, P extends ExpressionHolder<O>>
      extends OrdinalArgument<O,P,ArrayIndexer<O,P>>,
      NonPrimitiveExpression<O,P,ArrayIndexer<O,P>>,
      InvocationTargetHolder<O,P,ArrayIndexer<O,P>>
{

   /**
    * Returns the expression returning the index to be accessed
    * @return The expression returning the index used by this <code>ArrayIndexer</code>
    */
   public ExpressionSource<O,ArrayIndexer<O,P>,?> getIndex();

   /**
    * Sets the expression returning the index to be used for accessing the array
    * @param index   An expression returning an integer, used to access the array
    * @return The <code>ArrayIndexer</code> itself
    */
   public ArrayIndexer<O,P> setIndex(ExpressionSource<?,?,?> index);

}
