package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface Expression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ExpressionSource<O>,
        Origin<T> {


}
