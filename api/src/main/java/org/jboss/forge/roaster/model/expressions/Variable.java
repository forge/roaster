package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface Variable<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends BareArgument<O, T> {

}
