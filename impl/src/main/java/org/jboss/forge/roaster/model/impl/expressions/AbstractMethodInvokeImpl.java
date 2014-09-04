package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InvokeExpression;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMethodInvokeImpl<O extends JavaSource<O>, T extends ExpressionSource<O>,J extends org.eclipse.jdt.core.dom.Expression>
        extends InvokeableImpl<O,T,J>
        implements MethodExpression<O,T> {

    protected List<Argument<O,MethodExpression<O,T>>> arguments = Collections.EMPTY_LIST;

    public AbstractMethodInvokeImpl( String method ) {
        super( method );
    }

    @Override
    public MethodExpression<O, T> addArgument( Argument<O,MethodExpression<O,T>> argument ) {
        if ( arguments.isEmpty() ) {
            arguments = new LinkedList<Argument<O,MethodExpression<O,T>>>();
        }
        arguments.add( argument );
        return this;
    }


    @Override
    public MethodExpression<O, T> setTarget( Expression<O, InvokeExpression<O, T>> target ) {
        this.target = target;
        return this;
    }
}
