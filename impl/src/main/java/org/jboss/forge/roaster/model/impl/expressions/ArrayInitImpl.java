package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayInitImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ExpressionImpl<O,T,ArrayInitializer>
        implements ArrayInit<O,T> {

    private List<ExpressionSource<O>> elements = Collections.EMPTY_LIST;

    private ArrayInitializer init;

    @Override
    public ArrayInit<O, T> addElement( ArrayInit<O, T> subRow ) {
        if ( elements.isEmpty() ) {
            elements = new ArrayList(  );
        }
        elements.add( subRow );
        return this;
    }

    @Override
    public ArrayInit<O, T> addElement( Expression<O, T> subElement ) {
        if ( elements.isEmpty() ) {
            elements = new ArrayList(  );
        }
        elements.add( subElement );
        return this;
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public ArrayInitializer getInternal() {
        return init;
    }

    @Override
    public void materialize( AST ast ) {
        this.init = ast.newArrayInitializer();
        for ( ExpressionSource<O> src : elements ) {
            this.init.expressions().add( wireAndGetExpression( src, this, ast ) );
        }
    }
}
