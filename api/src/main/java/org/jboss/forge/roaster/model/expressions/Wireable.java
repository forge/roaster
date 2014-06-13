package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface Wireable<O extends JavaSource<O>, T extends ExpressionSource<O>> {

    void wireExpression( Expression<O, T> child );

    ExpressionSource getParent();

    void wireParent( ExpressionSource parent );

}
