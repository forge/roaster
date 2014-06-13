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
 * Abstract factory interface supporting the construction of constructor expressions, in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ConstructorBuilder<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
{

   /**
    * Creates a <code>ConstructorExpression</code> for the given class name
    * @param klass the name of the class to instantiate
    * @return  A <code>ConstructorExpression</code> for class <code>klass</code>
    */
   public ConstructorExpression<O,P> construct(String klass);

   /**
    * Creates a <code>ConstructorExpression</code> for the given class
    * @param klass the class to instantiate
    * @return  A <code>ConstructorExpression</code> for class <code>klass</code>
    */
   public ConstructorExpression<O,P> construct(Class<?> klass);

   /**
    * Creates an Array Creation expression
    * @param klass The type of the array to be created
    * @return  A <code>ArrayConstructorExpression</code> of the given type
    */
   public ArrayConstructorExpression<O,P> newArray(Class<?> klass);

   /**
    * Creates an Array Creation expression
    * @param klass The name of the type of the array to be created
    * @return  A <code>ArrayConstructorExpression</code> of the given type
    */
   public ArrayConstructorExpression<O,P> newArray(String klass);

}
