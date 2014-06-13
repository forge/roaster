package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.Statement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BlockStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.Block,BlockStatement<O,T>>
        implements BlockStatement<O,T> {

    private org.eclipse.jdt.core.dom.Block block;
    private List<Statement<?,?,?>> statements = Collections.EMPTY_LIST;

    public BlockStatementImpl() { }

    @Override
    public BlockStatement<O, T> addStatement( Statement<?,?,?> statement ) {
        if ( statements.isEmpty() ) {
            statements = new LinkedList<Statement<?,?,?>>();
        }
        statements.add( statement );
        return this;
    }

    @Override
    public BlockStatement<O, T> addStatement( Expression<?,?> expression ) {
        return addStatement( new ExpressionStatementImpl<O, Block<O, T>>( expression ) );
    }

    @Override
    public org.eclipse.jdt.core.dom.Block getInternal() {
        return block;
    }

    @Override
    public void materialize( AST ast ) {
        block = ast.newBlock();

        for ( Statement<?,?,?> statement : statements ) {
            block.statements().add( wireAndGetStatement( statement, this, ast ) );
        }
    }

    public BlockStatement wrap() {
        return this;
    }

}