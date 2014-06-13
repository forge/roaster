/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.spi;


import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.AccessBuilder;
import org.jboss.forge.roaster.model.expressions.BasicExpressionFactory;
import org.jboss.forge.roaster.model.expressions.NonPrimitiveExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * This interface supports the creation of expressions to be used in statements
 * @param <O>
 * @see  org.jboss.forge.roaster.spi.StatementFactory
 */
public interface ExpressionFactory<O extends JavaSource<O>,
                                           P extends ExpressionHolder<O>,
                                           E extends NonPrimitiveExpression<O,P,E>>
    extends AccessBuilder<O,P,E>,
        BasicExpressionFactory<O,P> {

}
