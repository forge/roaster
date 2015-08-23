/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;

/**
 * Abstract marker interface that represents expressions requiring arguments
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ArgumentHolder<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends NonPrimitiveExpression<O,P,?>>
      extends ExpressionHolder<O>
{

   /**
    * Adds an argument to the expression
    * @param arg  The argument to be added
    * @return  The expression itself
    */
   public E addArgument(Argument<?,?,?> arg);

   /**
    * Returns the current list of arguments
    * @return  An immutable list containing the arguments
    */
   public List<Argument<O,E,?>> getArguments();
}
