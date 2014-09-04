package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.TryCatchStatement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class TryCatchStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T, TryStatement,TryCatchStatement<O,T>>
        implements TryCatchStatement<O,T> {

    private TryStatement tryCatch;

    private Map<DeclareExpression,Statement> catches = Collections.EMPTY_MAP;
    private Statement body;
    private Statement fine;

    public TryCatchStatementImpl() { }


    @Override
    public TryStatement getInternal() {
        return tryCatch;
    }

    @Override
    public void materialize( AST ast ) {
        tryCatch = ast.newTryStatement();

        for ( DeclareExpression declare : catches.keySet() ) {
            CatchClause clause = ast.newCatchClause();
            clause.setException( varToSvd( (VariableDeclarationExpression) wireAndGetExpression( declare, this, ast ), ast ) );
            clause.setBody( (org.eclipse.jdt.core.dom.Block) wireAndGetStatement( catches.get( declare ).wrap(), this, ast ) );
            tryCatch.catchClauses().add( clause );
        }

        if ( fine != null ) {
            tryCatch.setFinally( (org.eclipse.jdt.core.dom.Block) wireAndGetStatement( fine.wrap(), this, ast ) );
        }
        if ( body != null ) {
            tryCatch.setBody( (org.eclipse.jdt.core.dom.Block) wireAndGetStatement( body.wrap(), this, ast ) );
        }
    }

    private SingleVariableDeclaration varToSvd( VariableDeclarationExpression var, AST ast ) {
        SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
        VariableDeclarationFragment frag = (VariableDeclarationFragment) var.fragments().get( 0 );
        svd.setName( ast.newSimpleName( frag.getName().getIdentifier() ) );
        svd.setType( JDTHelper.getType( var.getType().toString(), ast ) );
        return svd;
    }

    @Override
    public TryCatchStatement<O, T> addCatch( DeclareExpression declaration, Statement block ) {
        if ( catches.isEmpty() ) {
            catches = new LinkedHashMap<DeclareExpression, Statement>();
        }
        catches.put( declaration, block );
        return this;
    }

    @Override
    public TryCatchStatement<O, T> setFinally( Statement block ) {
        this.fine = block;
        return this;
    }

    @Override
    public TryCatchStatement<O, T> setBody( Statement block ) {
        this.body = block;
        return this;
    }
}
