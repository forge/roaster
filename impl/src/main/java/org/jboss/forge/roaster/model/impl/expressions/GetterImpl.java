package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.InvokeExpression;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class GetterImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends InvokeableImpl<O,T,MethodInvocation>
        implements Getter<O,T> {


    protected MethodInvocation invoke;

    public GetterImpl( String fieldName, String type ) {
        super( JDTHelper.getter( fieldName, type ) );
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        setTarget( (Expression<O,InvokeExpression<O,T>>) child );
    }

    @Override
    public InvokeExpression<O, T> setTarget( Expression<O, InvokeExpression<O, T>> target ) {
        this.target = target;
        return this;
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
    }
}
