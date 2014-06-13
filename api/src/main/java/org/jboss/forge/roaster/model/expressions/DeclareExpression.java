/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;
import java.util.Map;

/**
 * Represent a variable declaration expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface DeclareExpression<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Argument<O,P,DeclareExpression<O,P>>,
      NonPrimitiveExpression<O,P,DeclareExpression<O,P>>
{

   /**
    * Adds an additional variable to the declaration and initializes it
    * @param name The name of the new variable
    * @param expr The expression used to initialize the variable
    * @return  this <code>DeclareExpression</code> itself
    */
   public DeclareExpression<O,P> addDeclaration(String name, ExpressionSource<?,?,?> expr);

   /**
    * Adds an additional variable to the declaration
    * @param name The name of the new variable
    * @return  this <code>DeclareExpression</code> itself
    */
   public DeclareExpression<O,P> addDeclaration(String name);


   /**
    * Returns the name of the first variable in this declaration
    * @return
    */
   public String getVariableName();

   /**
    * Returns the names of all the variables in this declaration
    * @return An immutable list containing the variable names
    */
   public List<String> getVariableNames();

   /**
    * Returns the name of the type of the variable(s)
    * @return The type of this declaration
    */
   public String getVariableType();

   /**
    * Returns the initialization expression of the first declared variable (if any)
    * Notice that this is different from the first initialization in the declaration (e.g. int a, b = 0;)
    * @return  The expression used to initialize the first variable in the declaration
    */
   public ExpressionSource<O,DeclareExpression<O,P>,?> getInitExpression();

   /**
    * Returns the declared variables, associated to their initialization expression(s), if any
    * @return An immutable map associating the declared variable names to their initialization expressions
    */
   public Map<String,ExpressionSource<O,DeclareExpression<O,P>,?>> getInitExpressions();

   /**
    * Returns true if this declaration contains exactly one variable
    * @return true if this declaration contains exactly one variable
    */
   public boolean isSingleDeclaration();

   /**
    * Returns the number of declared variables
    * @return the number of declared variables
    */
   public int getNumVariables();
}
