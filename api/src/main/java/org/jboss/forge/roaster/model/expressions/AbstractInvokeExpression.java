package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface AbstractInvokeExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T>,
        ChainableExpression<O, T> {

}
