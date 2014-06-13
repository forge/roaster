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
 * Represents an expression evaluation statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface EvalExpressionStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ExpressionStatement<O,P,EvalExpressionStatement<O,P>>
{

   /**
    * Sets the inner expression to be evaluated by this statement
    * @param expr the expression to be evaluated
    * @return this <code>EvalExpressionStatement</code> itself
    */
   public EvalExpressionStatement<O,P> setExpr(ExpressionSource<?,?,?> expr);

}


