package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.SuperStatement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SuperStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends    StatementImpl<O,T,SuperConstructorInvocation,SuperStatement<O,T>>
        implements SuperStatement<O,T> {

    private SuperConstructorInvocation invoke;

    private List<Argument> argumentList = Collections.EMPTY_LIST;

    public SuperStatementImpl() { }

    @Override
    public SuperStatement<O, T> addArgument( Argument argument ) {
        if ( argumentList.isEmpty() ) {
            argumentList = new LinkedList<Argument>();
        }
        argumentList.add( argument );
        return this;
    }

    @Override
    public SuperConstructorInvocation getInternal() {
        return invoke;
    }

    @Override
    public void materialize( AST ast ) {
        invoke = ast.newSuperConstructorInvocation();

        for ( Argument argument : argumentList ) {
            invoke.arguments().add( wireAndGetExpression( argument, this, ast ) );
        }
    }
}
