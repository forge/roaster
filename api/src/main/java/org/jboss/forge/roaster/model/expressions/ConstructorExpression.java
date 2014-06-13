package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface ConstructorExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T> {

    public ConstructorExpression<O,T> addArgument( Argument<O, ConstructorExpression<O,T>> arg );
}
