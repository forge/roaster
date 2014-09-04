package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactory;

public interface ParenExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ExpressionFactory<O, T>,
        Argument<O, T> {

}
