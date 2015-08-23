/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactory;

/**
 * Represent a parenthesized expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ParenExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends ExpressionFactory<O,P,ParenExpression<O,P>>,
      NonPrimitiveExpression<O,P,ParenExpression<O,P>>,
      Argument<O,P,ParenExpression<O,P>>
{

   /**
    * Returns the expression within parenthesis
    * @return the argument of this <code>Pare</code>
    */
   public ExpressionSource<O,ParenExpression<O,P>,?> getExpression();
}
