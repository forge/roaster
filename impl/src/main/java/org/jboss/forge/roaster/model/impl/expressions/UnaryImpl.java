package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class UnaryImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,PrefixExpression>
        implements UnaryExpression<O,T> {

    PrefixExpression expr;

    private Argument<O,? extends UnaryExpression<O,T>> arg;
    private PrefixOp op;

    public UnaryImpl( PrefixOp op, Argument<O,UnaryExpression<O,T>> expr ) {
        this.arg = expr;
        this.op = op;
    }

    @Override
    public PrefixExpression getInternal() {
        return expr;
    }

    @Override
    public void materialize( AST ast ) {
        expr = ast.newPrefixExpression();
        expr.setOperator( PrefixExpression.Operator.toOperator( op.getOp() ) );

        if ( arg != null ) {
            expr.setOperand( wireAndGetExpression( arg, this, ast ) );
        }
    }
}
