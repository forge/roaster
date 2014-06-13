package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class DeclareExpressionImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,VariableDeclarationExpression>
        implements DeclareExpression<O,T> {

    private VariableDeclarationExpression var;

    private String type;
    private String name;
    private Expression init;

    public DeclareExpressionImpl( String type, String name ) {
        this.type = type;
        this.name = name;
    }

    @Override
    public DeclareExpression<O, T> init( Expression expr ) {
        this.init = expr;
        return this;
    }

    @Override
    public VariableDeclarationExpression getInternal() {
        return var;
    }

    @Override
    public void materialize( AST ast ) {
        VariableDeclarationFragment frag = ast.newVariableDeclarationFragment();
        frag.setName( ast.newSimpleName( name ) );
        var = ast.newVariableDeclarationExpression( frag );
        var.setType( JDTHelper.getType( type, ast ) );

        if ( init != null ) {
            frag.setInitializer( wireAndGetExpression( init, this, ast ) );
        }
    }
}
