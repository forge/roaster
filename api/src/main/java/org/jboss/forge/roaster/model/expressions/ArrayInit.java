package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface ArrayInit<O extends JavaSource<O>, T extends ExpressionSource<O>>
    extends ExpressionSource<O> {

    public ArrayInit<O,T> addElement( ArrayInit<O,T> subRow );

    public ArrayInit<O,T> addElement( Expression<O,T> subElement );

    public int size();

}
