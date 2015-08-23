/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.impl.statements.JdtStatementWrapper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;


public abstract class BlockImpl<O extends JavaSource<O>, 
                                P extends BlockHolder<O>,
                                B extends Block<O,P>>
    extends NodeImpl<O,P,org.eclipse.jdt.core.dom.Block>
    implements Block<O,P>,
               JdtStatementWrapper<O,B,org.eclipse.jdt.core.dom.Block> {

    protected org.eclipse.jdt.core.dom.Block jdtBlock;

    @Override
    public boolean isMaterialized() {
        return getInternal() != null;
    }

    @Override
    public org.eclipse.jdt.core.dom.Block getInternal() {
        return jdtBlock;
    }

    @Override
    public void setInternal(org.eclipse.jdt.core.dom.Block jdtNode) {
        super.setInternal(jdtNode);
        this.jdtBlock = jdtNode;
    }

    public org.eclipse.jdt.core.dom.Statement wireAndGetStatement( org.jboss.forge.roaster.model.statements.Statement<O,B,?> statement, B parent, AST ast ) {
        ASTNode<? extends Statement> node = (ASTNode<? extends Statement>) statement;
        statement.setOrigin( parent );
        node.setAst( ast );
        Statement stat = node.materialize( ast );

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
