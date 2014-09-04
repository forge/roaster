package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.source.JavaSource;

public class FieldImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends SimpleAccessorImpl<O,T,FieldAccess>
        implements Field<O,T> {

    private FieldAccess fld;

    private String fieldName;
    private Expression<O,T> expression;

    public FieldImpl( String fieldName ) {
        this.fieldName = fieldName;
    }

    @Override
    public FieldAccess getInternal() {
        return fld;
    }

    @Override
    public void materialize( AST ast ) {
        fld = ast.newFieldAccess();
        fld.setName( ast.newSimpleName( fieldName ) );
        if ( expression == null ) {
            fld.setExpression( ast.newThisExpression() );
        } else {
            fld.setExpression( wireAndGetExpression( expression, this, ast ) );
        }
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        this.expression = child;
    }
}
