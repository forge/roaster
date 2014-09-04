package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface MethodExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends InvokeExpression<O,T>,
        ArgumentHolder<O, MethodExpression<O,T>> {

    public MethodExpression<O, T> setTarget( Expression<O, InvokeExpression<O, T>> target );

    public MethodExpression<O, T> addArgument( Argument<O, MethodExpression<O, T>> argument );
}
