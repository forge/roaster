package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.WhileStatement;

public class WhileStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.WhileStatement,WhileStatement<O,T>>
        implements WhileStatement<O,T> {

    private org.eclipse.jdt.core.dom.WhileStatement rep;

    private Statement body;
    private Expression condition;

    public WhileStatementImpl() { }

    @Override
    public WhileStatement<O,T> setBody( Statement statement ) {
        this.body = statement;
        return this;
    }

    @Override
    public WhileStatement<O, T> setCondition( Expression expr ) {
        this.condition = expr;
        return this;
    }

    @Override
    public org.eclipse.jdt.core.dom.WhileStatement getInternal() {
        return rep;
    }

    @Override
    public void materialize( AST ast ) {
        rep = ast.newWhileStatement();

        if ( body != null ) {
            rep.setBody( wireAndGetStatement( body.wrap(), this, ast ) );
        }
        if ( condition != null ) {
            rep.setExpression( wireAndGetExpression( condition, this, ast ) );
        }
    }
}
