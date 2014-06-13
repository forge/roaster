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
 * Represents a while loop statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface WhileStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends LoopingStatement<O,P,WhileStatement<O,P>>,
      BodyHolder<O,P,WhileStatement<O,P>>
{

   /**
    * Returns the conditional expression evaluated by this while statement
    * @return the conditional expression evaluated by this while statement
    */
   public ExpressionSource<O,WhileStatement<O,P>,?> getCondition();

   /**
    * Sets the condition expression evaluated by this while statement
    * @param expr The condition expression
    * @return this <code>While</code> itself
    */
   public WhileStatement<O,P> setCondition(ExpressionSource<?,?,?> expr);

}
