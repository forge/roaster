package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Wireable;
import org.jboss.forge.roaster.model.impl.expressions.DotAccessorImpl;
import org.jboss.forge.roaster.model.impl.expressions.JdtExpressionWrapper;
import org.jboss.forge.roaster.model.impl.statements.JdtStatementWrapper;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;

public abstract class NodeImpl<O extends JavaSource<O>,T,J extends org.eclipse.jdt.core.dom.ASTNode>
       implements Origin<T>,
                  ASTNode<J> {

    private T origin;
    private AST ast;

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

    public <X extends org.eclipse.jdt.core.dom.Expression, P> X wireAndGetExpression( ExpressionSource expression, P parent, AST ast ) {
        JdtExpressionWrapper<O,P,X> node = (JdtExpressionWrapper<O,P,X>) expression;
        node.setOrigin( parent );
        node.setAst( ast );
        node.materialize( ast );
        return node.getInternal();
    }

    public <K extends org.eclipse.jdt.core.dom.Statement, P> org.eclipse.jdt.core.dom.Statement wireAndGetStatement( Statement statement, P parent, AST ast ) {
        JdtStatementWrapper<O,P,K> node = (JdtStatementWrapper<O,P,K>) statement;
        node.setOrigin( parent );
        node.setAst( ast );
        node.materialize( ast );
        return node.getInternal();
    }

}
