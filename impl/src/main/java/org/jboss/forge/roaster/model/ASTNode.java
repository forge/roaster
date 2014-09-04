package org.jboss.forge.roaster.model;


import org.eclipse.jdt.core.dom.AST;

public interface ASTNode<J extends org.eclipse.jdt.core.dom.ASTNode> {

    public AST getAst();

    public J getInternal();

    public void materialize( AST ast );

    void setAst( AST ast );
}
