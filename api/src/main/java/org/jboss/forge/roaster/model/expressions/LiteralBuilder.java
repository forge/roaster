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
 * Abstract factory interface that supports the creation of literal expressions in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface LiteralBuilder<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
{

   /**
    * Creates a String literal
    * @param val  The string value
    * @return  a new <code>StringLiteral</code>
    */
   public StringLiteral<O,P> literal(String val);

   /**
    * Creates a Number Literal
    * @param val  The Number value
    * @return  a new <code>NumberLiteral</code>
    */
   public NumberLiteral<O,P> literal(Number val);

   /**
    * Creates a CharacterLiteral
    * @param val  The Character value
    * @return  a new <code>CharacterLiteral</code>
    */
   public CharacterLiteral<O,P> literal(Character val);

   /**
    * Creates a BooleanLiteral
    * @param val  The Boolean value
    * @return  a new <code>BooleanLiteral</code>
    */
   public BooleanLiteral<O,P> literal(Boolean val);

   /**
    * Creates a literal based on the <code>klass</code> name passed as an argument.
    * Boolean returns false, Numbers return the appropriate representation for 0,
    * Objects return null
    * @param klass   The name of the type of literal to create
    * @return  a default <code>Literal</code> for the given type
    */
   public Literal<O,P,?> zeroLiteral(String klass);

   /**
    * Creates a literal based on the <code>klass</code> name passed as an argument.
    * Boolean returns false, Numbers return the appropriate representation for 0,
    * Objects return null
    * @param klass   The type of literal to create
    * @return  a default <code>Literal</code> for the given type
    */
   public Literal<O,P,?> zeroLiteral(Class<?> klass);

   /**
    * Creates a literal expression pointing to <code>this</code> object
    * @return  a new <code>ThisLiteral</code>
    */
   public ThisLiteral<O,P> thisLiteral();

   /**
    * Creates a null literal expression
    * @return  a new <code>NullLiteral</code>
    */
   public NullLiteral<O,P> nullLiteral();

   /**
    * Creates a classs literal expression
    * @param klass   The name of the class
    * @return  a new <code>ClassLiteral</code>
    */
   public ClassLiteral<O,P> classLiteral(String klass);

   /**
    * Creates a classs literal expression
    * @param klass   The class
    * @return  a new <code>ClassLiteral</code>
    */
   public ClassLiteral<O,P> classLiteral(Class<?> klass);

}
