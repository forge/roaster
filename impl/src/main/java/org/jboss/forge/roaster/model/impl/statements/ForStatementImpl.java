package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ForStatement;
import org.jboss.forge.roaster.model.statements.Statement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ForStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, org.eclipse.jdt.core.dom.ForStatement,ForStatement<O,T>>
        implements ForStatement<O,T> {

    private org.eclipse.jdt.core.dom.ForStatement iter;

    private List<DeclareExpression> declarations = Collections.EMPTY_LIST;
    private List<Expression> updaters = Collections.EMPTY_LIST;
    private Expression condition;
    private Statement body;

    public ForStatementImpl() { }

    @Override
    public ForStatement<O, T> addDeclaration( DeclareExpression declaration ) {
        if ( declarations.isEmpty() ) {
            declarations = new LinkedList<DeclareExpression>();
        }
        declarations.add( declaration );
        return this;
    }

    @Override
    public ForStatement<O, T> addUpdate( Expression expression ) {
        if ( updaters.isEmpty() ) {
            updaters = new LinkedList<Expression>();
        }
        updaters.add( expression );
        return this;
    }

    @Override
    public ForStatement<O, T> setCondition( Expression booleanExpression ) {
        this.condition = booleanExpression;
        return this;
    }

    @Override
    public ForStatement<O, T> setBody( Statement statement ) {
        this.body = statement;
        return this;
    }

    @Override
    public org.eclipse.jdt.core.dom.ForStatement getInternal() {
        return iter;
    }

    @Override
    public void materialize( AST ast ) {
        iter = ast.newForStatement();

        for ( DeclareExpression declaration : declarations ) {
            VariableDeclarationExpression declare = (VariableDeclarationExpression) wireAndGetExpression( declaration, this, ast );
            if ( iter.initializers().isEmpty() ) {
                iter.initializers().add( declare );
            } else {
                VariableDeclarationExpression existing = (VariableDeclarationExpression) iter.initializers().get( 0 );
                VariableDeclarationFragment frag = (VariableDeclarationFragment) declare.fragments().get( 0 );
                frag.delete();
                existing.fragments().add( frag );
            }
        }

        if ( condition != null ) {
            iter.setExpression( wireAndGetExpression( condition, this, ast ) );
        }

        for ( Expression updater : updaters ) {
            iter.updaters().add( wireAndGetExpression( updater, this, ast ) );
        }

        if ( body != null ) {
            iter.setBody( wireAndGetStatement( body.wrap(), this, ast ) );
        }
    }
}
