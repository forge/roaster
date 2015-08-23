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
 * Abstract factory interface that supports the construction of basic, simple expressions, in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface BasicExpressionFactory<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends LiteralBuilder<O,P>,
      ConstructorBuilder<O,P>
{

   /**
    * Creates a new variable dereferencing expression
    * @param varName the name of the variable
    * @return a new <code>Variable</code>
    */
   public Variable<O,P> var(String varName);

   /**
    * Creates a new infix operator expression with the given op
    * @param op   the infix operator
    * @return  a new <code>OperatorExpression</code> with no operands
    */
   public OperatorExpression<O,P> operator(Op op);

   /**
    * Creates a new prefix operator expression with the given op
    * @param operator  the prefix operator
    * @return  a new <code>UnaryExpression</code> with no operand
    */
   public UnaryExpression<O,P> operator(PrefixOp operator);

   /**
    * Creates a new ternary expression
    * @return a new, empty <code>TernaryExpression</code>
    */
   public TernaryExpression<O,P> ternary();

   /**
    * Creates a cast expression
    * @param klass   the name of the type to cast to
    * @param arg     the expression being cast
    * @return  a new <code>CastExpression</code>
    */
   public CastExpression<O,P> cast(String klass, ExpressionSource<O,?,?> arg);

   /**
    * Creates a cast expression
    * @param klass   the type to cast to
    * @param arg     the expression being cast
    * @return  a new <code>CastExpression</code>
    */
   public CastExpression<O,P> cast(Class klass, ExpressionSource<O,?,?> arg);

   /**
    * Creates a new parenthesized expression wrapping the given expression
    * @param inner   the inner expression
    * @return  a new <code>ParenExpression</code>
    */
   public ParenExpression<O,P> paren(ExpressionSource<O,?,?> inner);

   /**
    * Creates a specialized unary expression, using the operator not (!)
    * @param arg  the operand expression
    * @return  a new <code>UnaryExpression</code>
    */
   public UnaryExpression<O,P> not(ExpressionSource<O,?,?> arg);

   /**
    * Creates a new assignment expression with the given operator
    * @param operator   the assignment operator
    * @return  a new, empty <code>AssignExpression</code>
    */
   public AssignExpression<O,P> assign(Assignment operator);

   /**
    * Creates a new variable declaration expression
    * @param klass   the name of the type of the variable
    * @param name    the name of the variable
    * @return  a new, uninitialized <code>DeclareExpression</code>
    */
   public DeclareExpression<O,P> declare(String klass, String name);

   /**
    * Creates a new variable declaration expression
    * @param klass   the type of the variable
    * @param name    the name of the variable
    * @return  a new, uninitialized <code>DeclareExpression</code>
    */
   public DeclareExpression<O,P> declare(Class klass, String name);

   /**
    * Creates a new variable declaration expression, initializing it
    * @param klass   the name of the type of the variable
    * @param name    the name of the variable
    * @param init    the initialization expression
    * @return  a new  <code>DeclareExpression</code>
    */
   public DeclareExpression<O,P> declare(String klass, String name, ExpressionSource<O,?,?> init);

   /**
    * Creates a new variable declaration expression, initializing it
    * @param klass   the type of the variable
    * @param name    the name of the variable
    * @param init    the initialization expression
    * @return  a new  <code>DeclareExpression</code>
    */
   public DeclareExpression<O,P> declare(Class klass, String name, ExpressionSource<O,?,?> init);

   /**
    * Creates a new accessor giving access to the static members of a class
    * @param klass   the name of the class
    * @return  a new <code>Accessor</code> targeting the given class
    */
   public Accessor<O,P,?> classStatic(String klass);

   /**
    * Creates a new accessor giving access to the static members of a class
    * @param klass   the class
    * @return  a new <code>Accessor</code> targeting the given class
    */
   public Accessor<O,P,?> classStatic(Class klass);

   /**
    * Creates a new instanceof expression
    * @param klass   the name of the type to check for membership
    * @param arg     the expression returning the value to check for class membership
    * @return  a new <code>InstanceofExpression</code>
    */
   public InstanceofExpression<O,P> instanceOf(String klass, ExpressionSource<O,?,?> arg);

   /**
    * Creates a new instanceof expression
    * @param klass   the type to check for membership
    * @param arg     the expression returning the value to check for class membership
    * @return  a new <code>InstanceofExpression</code>
    */
   public InstanceofExpression<O,P> instanceOf(Class klass, ExpressionSource<O,?,?> arg);

   /**
    * Creates a new super accessor expression
    * @return a new <code>Super</code> accessor
    */
   public Super<O,P> sup();

   /**
    * Creates a new array initialization expression
    * @return a new <code>ArrayInit</code>
    */
   public ArrayInit<O,ArrayConstructorExpression<O,P>> vec();
}
