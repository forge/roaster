/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a throw statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ThrowStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ControlFlowStatement<O,P,ThrowStatement<O,P>>
{

   /**
    * Returns the expression that evaluates to the throwable object
    * @return the expression that evaluates to the throwable object
    */
   public ExpressionSource<O,ThrowStatement<O,P>,?> getThrowable();

   /**
    * Sets the expression that evaluates to the throwable object
    * @param expr the expression returning the throwable object
    * @return this <code>Thr</code> itself
    */
   public ThrowStatement<O,P> setThrowable(ExpressionSource<?,?,?> expr);

}
