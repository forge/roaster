package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.TernaryExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class TernaryImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,ConditionalExpression>
        implements TernaryExpression<O,T> {

    private ConditionalExpression ternary;

    private Expression condition;
    private Expression onTrue;
    private Expression onFalse;

    public TernaryImpl() {}

    @Override
    public TernaryExpression<O,T> setCondition( Expression expression ) {
        this.condition = expression;
        return this;
    }

    @Override
    public TernaryExpression<O,T> setIfExpression( Expression onTrue ) {
        this.onTrue = onTrue;
        return this;
    }

    @Override
    public TernaryExpression<O,T> setElseExpression( Expression onFalse ) {
        this.onFalse = onFalse;
        return this;
    }

    @Override
    public ConditionalExpression getInternal() {
        return ternary;
    }

    @Override
    public void materialize( AST ast ) {
        ternary = ast.newConditionalExpression();

        if ( condition != null ) {
            ternary.setExpression( wireAndGetExpression( condition, this, ast ) );
        }
        if ( onTrue != null ) {
            ternary.setThenExpression( wireAndGetExpression( onTrue, this, ast ) );
        }
        if ( onFalse != null ) {
            ternary.setElseExpression( wireAndGetExpression( onFalse, this, ast ) );
        }
    }
}
