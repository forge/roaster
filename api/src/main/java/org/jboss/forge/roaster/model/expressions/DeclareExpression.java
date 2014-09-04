package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface DeclareExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T> {

    public DeclareExpression<O, T> init( Expression expr );

}
