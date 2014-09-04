package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InvokeExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class InvokeableImpl<O extends JavaSource<O>, T extends ExpressionSource<O>, J extends org.eclipse.jdt.core.dom.Expression>
        extends BaseInvokeableImpl<O,T,J>
        implements InvokeExpression<O, T> {

    protected Expression<O,InvokeExpression<O,T>> target;


    public InvokeableImpl( String method ) {
        super( method );
    }

    @Override
    public Accessor<O, T> dot() {
        return new DotAccessorImpl<O, T>( this );
    }

}
