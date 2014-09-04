package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface Accessor<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Argument<O, T>,
        AccessBuilder<O, T> {

}
