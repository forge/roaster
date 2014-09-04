package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BreakStatement;

public class BreakStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.BreakStatement,BreakStatement<O,T>>
        implements BreakStatement<O,T> {

    private org.eclipse.jdt.core.dom.BreakStatement ret;

    private String target;

    public BreakStatementImpl() { }

    @Override
    public org.eclipse.jdt.core.dom.BreakStatement getInternal() {
        return ret;
    }

    @Override
    public void materialize( AST ast ) {
        ret = ast.newBreakStatement();
        if ( target != null ) {
            ret.setLabel( ast.newSimpleName( target ) );
        }
    }

    @Override
    public BreakStatement<O, T> setTarget( String label ) {
        this.target = label;
        return this;
    }
}
