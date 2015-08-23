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
 * Represent an instanceof expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface InstanceofExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,InstanceofExpression<O,P>>,
      NonPrimitiveExpression<O,P,InstanceofExpression<O,P>>
{

   /**
    * Returns the name of the type instanceof is being checked against
    * @return the name of the type instanceof is being checked against
    */
   public String getTypeName();

   /**
    * Returns the expression being tested for instanceof
    * @return the expression being tested for instanceof
    */
   public ExpressionSource<O,InstanceofExpression<O,P>,?> getExpression();
}
