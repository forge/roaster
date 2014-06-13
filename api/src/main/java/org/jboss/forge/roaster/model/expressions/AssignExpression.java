package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface AssignExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ExpressionSource<O>,
        Argument<O, T> {

    public AssignExpression<O, T> setLeft( Accessor left );

    public AssignExpression<O, T> setRight( Expression right );
}
