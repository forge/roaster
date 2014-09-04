package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.expressions.Super;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactoryImpl;

public class SuperImpl<O extends JavaSource<O>,T extends ExpressionSource<O>>
        extends ExpressionFactoryImpl<O,T,org.eclipse.jdt.core.dom.Expression>
        implements Super<O,T> {

    @Override
    public Field<O,T> field( String fieldName ) {
        return new SuperFieldImpl<O,T>( fieldName );

    }

    @Override
    public Getter getter( String field, String klass ) {
        return new SuperGetterImpl<O,T>( field, klass );
    }

    @Override
    public Getter getter( String field, Class klass ) {
        return getter( field, klass.getName() );
    }

    @Override
    public MethodExpression invoke( String method ) {
        return new SuperMethodInvokeImpl<O,T>( method );
    }

    @Override
    public Setter setter( String fldName, Class type, Expression value ) {
        return setter( fldName, type.getName(), value );
    }

    @Override
    public Setter setter( String fldName, String type, Expression value ) {
        return new SuperSetterImpl<O,T>( fldName, type, value );
    }
}
