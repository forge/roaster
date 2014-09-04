package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

public class AssertStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends    StatementImpl<O,T,AssertStatement, org.jboss.forge.roaster.model.statements.AssertStatement<O,T>>
        implements org.jboss.forge.roaster.model.statements.AssertStatement<O,T> {

    private AssertStatement assrt;
    private Expression<O, org.jboss.forge.roaster.model.statements.AssertStatement<O,T>> assertion;
    private Expression<O, org.jboss.forge.roaster.model.statements.AssertStatement<O,T>> message;


    @Override
    public AssertStatement getInternal() {
        return assrt;
    }

    @Override
    public void materialize( AST ast ) {
        assrt = ast.newAssertStatement();
        if ( assertion != null ) {
            assrt.setExpression( wireAndGetExpression( assertion, this, ast ) );
        }
        if ( message != null ) {
            assrt.setMessage( wireAndGetExpression( message, this, ast ) );
        }
    }

    @Override
    public org.jboss.forge.roaster.model.statements.AssertStatement<O, T> setAssertion( Expression<O, org.jboss.forge.roaster.model.statements.AssertStatement<O,T>> expression ) {
        this.assertion = expression;
        return this;
    }

    @Override
    public org.jboss.forge.roaster.model.statements.AssertStatement<O, T> setMessage( Expression<O, org.jboss.forge.roaster.model.statements.AssertStatement<O,T>> msg ) {
        this.message = msg;
        return this;
    }
}
