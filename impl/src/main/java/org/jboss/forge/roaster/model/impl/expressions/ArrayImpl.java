package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ArrayImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
    extends    ArgumentImpl<O,T,ArrayCreation>
    implements ArrayConstructorExpression<O,T> {

    private ArrayCreation array;

    private String type;
    private List<Expression<O,ArrayConstructorExpression<O,T>>> dims = Collections.EMPTY_LIST;
    private ArrayInit<O,ArrayConstructorExpression<O,T>> init;

    public ArrayImpl( String type ) {
        this.type = type;
    }

    @Override
    public ArrayCreation getInternal() {
        return array;
    }

    @Override
    public void materialize( AST ast ) {
        array = ast.newArrayCreation();
        array.setType( (ArrayType) JDTHelper.getType( type + new String( new char[ getDimension() ] ).replace( "\0", "[]" ), ast ) );
        for ( Expression<O,ArrayConstructorExpression<O,T>> dim : dims ) {
            array.dimensions().add( wireAndGetExpression( dim, this, ast ) );
        }
        if ( init != null ) {
            array.setInitializer( (ArrayInitializer) wireAndGetExpression( init, this, ast ) );
        }
    }

    @Override
    public ArrayConstructorExpression<O, T> addDimension( Expression<O, ArrayConstructorExpression<O, T>> dim ) {
        if ( dims.isEmpty() ) {
            dims = new ArrayList<Expression<O, ArrayConstructorExpression<O, T>>>();
        }
        dims.add( dim );
        return this;
    }

    @Override
    public ArrayConstructorExpression<O, T> init( ArrayInit<O, ArrayConstructorExpression<O, T>> array ) {
        this.init = array;
        return this;
    }

    public int getDimension() {
        if ( ! dims.isEmpty() ) {
            return dims.size();
        } else if ( init != null ) {
            return init.size();
        }
        return 0;
    }

    @Override
    public ArrayInit<O,ArrayConstructorExpression<O,T>> vec() {
        ArrayInit<O,ArrayConstructorExpression<O,T>> init = new ArrayInitImpl<O,ArrayConstructorExpression<O, T>>();
        this.init = init;
        return init;
    }
}

