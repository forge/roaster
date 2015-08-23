/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;
import java.util.Map;

/**
 * Represents a variable declaration statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface DeclareStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementSource<O,P,DeclareStatement<O,P>>
{


   /**
    * Returns the names of the variables declared in this statement
    * @return the names of the variables declared in this statement
    */
   public List<String> getVariableNames();

   /**
    * Returns the name of the first variable declared in this statement
    * @return the name of the first variable declared in this statement
    */
   public String getVariableName();

   /**
    * Returns the type of the variables declared in this statement
    * @return the type of the variables declared in this statement
    */
   public String getVariableType();

   /**
    * Returns the declared variables, associated to their initialization expression(s), if any
    * @return An immutable map associating the declared variable names to their initialization expressions
    */
   public Map<String,ExpressionSource<O,DeclareStatement<O,P>,?>> getInitExpressions();

   /**
    * Returns the initialization expression of the first declared variable (if any)
    * Notice that this is different from the first initialization in the declaration (e.g. int a, b = 0;)
    * @return  The expression used to initialize the first variable in the declaration
    */
   public ExpressionSource<O,DeclareStatement<O,P>,?> getInitExpression();

   /**
    * Returns true if this declaration statement contains exactly one variable
    * @return true if this declaration statement contains exactly one variable
    */
   public boolean isSingleDeclaration();


   /**
    * Sets the name and type of the first variable declared by this statement
    * @param name    The name of the new variable
    * @param klass   The type of the variable
    * @return  this <code>DeclareStatement</code> itself
    */
   public DeclareStatement<O,P> setVariable(Class klass, String name);

   /**
    * Sets the name and type of the first variable declared by this statement
    * @param name    The name of the new variable
    * @param klass   The name of the type of the variable
    * @return  this <code>DeclareStatement</code> itself
    */
   public DeclareStatement<O,P> setVariable(String klass, String name);

   /**
    * Sets the name and type of the first variable declared by this statement and initializes it
    * @param name    The name of the new variable
    * @param klass   The type of the variable
    * @param init The expression used to initialize the variable
    * @return  this <code>DeclareStatement</code> itself
    */
   public DeclareStatement<O,P> setVariable(Class klass, String name, ExpressionSource<?,?,?> init);

   /**
    * Sets the name and type of the first variable declared by this statement and initializes it
    * @param name    The name of the new variable
    * @param klass   The name of the type of the variable
    * @param init The expression used to initialize the variable
    * @return  this <code>DeclareStatement</code> itself
    */
   public DeclareStatement<O,P> setVariable(String klass, String name, ExpressionSource<?,?,?> init);

   /**
    * Adds an additional variable to the declaration statement
    * @param name The name of the new variable
    * @return  this <code>DeclareStatement</code> itself
    */
   public DeclareStatement<O,P> addVariable(String name);

   /**
    * Adds an additional variable to the declaration statement and initializes it
    * @param name The name of the new variable
    * @param init The expression used to initialize the variable
    * @return  this <code>DeclareStatement</code> itself
    */
   public DeclareStatement<O,P> addVariable(String name, ExpressionSource<?,?,?> init);


}