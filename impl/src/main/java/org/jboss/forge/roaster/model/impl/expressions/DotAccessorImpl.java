package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.expressions.AccessBuilder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.expressions.Wireable;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactoryImpl;

public class DotAccessorImpl<O extends JavaSource<O>,T extends ExpressionSource<O>>
        extends ExpressionFactoryImpl<O,T,org.eclipse.jdt.core.dom.Expression>
        implements Accessor<O,T> {

    private Expression parent;

    public DotAccessorImpl( Expression parent ) {
        this.parent = parent;
    }

    @Override
    public T getOrigin() {
        return null;
    }

    @Override
    public Field<O,T> field( String fieldName ) {
        Field<O,T> field = super.field( fieldName );
        swap( field, this.parent );
        return field;
    }

    @Override
    public Getter<O,T> getter( String fieldName, String type ) {
        Getter getter = super.getter( fieldName, type );
        swap( getter, this.parent );
        return getter;
    }

    @Override
    public Setter<O, T> setter( String fldName, String type, org.jboss.forge.roaster.model.expressions.Expression<O,Setter<O,T>> value ) {
        Setter<O,T> setter = super.setter( fldName, type, value );
        swap( setter, this.parent );
        return setter;
    }

    @Override
    public Variable<O,T> var( String varName ) {
        Variable<O,T> var = super.var( varName );
        swap( var, this.parent );
        return var;
    }

    @Override
    public MethodExpression<O,T> invoke( String method ) {
        MethodExpression<O,T> invoke = super.invoke( method );
        swap( invoke, this.parent );
        return invoke;
    }

    @Override
    public AccessBuilder<O, T> itemAt( Expression<O,ArrayIndexer<O,T>> index ) {
        AccessBuilder<O,T> accessor = super.itemAt( index );
        swap( accessor,this.parent );
        return accessor;
    }


    private void swap( Expression child, Expression parent ) {
        Wireable sub = (Wireable) child;
        Wireable sup = (Wireable) parent;

        sub.wireParent( sup.getParent() );
        sup.wireParent( child );
        sub.wireExpression( parent );
    }

    @Override
    public void materialize( AST ast ) {
        super.materialize( ast );
    }
}
