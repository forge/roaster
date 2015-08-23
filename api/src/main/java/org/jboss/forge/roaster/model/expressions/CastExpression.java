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
 * Represent a cast expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface CastExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,CastExpression<O,P>>,
      NonPrimitiveExpression<O,P,CastExpression<O,P>>
{

   /**
    * Returns the type of the cast
    * @return the name of the type the expression is being cast to
    */
   public String getType();

   /**
    * Returns the expression that is being cast
    * @return the expression being cast
    */
   public ExpressionSource<O,CastExpression<O,P>,?> getExpression();

   public CastExpression<O,P> setExpression( ExpressionSource<?,?,?> expr );
}
