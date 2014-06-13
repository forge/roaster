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
 * Marker interface representing expressions returning values that can be incremented or decremented, in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface OrdinalArgument<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends NonPrimitiveExpression<O,P,E>>
      extends Argument<O,P,E>,
      Accessor<O,P,E>,
      NonPrimitiveExpression<O,P,E>
{

   /**
    * Creates a postfix self-increment expression having the result of this expression as an argument
    * @return a new <code>PostFixExpression</code>
    * @see org.jboss.forge.roaster.model.expressions.PostfixOp
    */
   public PostFixExpression<O,OrdinalArgument<O,P,E>> inc();

   /**
    * Creates a postfix self-decrement expression having the result of this expression as an argument
    * @return a new <code>PostFixExpression</code>
    * @see org.jboss.forge.roaster.model.expressions.PostfixOp
    */
   public PostFixExpression<O,OrdinalArgument<O,P,E>> dec();

}
