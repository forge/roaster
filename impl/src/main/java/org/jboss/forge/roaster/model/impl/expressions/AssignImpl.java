package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class AssignImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,Assignment>
        implements AssignExpression<O,T> {

    private Assignment axx;

    private Accessor left;
    private Expression right;
    private org.jboss.forge.roaster.model.expressions.Assignment op;

    public AssignImpl( org.jboss.forge.roaster.model.expressions.Assignment op ) {
        this.op = op;
    }

    @Override
    public AssignExpression<O, T> setLeft( Accessor left ) {
        this.left = left;
        return this;
    }

    @Override
    public AssignExpression<O, T> setRight( Expression right ) {
        this.right = right;
        return this;
    }

    @Override
    public Assignment getInternal() {
        return axx;
    }

    @Override
    public void materialize( AST ast ) {
        axx = ast.newAssignment();
        axx.setOperator( Assignment.Operator.toOperator( op.getOp() ) );

        if ( left != null ) {
            axx.setLeftHandSide( wireAndGetExpression( left, this, ast ) );
        }
        if ( right != null ) {
            axx.setRightHandSide( wireAndGetExpression( right, this, ast ) );
        }
    }
}
