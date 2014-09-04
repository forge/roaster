package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InvokeExpression;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.expressions.Wireable;
import org.jboss.forge.roaster.model.source.JavaSource;

public class MethodInvokeImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AbstractMethodInvokeImpl<O,T,MethodInvocation>
        implements Wireable<O,T> {

    protected MethodInvocation invoke;

    public MethodInvokeImpl( String method ) {
        super( method );
    }

    @Override
    public MethodInvocation getInternal() {
        return invoke;
    }

    @Override
    public void materialize( AST ast ) {
        invoke = ast.newMethodInvocation();

        getInternal().setName( ast.newSimpleName( method ) );
        if ( target != null ) {
            getInternal().setExpression( wireAndGetExpression( target, this, ast ) );
        }
        for ( Argument<O,MethodExpression<O,T>> arg : arguments ) {
            getInternal().arguments().add( wireAndGetExpression( arg, this, ast ) );
        }
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        setTarget( (Expression<O,InvokeExpression<O,T>>) child );
    }
}
