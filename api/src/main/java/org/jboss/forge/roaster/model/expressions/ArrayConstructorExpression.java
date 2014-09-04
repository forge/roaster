package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface ArrayConstructorExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T> {

    public ArrayConstructorExpression<O,T> addDimension( Expression<O,ArrayConstructorExpression<O,T>> dim );

    public ArrayConstructorExpression<O,T> init( ArrayInit<O,ArrayConstructorExpression<O,T>> array );
}
