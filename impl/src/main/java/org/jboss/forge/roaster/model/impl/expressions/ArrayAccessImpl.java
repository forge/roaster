package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ArrayAccessImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
    extends SimpleAccessorImpl<O,T,ArrayAccess>
    implements ArrayIndexer<O,T> {

    private Expression<O,ArrayIndexer<O,T>> index;
    private Expression<O,T> target;
    private ArrayAccess axx;

    public ArrayAccessImpl( Expression<O,ArrayIndexer<O,T>> index ) {
        this.index = index;
    }

    @Override
    public ArrayAccess getInternal() {
        return axx;
    }

    @Override
    public void materialize( AST ast ) {
        axx = ast.newArrayAccess();
        axx.setIndex( wireAndGetExpression( index, this, ast ) );
        if ( target != null ) {
            axx.setArray( wireAndGetExpression( target, this, ast ) );
        }
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        this.target = child;
    }
}
