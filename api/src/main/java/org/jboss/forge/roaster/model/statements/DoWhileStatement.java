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
 * Represents a do while loop statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface DoWhileStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends LoopingStatement<O,P,DoWhileStatement<O,P>>,
      BodyHolder<O,P,DoWhileStatement<O,P>>
{

   /**
    * Returns the conditional expression evaluated by this loop
    * @return the conditional expression evaluated by this loop
    */
   public ExpressionSource<O,DoWhileStatement<O,P>,?> getCondition();

   /**
    * Sets the condition expression evaluated by this loop
    * @param expr The condition expression
    * @return this <code>DoWhileStatement</code> itself
    */
   public DoWhileStatement<O,P> setCondition(ExpressionSource<?,?,?> expr);

}
