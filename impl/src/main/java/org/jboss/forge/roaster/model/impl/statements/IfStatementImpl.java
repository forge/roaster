package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.IfStatement;
import org.jboss.forge.roaster.model.statements.Statement;

public class IfStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.IfStatement,IfStatement<O,T>>
        implements IfStatement<O,T> {

    private org.eclipse.jdt.core.dom.IfStatement iff;

    private Expression condition;
    private Statement thenBody;
    private Statement elseBody;

    public IfStatementImpl() { }


    @Override
    public IfStatement<O,T> setElse( Statement statement ) {
        this.elseBody = statement;
        return this;
    }

    @Override
    public IfStatement<O, T> setCondition( Expression condition ) {
        this.condition = condition;
        return this;
    }

    @Override
    public IfStatement<O,T> setThen( Statement statement ) {
        this.thenBody = statement;
        return this;
    }

    @Override
    public org.eclipse.jdt.core.dom.IfStatement getInternal() {
        return iff;
    }

    @Override
    public void materialize( AST ast ) {
        iff = ast.newIfStatement();

        if ( condition != null ) {
            iff.setExpression( wireAndGetExpression( condition, this, ast ) );
        }
        if ( thenBody != null ) {
            iff.setThenStatement( wireAndGetStatement( thenBody.wrap(), this, ast ) );
        }
        if ( elseBody != null ) {
            iff.setElseStatement( wireAndGetStatement( elseBody.wrap(), this, ast ) );
        }
    }
}
