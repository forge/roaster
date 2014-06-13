package org.jboss.forge.roaster.model.impl.expressions;


import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface JdtExpressionWrapper<O extends JavaSource<O>,T,J extends Expression>
    extends ASTNode<J>,
            Origin<T> {


    public void setOrigin( T parent );

}
