package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface ConstructorBuilder<O extends JavaSource<O>, T extends ExpressionSource<O>> {

    public ConstructorExpression<O,T> newInstance( String klass );

    public ConstructorExpression<O,T> newInstance( Class<?> klass );

    public ArrayConstructorExpression<O,T> newArray( Class<?> klass );

    public ArrayConstructorExpression<O,T> newArray( String klass );

}
