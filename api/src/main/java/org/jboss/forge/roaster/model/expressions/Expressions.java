/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactory;

import java.util.ServiceLoader;

public abstract class Expressions
{

   protected static ExpressionFactory factory;

   @SuppressWarnings("unchecked")
   protected static <O extends JavaSource<O>> ExpressionFactory<O,?,?> getFactory()
   {
      synchronized (Expressions.class)
      {
         ServiceLoader<ExpressionFactory> sl = ServiceLoader.load(ExpressionFactory.class, Expressions.class.getClassLoader());
         if (sl.iterator().hasNext())
         {
            factory = sl.iterator().next();
         } else
         {
            throw new IllegalStateException("No ExpressionFactory implementation available, unable to continue");
         }
      }
      return factory;
   }

   /**
    * Creates a String literal expression
    * @param val  The String value
    * @param <O> the Java Source type
    * @return A literal expression that evaluates to the given String
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> StringLiteral<O,?> literal(String val)
   {
      return (StringLiteral<O,?>) getFactory().literal(val);
   }

   /**
    * Creates a Number literal expression
    * @param val  The Number value
    * @param <O> the Java Source type
    * @return A literal expression that evaluates to the given Number
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> NumberLiteral<O,?> literal(Number val)
   {
      return (NumberLiteral<O,?>) getFactory().literal(val);
   }

   /**
    * Creates a Char literal expression
    * @param val  The Char value
    * @param <O> the Java Source type
    * @return A literal expression that evaluates to the given Char
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> CharacterLiteral<O,?> literal(Character val)
   {
      return (CharacterLiteral<O,?>) getFactory().literal(val);
   }

   /**
    * Creates a Boolean literal expression
    * @param val  The Boolean value
    * @param <O> the Java Source type
    * @return A literal expression that evaluates to the given Boolean
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> BooleanLiteral<O,?> literal(Boolean val)
   {
      return (BooleanLiteral<O,?>) getFactory().literal(val);
   }

   /**
    * Creates a default literal expression of the given type:
    * The default for booleans is false; numbers are mapped to the appropriate literal "0";
    * Passing any Object class (including String) returns a null literal expression
    * @param klass   The name of the type for which a default expression is required
    * @param <O> the Java Source type
    * @return A literal expression that evaluates to "0"/null, depending on the <code>klass</code>
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Literal<O,?,?> zeroLiteral(String klass)
   {
      return (Literal<O,?,?>) getFactory().zeroLiteral(klass);
   }

   /**
    * Creates a default literal expression of the given type:
    * The default for booleans is false; numbers are mapped to the appropriate literal "0";
    * Passing any Object class (including String) returns a null literal expression
    * @param klass   The type for which a default expression is required
    * @param <O> the Java Source type
    * @return A literal expression that evaluates to "0"/null, depending on the <code>klass</code>
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Literal<O,?,?> zeroLiteral(Class<?> klass)
   {
      return (Literal<O,?,?>) getFactory().zeroLiteral(klass);
   }

   /**
    * Creates a literal expression that evaluates to "this"
    * @param <O> the Java Source type
    * @return A literal expression representing "this" object
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ThisLiteral<O,?> thisLiteral()
   {
      return (ThisLiteral<O,?>) getFactory().thisLiteral();
   }

   /**
    * Creates a literal expression that evaluates to null
    * @param <O> the Java Source type
    * @return A null literal expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> NullLiteral<O,?> nullLiteral()
   {
      return (NullLiteral<O,?>) getFactory().nullLiteral();
   }

   /**
    * Creates a literal expression that evaluates to the class of the given name
    * @param klass   The name of the class
    * @param <O> the Java Source type
    * @return A class literal expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ClassLiteral<O,?> classLiteral(String klass)
   {
      return (ClassLiteral<O,?>) getFactory().classLiteral(klass);
   }

   /**
    * Creates a literal expression that evaluates to the given class
    * @param klass   The name of the class
    * @param <O> the Java Source type
    * @return A class literal expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ClassLiteral<O,?> classLiteral(Class<?> klass)
   {
      return (ClassLiteral<O,?>) getFactory().classLiteral(klass);
   }

   /**
    * Creates an accessor expression off the class with the given name, to invoke static methods and properties
    * @param klass   The name of the class
    * @param <O> the Java Source type
    * @return A static class accessor expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Accessor<O,?,?> classStatic(String klass)
   {
      return (Accessor<O,?,?>) getFactory().classStatic(klass);
   }

   /**
    * Creates an accessor expression off the given class, to invoke static methods and properties
    * @param klass   The name of the class
    * @param <O> the Java Source type
    * @return A static class accessor expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Accessor<O,?,?> classStatic(Class<?> klass)
   {
      return (Accessor<O,?,?>) getFactory().classStatic(klass);
   }

   /**
    * Creates a constructor expression for the class of the given name
    * @param klass   The name of the class
    * @param <O> the Java Source type
    * @return A new, empty constructor expression for the given class
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ConstructorExpression<O,?> construct(String klass)
   {
      return (ConstructorExpression<O,?>) getFactory().construct(klass);
   }

   /**
    * Creates a constructor expression for the given class
    * @param klass   The name of the class
    * @param <O> the Java Source type
    * @return A new, emptyconstructor expression for the given class
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ConstructorExpression<O,?> construct(Class<?> klass)
   {
      return (ConstructorExpression<O,?>) getFactory().construct(klass);
   }

   /**
    * Creates a new array creation expression. The type of the array is the class of the given name
    * @param klass   The name of the array type
    * @param <O> the Java Source type
    * @return A new, empty array creation expression for the given class
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ArrayConstructorExpression<O,?> newArray(String klass)
   {
      return (ArrayConstructorExpression<O,?>) getFactory().newArray(klass);
   }

   /**
    * Creates a new array creation expression. The type of the array is the given class
    * @param klass   The name of the array type
    * @param <O> the Java Source type
    * @return A new, empty array creation expression for the given class
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ArrayConstructorExpression<O,?> newArray(Class<?> klass)
   {
      return (ArrayConstructorExpression<O,?>) getFactory().newArray(klass);
   }

   /**
    * Creates an array initializer expression, to be used with an array declaration
    * @param <O> the Java Source type
    * @return A new array initialization expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ArrayInit<O,?> vec()
   {
      return (ArrayInit<O,?>) getFactory().vec();
   }

   /**
    * Creates a variable reference expression
    * @param variable The name of the variable
    * @param <O> the Java Source type
    * @return A new expression that evaluates to the value of the variable with name <code>variable</code>
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Variable<O,?> var(String variable)
   {
      return (Variable<O,?>) getFactory().var(variable);
   }

   /**
    * Creates an infix operator expression, using the given operator <code>operator</code>
    * @param operator The operator for the expression
    * @param <O> the Java Source type
    * @return A new infix operator expression with no arguments
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> OperatorExpression<O,?> operator(Op operator)
   {
      return (OperatorExpression<O,?>) getFactory().operator(operator);
   }

   /**
    * Creates a unary, prefix operator expression, using the given operator <code>operator</code>
    * @param operator The prefix operator for the expression
    * @param <O> the Java Source type
    * @return A new unary prefix operator expression with no argument
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> UnaryExpression<O,?> operator(PrefixOp operator)
   {
      return (UnaryExpression<O,?>) getFactory().operator(operator);
   }

   /**
    * Creates a new ternary expression
    * @param <O> the Java Source type
    * @return A new, empty ternary expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> TernaryExpression<O,?> ternary()
   {
      return (TernaryExpression<O,?>) getFactory().ternary();
   }

   /**
    * Creates a cast expression, casting the given expression to the type of the given <code>klass</code> name
    * @param klass   The name of the class to cast the expression to
    * @param expression The expression to be cast
    * @param <O> the Java Source type
    * @return A new cast expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Argument<O,?,?> cast(String klass, ExpressionSource expression)
   {
      return (Argument<O,?,?>) getFactory().cast(klass, expression);
   }

   /**
    * Creates a cast expression, casting the given expression to the type of the given <code>klass</code>
    * @param klass   The class to cast the expression to
    * @param expression The expression to be cast
    * @param <O> the Java Source type
    * @return A new cast expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Argument<O,?,?> cast(Class klass, ExpressionSource expression)
   {
      return (Argument<O,?,?>) getFactory().cast(klass, expression);
   }

   /**
    * Creates a parenthesis expression wrapping the given <code>inner</code> expression
    * @param <O> the Java Source type
    * @return A new, parenthesis expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ParenExpression<O,?> paren(ExpressionSource inner)
   {
      return (ParenExpression<O,?>) getFactory().paren(inner);
   }

   /**
    * Creates a unary, boolean "not" expression that negates the given <code>inner</code> argument
    * @param inner The expression to be negated
    * @param <O> the Java Source type
    * @return A new "not" expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> UnaryExpression<O,?> not(ExpressionSource inner)
   {
      return (UnaryExpression<O,?>) getFactory().not(inner);
   }

   /**
    * Creates a new assignment expression using the given assignment operator
    * @param operator   The assignment operator
    * @param <O> the Java Source type
    * @return Creates a new assignment expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> AssignExpression<O,?> assign(Assignment operator)
   {
      return (AssignExpression<O,?>) getFactory().assign(operator);
   }

   /**
    * Creates a variable declaration expression, given a variable name and a class name
    * @param klass The name of the type of the declared variable
    * @param name  The name of the declared variable
    * @param <O> the Java Source type
    * @return  A new variable declaration expression.
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> DeclareExpression<O,?> declare(String klass, String name)
   {
      return (DeclareExpression<O,?>) getFactory().declare(klass, name);
   }

   /**
    * Creates a variable declaration expression, given a variable name and a class
    * @param klass The type of the declared variable
    * @param name  The name of the declared variable
    * @param <O> the Java Source type
    * @return  A new variable declaration expressions.
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> DeclareExpression<O,?> declare(Class klass, String name)
   {
      return (DeclareExpression<O,?>) getFactory().declare(klass, name);
   }

   /**
    * Creates a variable declaration expression, given a variable name, a class name and an initialization expression
    * @param klass The name of the type of the declared variable
    * @param name  The name of the declared variable
    * @param init  The expression providing the initial value of the variable
    * @param <O> the Java Source type
    * @return  A variable declaration expressions with an initializer.
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> DeclareExpression<O,?> declare(String klass, String name, ExpressionSource init)
   {
      return (DeclareExpression<O,?>) getFactory().declare(klass, name, init);
   }

   /**
    * Creates a variable declaration expression, given a variable name, a class and an initialization expression
    * @param klass The type of the declared variable
    * @param name  The name of the declared variable
    * @param init  The expression providing the initial value of the variable
    * @param <O> the Java Source type
    * @return  A variable declaration expressions with an initializer.
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> DeclareExpression<O,?> declare(Class klass, String name, ExpressionSource init)
   {
      return (DeclareExpression<O,?>) getFactory().declare(klass, name, init);
   }

   /**
    * Creates a new method invocation expression
    * @param method The name of the method to invoke
    * @param <O> the Java Source type
    * @return A new method ivocation expression with no arguments
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> MethodCallExpression<O,?> invoke(String method)
   {
      return (MethodCallExpression<O,?>) getFactory().invoke(method);
   }

   /**
    * Creates a field accessor expression
    * @param field   The name of the field to access
    * @param <O> the Java Source type
    * @return  A new accessor for the given field
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Field<O,?> field(String field)
   {
      return (Field<O,?>) getFactory().field(field);
   }

   /**
    * Creates a specialized getter method expression.
    * The method name will start with "get" or "is" depending on the field type, and will be camel-cased according to the javabean conventions
    * @param field   The field exposed by the getter
    * @param klass   The name of the type of the field exposed by the getter.
    * @param <O> the Java Source type the Java Source type
    * @return A new getter expression for the given field
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Getter<O,?> getter(String field, String klass)
   {
      return (Getter<O,?>) getFactory().getter(field, klass);
   }

   /**
    * Creates a specialized getter method expression.
    * The method name will start with "get" or "is" depending on the field type, and will be camel-cased according to the javabean conventions
    * @param field   The field exposed by the getter
    * @param klass   The type of the field exposed by the getter.
    * @param <O> the Java Source type
    * @return A new getter expression for the given field
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Getter<O,?> getter(String field, Class klass)
   {
      return (Getter<O,?>) getFactory().getter(field, klass);
   }

   /**
    * Creates a specialized setter method expression. 
    * The method name will start with "set" and will be camel-cased according to the javabean conventions
    * @param field   The field exposed by the getter
    * @param klass   The name of the type of the field exposed by the setter.
    * @param value   The expression providing the new value for the field              
    * @param <O> the Java Source type
    * @return A new setter expression for the given field 
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Setter<O,?> setter(String field, String klass, ExpressionSource value)
   {
      return (Setter<O,?>) getFactory().setter(field, klass, value);
   }

   /**
    * Creates a specialized setter method expression. 
    * The method name will start with "set" and will be camel-cased according to the javabean conventions
    * @param field   The field exposed by the getter
    * @param klass   The type of the field exposed by the setter.
    * @param value   The expression providing the new value for the field              
    * @param <O> the Java Source type
    * @return A new setter expression for the given field 
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Setter<O,?> setter(String field, Class klass, ExpressionSource value)
   {
      return (Setter<O,?>) getFactory().setter(field, klass, value);
   }

   /**
    * Creates an accessor for the parent class
    * @param <O> the Java Source type
    * @return  A new "super" accessor
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> Super<O,?> sup()
   {
      return (Super<O,?>) getFactory().sup();
   }

   /**
    * Creates a new instanceof expression for the given expression and type name
    * @param klass      The name of the type for the instanceof check
    * @param expression The expression to be evaluated
    * @param <O> the Java Source type
    * @return  A new instanceof expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> InstanceofExpression<O,?> instanceOf(String klass, ExpressionSource expression)
   {
      return (InstanceofExpression<O,?>) getFactory().instanceOf(klass, expression);
   }

   /**
    * Creates a new instanceof expression for the given expression and type 
    * @param klass      The type for the instanceof check
    * @param expression The expression to be evaluated
    * @param <O> the Java Source type
    * @return  A new instanceof expression
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> InstanceofExpression<O,?> instanceOf(Class klass, ExpressionSource expression)
   {
      return (InstanceofExpression<O,?>) getFactory().instanceOf(klass, expression);
   }

}
