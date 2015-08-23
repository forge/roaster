/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.spi;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.NonPrimitiveExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.impl.expressions.ArrayAccessImpl;
import org.jboss.forge.roaster.model.impl.expressions.BasicExpressionFactoryImpl;
import org.jboss.forge.roaster.model.impl.expressions.FieldImpl;
import org.jboss.forge.roaster.model.impl.expressions.GetterImpl;
import org.jboss.forge.roaster.model.impl.expressions.MethodInvokeImpl;
import org.jboss.forge.roaster.model.impl.expressions.SetterImpl;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ExpressionFactoryImpl<O extends JavaSource<O>,
                                   P extends ExpressionHolder<O>,
                                   E extends NonPrimitiveExpression<O,P,E>,
                                   J extends org.eclipse.jdt.core.dom.Expression>
        extends BasicExpressionFactoryImpl<O,P,J>
        implements ExpressionFactory<O,P,E> {


    public ExpressionFactoryImpl() { }

    @Override
    public Field<O,E> field( String fieldName ) {
        return new FieldImpl<O,E>( fieldName );
    }

    @Override
    public Getter<O,E> getter( String fieldName, String type ) {
        return new GetterImpl<O,E>( fieldName, type );
    }

    @Override
    public Getter<O,E> getter( String fieldName, Class type ) {
        return getter( fieldName, type.getName() );
    }

    @Override
    public Setter<O,E> setter( String fldName, String type, ExpressionSource<?,?,?> value ) {
        return new SetterImpl<O, E>( fldName, type, value );
    }

    @Override
    public Setter<O,E> setter( String fldName, Class type, ExpressionSource<?,?,?> value ) {
        return setter( fldName, type.getName(), value );
    }

    @Override
    public MethodCallExpression<O,E> invoke( String method ) {
        return new MethodInvokeImpl<O,E>( method );
    }


    @Override
    public ArrayIndexer<O,E> itemAt( ExpressionSource<?,?,?> index ) {
        return new ArrayAccessImpl<O,E>( index );
    }
}
