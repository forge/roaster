package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.jboss.forge.roaster.model.expressions.CastExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class CastImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T, org.eclipse.jdt.core.dom.CastExpression>
        implements CastExpression<O,T> {

    private org.eclipse.jdt.core.dom.CastExpression cast;

    private String type;
    private Expression<O,CastExpression<O,T>> expression;

    public CastImpl( String klass, Expression<O,CastExpression<O,T>> expression ) {
        this.type = klass;
        this.expression = expression;
    }


    @Override
    public org.eclipse.jdt.core.dom.CastExpression getInternal() {
        return cast;
    }

    @Override
    public void materialize( AST ast ) {
        cast = ast.newCastExpression();
        cast.setType( JDTHelper.getType( type, ast ) );
        if ( expression != null ) {
            org.eclipse.jdt.core.dom.Expression expr = wireAndGetExpression( expression, this, ast );
            ParenthesizedExpression paren = ast.newParenthesizedExpression();
            paren.setExpression( expr );
            cast.setExpression( paren );
        }
    }
}
