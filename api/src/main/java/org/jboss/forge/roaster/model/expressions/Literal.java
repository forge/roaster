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
 * Abstract interface that represents a literal expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface Literal<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends Literal<O,P,E>>
      extends Argument<O,P,E>
{
}
