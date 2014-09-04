package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ThrowStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
    extends StatementImpl<O,T,ThrowStatement, org.jboss.forge.roaster.model.statements.ThrowStatement<O,T>>
    implements org.jboss.forge.roaster.model.statements.ThrowStatement<O,T> {

    private Expression<O, org.jboss.forge.roaster.model.statements.ThrowStatement<O,T>> expression;
    private ThrowStatement throwStatement;

    public ThrowStatementImpl() {}

    @Override
    public ThrowStatement getInternal() {
        return throwStatement;
    }

    public void materialize( AST ast ) {
        throwStatement = ast.newThrowStatement();
        if ( expression != null ) {
            throwStatement.setExpression( wireAndGetExpression( expression, this, getAst() ) );
        }
    }

    @Override
    public org.jboss.forge.roaster.model.statements.ThrowStatement<O,T> setThrowable( Expression<O, org.jboss.forge.roaster.model.statements.ThrowStatement<O, T>> expr ) {
        this.expression = expr;
        return this;
    }
}
