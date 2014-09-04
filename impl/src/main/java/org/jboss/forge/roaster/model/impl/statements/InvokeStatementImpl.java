package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.InvokeStatement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvokeStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends    StatementImpl<O,T,ExpressionStatement,InvokeStatement<O,T>>
        implements InvokeStatement<O,T> {

    private ExpressionStatement statement;
    private MethodInvocation invoke;

    private String method;
    private Accessor target;
    private List<Argument> argumentList = Collections.EMPTY_LIST;

    public InvokeStatementImpl() { }

    @Override
    public InvokeStatement<O, T> setMethod( String method ) {
        this.method = method;
        return this;
    }

    @Override
    public InvokeStatement<O, T> setTarget( Accessor target ) {
        this.target = target;
        return this;
    }

    @Override
    public InvokeStatement<O, T> addArgument( Argument argument ) {
        if ( argumentList.isEmpty() ) {
            argumentList = new LinkedList<Argument>();
        }
        argumentList.add( argument );
        return this;
    }

    @Override
    public ExpressionStatement getInternal() {
        return statement;
    }

    @Override
    public void materialize( AST ast ) {
        invoke = ast.newMethodInvocation();
        statement = ast.newExpressionStatement( invoke );

        invoke.setName( statement.getAST().newSimpleName( method ) );

        if ( target != null ) {
            invoke.setExpression( wireAndGetExpression( target, this, ast ) );
        }

        for ( Argument argument : argumentList ) {
            invoke.arguments().add( wireAndGetExpression( argument, this, ast ) );
        }
    }
}
