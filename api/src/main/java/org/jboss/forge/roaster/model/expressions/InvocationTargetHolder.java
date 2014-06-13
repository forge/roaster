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
 * Abstract marker interface that represents expressions that can be invoked
 * on the result of the evaluation of another expression
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface InvocationTargetHolder<O extends JavaSource<O>,
                                        P extends ExpressionHolder<O>,
                                        E extends NonPrimitiveExpression<O,P,E>>
{

   /**
    * The expression on which this <code>Expression</code> is invoked
    * @return The expression on which this <code>Expression</code> is invoked
    */
   public ExpressionSource<O,E,?> getInvocationTarget();

   /**
    * Sets the expression on which this <code>Expression</code> is invoked
    * @param target  The expression on which this <code>Expression</code> is invoked
    * @return  This <code>Expression</code> itself
    */
   public E setInvocationTarget(ExpressionSource<?,?,?> target);

}
