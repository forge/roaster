package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface BareArgument<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T>,
        Accessor<O, T>,
        Wireable<O,T> {

    public Argument<O, T> inc();

    public Argument<O, T> dec();

}
