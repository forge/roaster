package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface ClassLiteral<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends Literal<O, T>,
        Accessor<O, T> {
}
