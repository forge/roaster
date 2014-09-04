package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Wireable;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SelfArgumentImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AccessorImpl<O,T,ThisExpression>
        implements Accessor<O,T>,
                   Wireable<O,T> {

    public SelfArgumentImpl() {}

    ThisExpression self;

    @Override
    public ThisExpression getInternal() {
        return self;
    }

    @Override
    public void materialize( AST ast ) {
        self = ast.newThisExpression();
    }

    @Override
    public void wireExpression( org.jboss.forge.roaster.model.expressions.Expression<O, T> child ) {
        throw new UnsupportedOperationException( "This method should not be invoked, since 'this' can only be the start of a dot-chain" );
    }
}
