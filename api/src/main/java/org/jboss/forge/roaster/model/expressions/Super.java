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
 * Represent a "super" literal accessor expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface Super<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AccessBuilder<O,P,Super<O,P>>,
      NonPrimitiveExpression<O,P,Super<O,P>>
{

}
