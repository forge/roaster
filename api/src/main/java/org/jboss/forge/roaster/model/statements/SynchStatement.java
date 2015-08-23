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
 * Represents a synchronized block statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface SynchStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ControlFlowStatement<O,P,SynchStatement<O,P>>,
      BodyHolder<O,P,SynchStatement<O,P>>
{

   /**
    * Returns the expression that evaluates to the lock object
    * @return the expression that evaluates to the lock object
    */
   public ExpressionSource<O,SynchStatement<O,P>,?> getSynchOn();


   /**
    * Sets the expression that evaluates to the lock object
    * @param expr the expression returning the lock object
    * @return this <code>SynchStatement</code> itself
    */
   public SynchStatement<O,P> setSynchOn(ExpressionSource<?,?,?> expr);

}
