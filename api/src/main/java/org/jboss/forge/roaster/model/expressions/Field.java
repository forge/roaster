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
 * Represent a field access expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface Field<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends OrdinalArgument<O,P,Field<O,P>>,
      NonPrimitiveExpression<O,P,Field<O,P>>,
      InvocationTargetHolder<O,P,Field<O,P>>
{

   /**
    * Returns the field name
    * @return the field name
    */
   public String getFieldName();

}
