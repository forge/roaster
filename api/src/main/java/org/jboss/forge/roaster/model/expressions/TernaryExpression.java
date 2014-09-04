package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface TernaryExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T> {

    public TernaryExpression<O, T> setCondition( Expression expression );

    public TernaryExpression<O, T> setIfExpression( Expression onTrue );

    public TernaryExpression<O, T> setElseExpression( Expression onFalse );

}
