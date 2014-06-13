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
 * Represent a getter method invocation expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface Getter<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Accessor<O,P,Getter<O,P>>,
      MethodInvokeExpression<O,P,Getter<O,P>>
{

   /**
    * Returns the name of the field this getter is accessing
    * @return the name of the field this getter is accessing
    */
   public String getFieldName();
}
