package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;


public interface ChainableExpression<O extends JavaSource<O>, T extends ExpressionSource<O>> {

    public Accessor<O, T> dot();

}
