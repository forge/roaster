package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.Statement;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface JdtStatementWrapper<O extends JavaSource, T, J extends Statement> extends ASTNode<J> {

    public void setOrigin( T origin );
}
