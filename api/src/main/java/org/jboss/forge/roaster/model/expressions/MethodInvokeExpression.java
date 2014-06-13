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
 * Abstract interface that represent a method / accessor expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface MethodInvokeExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      M extends MethodInvokeExpression<O,P,M>>
      extends Argument<O,P,M>,
      Accessor<O,P,M>,
      NonPrimitiveExpression<O,P,M>,
      InvocationTargetHolder<O,P,M>
{

   /**
    * Returns the name of the method being invoked
    * @return the name of the method
    */
   public String getMethodName();

}
