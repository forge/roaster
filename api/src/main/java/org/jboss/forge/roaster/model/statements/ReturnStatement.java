/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a return statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ReturnStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BranchingStatement<O,P,ReturnStatement<O,P>>,
      ExpressionHolder<O>
{

   /**
    * Returns the expression used to compute the return value
    * @return the expression used to compute the return value
    */
   public ExpressionSource<O,ReturnStatement<O,P>,?> getReturn();

   /**
    * Sets the expression used to compute the value returned by the statement
    * @param expr the return value expression
    * @return this <code>ReturnStatement</code>
    */
   public ReturnStatement<O,P> setReturn(ExpressionSource<?,?,?> expr);

}
