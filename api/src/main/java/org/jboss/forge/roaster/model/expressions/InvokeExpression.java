package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;

public interface InvokeExpression<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AbstractInvokeExpression<O,T> {

    public InvokeExpression<O, T> setTarget( Expression<O, InvokeExpression<O, T>> target );

}
