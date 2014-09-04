package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.AssignStatement;

public class AssignStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T,ExpressionStatement,AssignStatement<O,T>>
        implements AssignStatement<O,T> {

    private Assignment axx;
    private ExpressionStatement statement;

    private Expression left;
    private Expression right;

    public AssignStatementImpl() {}

    @Override
    public AssignStatement<O, T> setRight( Expression right ) {
        this.right = right;
        return this;
    }

    @Override
    public AssignStatement<O,T> setLeft( Accessor left ) {
        this.left = left;
        return this;
    }

    @Override
    public ExpressionStatement getInternal() {
        return statement;
    }

    public void materialize( AST ast ) {
        axx = ast.newAssignment();
        axx.setOperator( Assignment.Operator.ASSIGN );
        statement = ast.newExpressionStatement( axx );

        if ( left != null ) {
            axx.setLeftHandSide( wireAndGetExpression( left, this, getAst() ) );
        }
        if ( right != null ) {
            axx.setRightHandSide( wireAndGetExpression( right, this, getAst() ) );
        }
    }

}
