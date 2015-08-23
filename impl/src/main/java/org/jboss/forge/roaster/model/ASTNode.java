package org.jboss.forge.roaster.model;


import org.eclipse.jdt.core.dom.AST;

public interface ASTNode<J extends org.eclipse.jdt.core.dom.ASTNode> {

    AST getAst();

    J materialize( AST ast );

    J getInternal();

    void setInternal(J jdtNode);

    boolean isMaterialized();

    void setAst( AST ast );
}
