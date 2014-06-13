package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.Expressions;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.DeclareStatement;

public class DeclareStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T,VariableDeclarationStatement,DeclareStatement<O,T>>
        implements DeclareStatement<O,T> {

    private VariableDeclarationStatement var;

    private Expression initializer;
    private String name;
    private String type;

    public DeclareStatementImpl() { }

    @Override
    public DeclareStatement<O, T> setVariable( Class klass, String name ) {
        return setVariable( klass.getName(), name );
    }

    public DeclareStatement<O, T> setVariable( String type, String name ) {
        this.name = name;
        this.type = type;
        return this;
    }

    @Override
    public DeclareStatement<O, T> setInitExpression( Expression init ) {
        this.initializer = init;
        return this;
    }

    @Override
    public DeclareStatement<O, T> setDefaultInitExpression() {
        setInitExpression( Expressions.zeroLiteral( type ) );
        return this;
    }

    public VariableDeclarationFragment getMainVar() {
        return (VariableDeclarationFragment) var.fragments().iterator().next();
    }

    @Override
    public VariableDeclarationStatement getInternal() {
        return var;
    }

    @Override
    public void materialize( AST ast ) {
        var = ast.newVariableDeclarationStatement( ast.newVariableDeclarationFragment() );

        var.setType( JDTHelper.getType( type, ast ) );
        getMainVar().setName( ast.newSimpleName( name ) );

        if ( initializer != null ) {
            getMainVar().setInitializer( wireAndGetExpression( initializer, this, ast ) );
        }
    }
}
