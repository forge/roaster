package org.jboss.forge.roaster.spi;

import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.AssertStatement;
import org.jboss.forge.roaster.model.statements.AssignStatement;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.BreakStatement;
import org.jboss.forge.roaster.model.statements.ContinueStatement;
import org.jboss.forge.roaster.model.statements.DeclareStatement;
import org.jboss.forge.roaster.model.statements.DoWhileStatement;
import org.jboss.forge.roaster.model.statements.ForEachStatement;
import org.jboss.forge.roaster.model.statements.ForStatement;
import org.jboss.forge.roaster.model.statements.IfStatement;
import org.jboss.forge.roaster.model.statements.InvokeStatement;
import org.jboss.forge.roaster.model.statements.ReturnStatement;
import org.jboss.forge.roaster.model.statements.SuperStatement;
import org.jboss.forge.roaster.model.statements.SwitchStatement;
import org.jboss.forge.roaster.model.statements.SynchStatement;
import org.jboss.forge.roaster.model.statements.ThisStatement;
import org.jboss.forge.roaster.model.statements.ThrowStatement;
import org.jboss.forge.roaster.model.statements.TryCatchStatement;
import org.jboss.forge.roaster.model.statements.WhileStatement;

public interface StatementFactory<O extends JavaSource<O>, T extends BlockHolder<O,?>> {

    /**
     * Creates a return statement 
     * @return
     */
    ReturnStatement<O,Block<O,T>> newReturn();

    /**
     * Creates a variable assignment statement 
     * @return
     */
    AssignStatement<O,Block<O,T>> newAssign();

    /**
     * Creates a variable declaration statement 
     * @return
     */
    DeclareStatement<O,Block<O,T>> newDeclare();

    /**
     * Creates a method invocation statement 
     * @return
     */
    InvokeStatement<O,Block<O,T>> newInvoke();

    /**
     * Creates an if statement 
     * @return
     */
    IfStatement<O,Block<O,T>> newIf();

    /**
     * Creates a while statement 
     * @return
     */
    WhileStatement<O,Block<O,T>> newWhile();

    /**
     * Creates a for statement 
     * @return
     */
    ForStatement<O,Block<O,T>> newFor();

    /**
     * Creates an enhanced for statement 
     * @return
     */
    ForEachStatement<O,Block<O,T>> newForEach();

    /**
     * Creates a block statement
     * @return
     */
    BlockStatement<O,Block<O,T>> newBlock();


    DoWhileStatement<O,Block<O,T>> newDoWhile();

    BreakStatement<O,Block<O,T>> newBreak();

    ContinueStatement<O,Block<O,T>> newContinue();

    ThrowStatement<O,Block<O,T>> newThrow();

    TryCatchStatement<O,Block<O,T>> newTryCatch();

    SynchStatement<O,Block<O,T>> newSynchronized();

    ThisStatement<O,Block<O,T>> newThis();

    SuperStatement<O,Block<O,T>> newSuper();

    AssertStatement<O,Block<O,T>> newAssert();

    SwitchStatement<O,Block<O,T>> newSwitch();
}
