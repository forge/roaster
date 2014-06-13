package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.impl.NodeImpl;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.Statements;

public abstract class StatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>, J extends org.eclipse.jdt.core.dom.Statement,S extends Statement<O,T,S>>
        extends NodeImpl<O,T,J>
        implements Statement<O,T,S>,
                   JdtStatementWrapper<O,T,J> {

    private String label;

    protected StatementImpl() {
    }

    public BlockStatement wrap() {
        BlockStatement wrapper = Statements.newBlock();
        wrapper.addStatement( this );
        return wrapper;
    }

    @Override
    public S setLabel( String label ) {
        this.label = label;
        return (S) this;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean hasLabel() {
        return label != null && label.length() > 0;
    }

    @Override
    public <K extends org.eclipse.jdt.core.dom.Statement, P> org.eclipse.jdt.core.dom.Statement wireAndGetStatement( Statement statement, P parent, AST ast ) {
        org.eclipse.jdt.core.dom.Statement stat = super.wireAndGetStatement( statement, parent, ast );
        if ( statement.hasLabel() ) {
            LabeledStatement labeledStatement = ast.newLabeledStatement();
            labeledStatement.setBody( stat );
            labeledStatement.setLabel( ast.newSimpleName( statement.getLabel() ) );
            return labeledStatement;
        } else {
            return stat;
        }
    }
}
