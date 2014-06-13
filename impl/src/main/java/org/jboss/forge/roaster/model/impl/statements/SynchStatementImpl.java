package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.SynchStatement;

public class SynchStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T,SynchronizedStatement,SynchStatement<O,T>>
        implements SynchStatement<O,T> {

    private SynchronizedStatement synch;

    private Expression expr;
    private Statement body;

    public SynchStatementImpl() { }

    @Override
    public SynchronizedStatement getInternal() {
        return synch;
    }

    @Override
    public void materialize( AST ast ) {
        synch = ast.newSynchronizedStatement();

        if ( expr != null ) {
            synch.setExpression( wireAndGetExpression( expr, this, ast ) );
        }
        if ( body != null ) {
            synch.setBody( (org.eclipse.jdt.core.dom.Block) wireAndGetStatement( body.wrap(), this, ast ) );
        }
    }

    @Override
    public SynchStatement<O, T> setSynchOn( Expression expr ) {
        this.expr = expr;
        return this;
    }

    @Override
    public SynchStatement<O, T> setBody( Statement block ) {
        this.body = block;
        return this;
    }
}
