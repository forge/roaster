/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.Assignment;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents an assignment statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface AssignmentStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ExpressionStatement<O,P,AssignmentStatement<O,P>>
{

   /**
    * Returns the LHS expression of this assignment statement
    * @return the LHS expression of this assignment statement
    */
   public Accessor<O,AssignExpression<O,AssignmentStatement<O,P>>,?> getLeft();

   /**
    * Sets the LHS of the assignment
    * @param left the expression that evaluates to the variable to be assigned
    * @return  this <code>AssignmentStatement</code> itself
    */
   public AssignmentStatement<O,P> setLeft(Accessor<?,?,?> left);

   /**
    * Returns the assignment operator used in this statement
    * @return the assignment operator used in this statement
    * @see org.jboss.forge.roaster.model.expressions.Assignment
    */
   public String getAssignmentOperator();

   /**
    * Sets the assignment operator to be used in this statement
    * @param op   the assignment operator
    * @return  this <code>AssignmentStatement</code> itself
    */
   public AssignmentStatement<O,P> setAssignmentOperator(Assignment op);

   /**
    * Returns the RHS expression of this assignment statement
    * @return the RHS expression of this assignment statement
    */
   public ExpressionSource<O,AssignExpression<O,AssignmentStatement<O,P>>,?> getRight();

   /**
    * Sets the RHS of the assignment
    * @param right the expression that evaluates to the value to be assigned
    * @return  this <code>AssignmentStatement</code> itself
    */
   public AssignmentStatement<O,P> setRight(ExpressionSource<?,?,?> right);

}
