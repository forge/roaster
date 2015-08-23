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
 * Represent a method call expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface MethodCallExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends MethodWithArgumentsInvokeExpression<O,P,MethodCallExpression<O,P>>
{

   /**
    * Sets the name of the method to be invoked
    * @param name The name of the method
    * @return  this <code>MethodCallExpression</code> itself
    */
   public MethodCallExpression<O,P> setMethodName(String name);

}
