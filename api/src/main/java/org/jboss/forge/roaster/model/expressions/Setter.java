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
 * Represent a setter method invocation expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface Setter<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends ExpressionSource<O,P,Setter<O,P>>,
      MethodWithArgumentsInvokeExpression<O,P,Setter<O,P>>
{

   /**
    * Returns the expression that evaluates to the value being set
    * @return the expression that evaluates to the value being set
    */
   public ExpressionSource<O,Setter<O,P>,?> getValue();
}
