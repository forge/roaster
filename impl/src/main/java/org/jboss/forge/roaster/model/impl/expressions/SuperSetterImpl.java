package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class SuperSetterImpl<O extends JavaSource<O>, T extends ExpressionSource<O>>
        extends AbstractMethodInvokeImpl<O,T,SuperMethodInvocation>
        implements Setter<O,T> {

    private SuperMethodInvocation setter;

    private Expression<O,Setter<O,T>> value;

    public SuperSetterImpl( String fieldName, String type, Expression<O, Setter<O, T>> value ) {
        super( JDTHelper.setter( fieldName, type ) );
        this.value = value;
    }

    @Override
    public SuperMethodInvocation getInternal() {
        return setter;
    }

    @Override
    public void materialize( AST ast ) {
        setter = ast.newSuperMethodInvocation();
        setter.setName( ast.newSimpleName( method ) );
        if ( value != null ) {
            setter.arguments().add( wireAndGetExpression( value, this, ast ) );
        }
    }

}
