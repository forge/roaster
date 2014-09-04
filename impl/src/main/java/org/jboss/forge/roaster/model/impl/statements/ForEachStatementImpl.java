package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ForEachStatement;
import org.jboss.forge.roaster.model.statements.Statement;

public class ForEachStatementImpl<O extends JavaSource<O>, T extends Block<O,? extends BlockHolder<O,?>>>
        extends StatementImpl<O,T,EnhancedForStatement,ForEachStatement<O,T>>
        implements ForEachStatement<O,T> {

    private EnhancedForStatement iter;

    private String name;
    private String type;
    private Expression source;
    private Statement body;

    public ForEachStatementImpl() { }

    @Override
    public ForEachStatement<O, T> setIterator( String klass, String name ) {
        this.name = name;
        this.type = klass;
        return this;
    }

    @Override
    public ForEachStatement<O, T> setIterator( Class klass, String name ) {
        return setIterator( klass.getName(), name );
    }

    @Override
    public ForEachStatement<O, T> setSource( Expression expr ) {
        this.source = expr;
        return this;
    }

    @Override
    public ForEachStatement<O, T> setBody( Statement statement ) {
        this.body = statement;
        return this;
    }

    @Override
    public EnhancedForStatement getInternal() {
        return iter;
    }

    @Override
    public void materialize( AST ast ) {
        iter = ast.newEnhancedForStatement();

        iter.getParameter().setName( iter.getAST().newSimpleName( name ) );
        iter.getParameter().setType( JDTHelper.getType( type, iter.getAST() ) );

        if ( source != null ) {
            iter.setExpression( wireAndGetExpression( source, this, ast ) );
        }
        if ( body != null ) {
            iter.setBody( wireAndGetStatement( body.wrap(), this, ast ) );
        }
    }
}
