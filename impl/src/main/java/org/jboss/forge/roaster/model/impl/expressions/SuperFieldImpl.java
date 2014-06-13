package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperFieldImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AccessorImpl<O,T,SuperFieldAccess>
        implements Field<O,T> {

    private SuperFieldAccess fld;

    private String fieldName;

    public SuperFieldImpl( String fieldName ) {
        this.fieldName = fieldName;
    }

    @Override
    public SuperFieldAccess getInternal() {
        return fld;
    }

    @Override
    public void materialize( AST ast ) {
        fld = ast.newSuperFieldAccess();
        fld.setName( ast.newSimpleName( fieldName ) );
    }

    @Override
    public Argument<O,T> inc() {
        return new PostFixImpl<O,T>( PostfixExpression.Operator.INCREMENT.toString(), this );
    }

    @Override
    public Argument<O,T> dec() {
        return new PostFixImpl<O,T>( PostfixExpression.Operator.DECREMENT.toString(), this );
    }

    @Override
    public void wireExpression( Expression<O, T> child ) {
        throw new UnsupportedOperationException( "Unable to set an access path other than 'super' on a SuperField " );
    }
}
