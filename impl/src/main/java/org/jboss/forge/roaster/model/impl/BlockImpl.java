package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.impl.statements.JdtStatementWrapper;
import org.jboss.forge.roaster.model.impl.statements.StatementImpl;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;

public class BlockImpl<O extends JavaSource<O>, T extends BlockHolder<O,T>>
    extends NodeImpl<O,T,org.eclipse.jdt.core.dom.Block>
    implements Block<O,T>,
        JdtStatementWrapper<O,T, org.eclipse.jdt.core.dom.Block> {

    private org.eclipse.jdt.core.dom.Block block;

    private BlockStatement body;

    public void setBlock( BlockStatement body ) {
        this.body = body;
    }

    @Override
    public org.eclipse.jdt.core.dom.Block getInternal() {
        return block;
    }

    @Override
    public void materialize( AST ast ) {
        
    }
}
