/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.spi;

import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.AssertStatement;
import org.jboss.forge.roaster.model.statements.AssignmentStatement;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.BreakStatement;
import org.jboss.forge.roaster.model.statements.EvalExpressionStatement;
import org.jboss.forge.roaster.model.statements.ContinueStatement;
import org.jboss.forge.roaster.model.statements.DeclareStatement;
import org.jboss.forge.roaster.model.statements.DoWhileStatement;
import org.jboss.forge.roaster.model.statements.ForEachStatement;
import org.jboss.forge.roaster.model.statements.ForStatement;
import org.jboss.forge.roaster.model.statements.IfStatement;
import org.jboss.forge.roaster.model.statements.InvokeStatement;
import org.jboss.forge.roaster.model.statements.ReturnStatement;
import org.jboss.forge.roaster.model.statements.SuperStatement;
import org.jboss.forge.roaster.model.statements.SwitchCaseStatement;
import org.jboss.forge.roaster.model.statements.SwitchStatement;
import org.jboss.forge.roaster.model.statements.SynchStatement;
import org.jboss.forge.roaster.model.statements.ThisStatement;
import org.jboss.forge.roaster.model.statements.ThrowStatement;
import org.jboss.forge.roaster.model.statements.TryCatchStatement;
import org.jboss.forge.roaster.model.statements.WhileStatement;

/**
 * This interface supports the creation of statements to be used in a source code block
 * @param <O>
 * @see org.jboss.forge.roaster.spi.ExpressionFactory
 */
public interface StatementFactory<O extends JavaSource<O>> {

    /**
     * Creates a return statement 
     * @return A new return statement
     */
    public ReturnStatement<O,?> newReturn();

    /**
     * Creates a variable assignment statement 
     * @return A new variable assignment statement
     */
    public AssignmentStatement<O,?> newAssign();

    /**
     * Creates a variable declaration statement 
     * @return A new variable declaration statement
     */
    public DeclareStatement<O,?> newDeclare();

    /**
     * Creates a method invocation statement 
     * @return A new method invocation expression statement
     */
    public InvokeStatement<O,?> newInvoke();

    /**
     * Creates an if statement 
     * @return A new if statement
     */
    public IfStatement<O,?> newIf();

    /**
     * Creates a while loop statement
     * @return A new while loop statement
     */
    public WhileStatement<O,?> newWhile();

    /**
     * Creates a for loop statement
     * @return A new for loop statement
     */
    public ForStatement<O,?> newFor();

    /**
     * Creates an enhanced for loop statement
     * @return A new enhanced for loop statement
     */
    public ForEachStatement<O,?> newForEach();

    /**
     * Creates a do while loop statement
     * @return A new while loop statement
     */
    public DoWhileStatement<O,?> newDoWhile();

    /**
     * Creates a break statement
     * @return A new break statement
     */
    public BreakStatement<O,?> newBreak();

    /**
     * Creates a continue statement
     * @return A new continue statement
     */
    public ContinueStatement<O,?> newContinue();

    /**
     * Creates a throw statement
     * @return A new throw statement
     */
    public ThrowStatement<O,?> newThrow();

    /**
     * Creates a try-catch statement
     * @return A new try-catch statement
     */
    public TryCatchStatement<O,?> newTryCatch();

    /**
     * Creates a synchronized block statement
     * @return A new synchronized block statement
     */
    public SynchStatement<O,?> newSynchronized();

    /**
     * Creates a constructor call statement
     * @return A new constructor call statement
     */
    public ThisStatement<O,?> newThis();

    /**
     * Creates a super constructor call statement
     * @return A new super constructor call statement
     */
    public SuperStatement<O,?> newSuper();

    /**
     * Creates an assert statement
     * @return A new assert statement
     */
    public AssertStatement<O,?> newAssert();

    /**
     * Creates a switch statement
     * @return A new switch case statement
     */
    public SwitchStatement<O,?> newSwitch();

    /**
     * Creates an expression statement
     * @return A new expression statement
     */
    public EvalExpressionStatement<O,?> newEval();

    /**
     * Creates a block statement
     * @return A new block statement
     */
    public BlockStatement<O,?> newBlock();

    /**
     * Creates a case element for a switch statement
     * @return A new switch-case element
     */
    public SwitchCaseStatement<O,?> newCase();
}
