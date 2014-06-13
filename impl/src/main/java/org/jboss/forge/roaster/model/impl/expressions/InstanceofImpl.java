package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InstanceofExpression;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class InstanceofImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends ArgumentImpl<O,T, org.eclipse.jdt.core.dom.InstanceofExpression>
        implements InstanceofExpression<O,T> {

    private org.eclipse.jdt.core.dom.InstanceofExpression isA;

    private String type;
    private Expression<O,InstanceofExpression<O,T>> expression;

    public InstanceofImpl( String klass, Expression<O, InstanceofExpression<O, T>> expression ) {
        this.type = klass;
        this.expression = expression;
    }

    @Override
    public org.eclipse.jdt.core.dom.InstanceofExpression getInternal() {
        return isA;
    }

    @Override
    public void materialize( AST ast ) {
        isA = ast.newInstanceofExpression();
        isA.setRightOperand( JDTHelper.getType( type, ast ) );
        if ( expression != null ) {
            org.eclipse.jdt.core.dom.Expression expr = wireAndGetExpression( expression, this, ast );
            isA.setLeftOperand( expr );
        }
    }
}
