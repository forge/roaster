package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.model.expressions.AccessBuilder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class AccessorImpl<O extends JavaSource<O>,T extends ExpressionSource<O>, X extends Expression>
    extends ArgumentImpl<O,T,X>
    implements Accessor<O,T> {

    public Field<O,T> field( String fieldName ) {
        return new DotAccessorImpl<O,T>( this ).field( fieldName );
    }

    public Getter<O,T> getter( String field, String klass ) {
        return new DotAccessorImpl<O,T>( this ).getter( field, klass );
    }

    public Getter<O,T> getter( String field, Class klass ) {
        return new DotAccessorImpl<O,T>( this ).getter( field, klass );
    }

    public Setter<O,T> setter( String fldName, String type, org.jboss.forge.roaster.model.expressions.Expression<O,Setter<O,T>> value ) {
        return new DotAccessorImpl<O,T>( this ).setter( fldName, type, value );
    }

    public MethodExpression<O,T> invoke( String method ) {
        return new DotAccessorImpl<O,T>( this ).invoke( method );
    }

    @Override
    public AccessBuilder<O, T> itemAt( org.jboss.forge.roaster.model.expressions.Expression<O,ArrayIndexer<O,T>> index ) {
        return new DotAccessorImpl<O, T>( this ).itemAt( index );
    }

}
