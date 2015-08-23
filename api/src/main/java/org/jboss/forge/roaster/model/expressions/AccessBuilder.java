/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Abstract factory interface that supports the construction of accessor expressions, in source format
 * The expression returns an object with accessible fields, accessors and methods
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface AccessBuilder<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends NonPrimitiveExpression<O,P,E>>
{

   /**
    * Returns a field access expression
    * Can be invoked on a parent expression
    * @param field   The name of the field
    * @return a field accessor
    */
   public Field<O,E> field(String field);

   /**
    * Returns a getter expression
    * Can be invoked on a parent expression
    * @param field   The name of the field
    * @param klass   The name of the type of the field
    * @return a getter method invocation expression
    */
   public Getter<O,E> getter(String field, String klass);

   /**
    * Returns a getter expression
    * Can be invoked on a parent expression
    * @param field   The name of the field
    * @param klass   The type of the field
    * @return a getter method invocation expression
    */
   public Getter<O,E> getter(String field, Class klass);

   /**
    * Returns a setter expression
    * Can be invoked on a parent expression
    * @param field   The name of the field
    * @param klass   The name of the type of the field
    * @param value   The expression returning the value to be set
    * @return a setter method invocation expression
    */
   public Setter<O,E> setter(String field, String klass, ExpressionSource<?,?,?> value);

   /**
    * Returns a setter expression
    * Can be invoked on a parent expression
    * @param field   The name of the field
    * @param klass   The type of the field
    * @param value   The expression returning the value to be set
    * @return a setter method invocation expression
    */
   public Setter<O,E> setter(String field, Class klass, ExpressionSource<?,?,?> value);

   /**
    * Returns a method invocation expression
    * Can be invoked on a parent expression
    * @param method  The name of the method
    * @return a method invocation expression
    */
   public MethodCallExpression<O,E> invoke(String method);

   /**
    * Returns an array indexing expression
    * Can be invoked on a parent expression
    * @param index  The expression returning the index used for accessing the array
    * @return an array indexing expression
    */
   public ArrayIndexer<O,E> itemAt(ExpressionSource<?,?,?> index);

}
