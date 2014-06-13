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

/**
 * Represents a switch statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface SwitchStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ControlFlowStatement<O,P,SwitchStatement<O,P>>,
      BodyHolder<O,P,SwitchStatement<O,P>>
{

   /**
    * Returns a list containing all the individual case expressions in this statement
    * @return
    */
   public List<ExpressionSource<O,SwitchStatement<O,P>,?>> getCaseExpressions();

   /**
    * Adds a case expression to the statement.
    * It will be wrapped internally with a <code>SwitchCaseStatement</code>
    * @param option  the case expression
    * @return this <code>SwitchStatement</code> itself
    */
   public SwitchStatement<O,P> addCase(ExpressionSource<?,?,?> option);

   /**
    * Adds a case statement, wrapping a case expression
    * @param option  the case statement
    * @return this <code>SwitchStatement</code> itself
    */
   public SwitchStatement<O,P> addCase(SwitchCaseStatement<?,?> option);

   /**
    * Adds the "default" case statement
    * @return this <code>SwitchStatement</code> itself
    */
   public SwitchStatement<O,P> addDefault();


   /**
    * Returns all the code statements in the body of this switch, including the <code>SwitchCaseStatement</code>,
    * appropriately interleaved
    * @return An immutable list with the statements in the body of this <code>SwitchStatement</code>
    */
   public List<StatementSource<O,SwitchStatement<O,P>,?>> getStatements();

   /**
    * Adds a code statement to the body of this <code>SwitchStatement</code>
    * @param arg the statement to be added
    * @return this <code>SwitchStatement</code> itself
    */
   public SwitchStatement<O,P> addStatement(StatementSource<?,?,?> arg);


   /**
    * Returns the expression returning the value on which the cases are evaluated
    * @return the expression on which the cases are evaluated
    */
   public ExpressionSource<O,SwitchStatement<O,P>,?> getSwitch();

   /**
    * Sets the switch expression
    * @param expr the expression on which the cases are evaluated
    * @return this <code>SwitchStatement</code> itself
    */
   public SwitchStatement<O,P> setSwitch(ExpressionSource<?,?,?> expr);

}
