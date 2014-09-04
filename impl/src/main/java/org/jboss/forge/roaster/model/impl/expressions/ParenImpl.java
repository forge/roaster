package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.ParenExpression;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ParenImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T,ParenthesizedExpression>
        implements ParenExpression<O,T> {

    ParenthesizedExpression paren;

    private Expression inner;

    public ParenImpl( Expression child ) {
        inner = child;
    }

    @Override
    public ParenthesizedExpression getInternal() {
        return paren;
    }

    @Override
    public void materialize( AST ast ) {
        paren = ast.newParenthesizedExpression();

        if ( inner != null ) {
            paren.setExpression( wireAndGetExpression( inner, this, ast ) );
        }
    }
}
