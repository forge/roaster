package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ExpressionStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
    extends StatementImpl<O,T,ExpressionStatement, org.jboss.forge.roaster.model.statements.ExpressionStatement<O,T>> {

    private Expression<O, org.jboss.forge.roaster.model.statements.ExpressionStatement<O,T>> expression;
    private ExpressionStatement exprStatement;

    public ExpressionStatementImpl( Expression expr ) {
        this.expression = expr;
    }

    public ExpressionStatementImpl() {
    }

    @Override
    public ExpressionStatement getInternal() {
        return exprStatement;
    }

    public void materialize( AST ast ) {
        exprStatement = ast.newExpressionStatement( wireAndGetExpression( expression, this, getAst() ) );
    }

}
