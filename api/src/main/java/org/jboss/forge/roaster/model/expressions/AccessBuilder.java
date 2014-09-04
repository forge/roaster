package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.source.JavaSource;

public interface AccessBuilder<O extends JavaSource<O>, T extends ExpressionSource<O>>
    extends Expression<O,T> {

    public Field<O, T> field( String field );

    public Getter<O, T> getter( String field, String klass );

    public Getter<O, T> getter( String field, Class klass );

    public Setter<O, T> setter( String fldName, String type, Expression<O, Setter<O, T>> value );

    public Setter<O, T> setter( String fldName, Class type, Expression<O, Setter<O, T>> value );

    public MethodExpression<O, T> invoke( String method );

    public AccessBuilder<O,T> itemAt( Expression<O,ArrayIndexer<O,T>> index );

}
