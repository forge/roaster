package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface OperatorExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T>,
        ArgumentHolder<O, OperatorExpression<O,T>> {

    public OperatorExpression<O, T> addArgument( Argument<O, OperatorExpression<O, T>> expression );
}
