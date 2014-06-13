package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SetterImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends MethodInvokeImpl<O,T>
        implements Setter<O,T> {

    private MethodInvocation setter;

    private Expression<O,Setter<O,T>> value;

    public SetterImpl( String fieldName, String type, Expression<O,Setter<O,T>> value ) {
        super( JDTHelper.setter( fieldName, type ) );
        this.value = value;
    }

    @Override
    public MethodInvocation getInternal() {
        return setter;
    }

    @Override
    public void materialize( AST ast ) {
        super.materialize( ast );
        if ( value != null ) {
            setter.arguments().add( wireAndGetExpression( value, this, ast ) );
        }
    }

}
