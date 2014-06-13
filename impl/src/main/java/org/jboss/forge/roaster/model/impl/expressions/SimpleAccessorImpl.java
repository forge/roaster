package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.BareArgument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class SimpleAccessorImpl<O extends JavaSource<O>, T extends ExpressionSource<O>, J extends Expression>
        extends AccessorImpl<O,T,J>
        implements BareArgument<O,T> {

    @Override
    public Argument<O,T> inc() {
        return new PostFixImpl<O,T>( PostfixExpression.Operator.INCREMENT.toString(), this );
    }

    @Override
    public Argument<O,T> dec() {
        return new PostFixImpl<O,T>( PostfixExpression.Operator.DECREMENT.toString(), this );
    }

}
