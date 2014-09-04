package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ContinueStatement;

public class ContinueStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.ContinueStatement,ContinueStatement<O,T>>
        implements ContinueStatement<O,T> {

    private org.eclipse.jdt.core.dom.ContinueStatement ret;

    private String target;

    public ContinueStatementImpl() { }

    @Override
    public org.eclipse.jdt.core.dom.ContinueStatement getInternal() {
        return ret;
    }

    @Override
    public void materialize( AST ast ) {
        ret = ast.newContinueStatement();
        if ( target != null ) {
            ret.setLabel( ast.newSimpleName( target ) );
        }
    }

    @Override
    public ContinueStatement<O, T> setTarget( String label ) {
        this.target = label;
        return this;
    }
}
