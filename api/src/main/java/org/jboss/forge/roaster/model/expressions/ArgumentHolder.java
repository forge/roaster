package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface ArgumentHolder<O extends JavaSource<O>, X extends ExpressionSource<O>> {

    public ArgumentHolder<O,X> addArgument( Argument<O, X> arg );
}
