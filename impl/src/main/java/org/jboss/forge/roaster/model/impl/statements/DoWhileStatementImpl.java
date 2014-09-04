package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.DoStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.DoWhileStatement;
import org.jboss.forge.roaster.model.statements.Statement;

public class DoWhileStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T,DoStatement,DoWhileStatement<O,T>>
        implements DoWhileStatement<O,T> {

    private DoStatement rep;

    private Statement body;
    private Expression condition;

    public DoWhileStatementImpl() { }

    @Override
    public DoWhileStatement<O,T> setBody( Statement statement ) {
        this.body = statement;
        return this;
    }

    @Override
    public DoWhileStatement<O, T> setCondition( Expression expr ) {
        this.condition = expr;
        return this;
    }

    @Override
    public DoStatement getInternal() {
        return rep;
    }

    @Override
    public void materialize( AST ast ) {
        rep = ast.newDoStatement();

        if ( body != null ) {
            rep.setBody( wireAndGetStatement( body.wrap(), this, ast ) );
        }
        if ( condition != null ) {
            rep.setExpression( wireAndGetExpression( condition, this, ast ) );
        }
    }
}
