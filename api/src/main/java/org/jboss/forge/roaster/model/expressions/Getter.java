package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface Getter<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Accessor<O, T>,
        InvokeExpression<O,T>,
        Wireable<O, T> {

}
