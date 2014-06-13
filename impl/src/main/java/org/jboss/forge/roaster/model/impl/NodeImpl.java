/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.expressions.JdtExpressionWrapper;
import org.jboss.forge.roaster.model.impl.statements.JdtStatementWrapper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.StatementSource;
import org.jboss.forge.roaster.model.statements.Statements;

public abstract class NodeImpl<O extends JavaSource<O>,
                               T,
                               J extends org.eclipse.jdt.core.dom.ASTNode>
       implements Origin<T>,
                  ASTNode<J> {

    protected T origin;
    protected AST ast;
    protected String label;

    @Override
    public T getOrigin() {
        return origin;
    }

    public void setOrigin( T origin ) {
        this.origin = origin;
    }

    public AST getAst() {
        return ast;
    }

    public void setAst( AST ast ) {
        this.ast = ast;
    }

    public String getLabel() {
        return label;
    }

    public boolean hasLabel() {
        return label != null && label.length() > 0;
    }

    @Override
    public void setInternal(J jdtNode) {
        setAst(jdtNode.getAST());
    }

    @Override
    public String toString() {
        return getInternal() != null ? getInternal().toString() : "";
    }

    @Override
    public boolean isMaterialized() {
        return getInternal() != null;
    }

}
