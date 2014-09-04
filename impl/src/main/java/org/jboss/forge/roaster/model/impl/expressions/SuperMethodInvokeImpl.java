package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperMethodInvokeImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AbstractMethodInvokeImpl<O,T,SuperMethodInvocation> {

    protected SuperMethodInvocation invoke;

    public SuperMethodInvokeImpl( String method ) {
        super( method );
    }

    @Override
    public SuperMethodInvocation getInternal() {
        return invoke;
    }

    @Override
    public void materialize( AST ast ) {
        invoke = ast.newSuperMethodInvocation();

        invoke.setName( ast.newSimpleName( method ) );
        for ( Argument<O,MethodExpression<O,T>> arg : arguments ) {
            invoke.arguments().add( wireAndGetExpression( arg, this, ast ) );
        }
    }

}
