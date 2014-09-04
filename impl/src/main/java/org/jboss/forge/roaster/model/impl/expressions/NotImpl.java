package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class NotImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends UnaryImpl<O,T> {

    public NotImpl( Argument<O,UnaryExpression<O,T>> expr ) {
        super( PrefixOp.NOT, expr );
    }

}
