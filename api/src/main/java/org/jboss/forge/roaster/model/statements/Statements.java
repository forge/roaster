package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.spi.StatementFactory;

import java.util.ServiceLoader;

public abstract class Statements {

    protected static StatementFactory factory;

    protected static StatementFactory getFactory() {
        synchronized ( Statements.class ) {
            ServiceLoader<StatementFactory> sl = ServiceLoader.load( StatementFactory.class, Statements.class.getClassLoader() );
            if ( sl.iterator().hasNext() ) {
                factory = sl.iterator().next();
            } else {
                throw new IllegalStateException( "No StatementFactory implementation available, unable to continue" );
            }
        }
        return factory;
    }

    public static ReturnStatement<?,?> newReturn() {
        return getFactory().newReturn();
    }

    public static IfStatement<?,?> newIf() {
        return getFactory().newIf();
    }

    public static AssignStatement<?,?> newAssign() {
        return getFactory().newAssign();
    }

    public static DeclareStatement<?,?> newDeclare() {
        return getFactory().newDeclare();
    }

    public static ForStatement<?,?> newFor() {
        return getFactory().newFor();
    }

    public static ForEachStatement<?,?> newForEach() {
        return getFactory().newForEach();
    }

    public static InvokeStatement<?,?> newInvoke() {
        return getFactory().newInvoke();
    }

    public static WhileStatement<?,?> newWhile() {
        return getFactory().newWhile();
    }

    public static DoWhileStatement<?,?> newDoWhile() {
        return getFactory().newDoWhile();
    }

    public static BlockStatement<?,?> newBlock() {
        return getFactory().newBlock();
    }

    public static BreakStatement<?,?> newBreak() {
        return getFactory().newBreak();
    }

    public static ContinueStatement<?,?> newContinue() {
        return getFactory().newContinue();
    }

    public static ThrowStatement<?,?> newThrow() {
        return getFactory().newThrow();
    }

    public static TryCatchStatement<?,?> newTryCatch() {
        return getFactory().newTryCatch();
    }

    public static SynchStatement<?,?> newSynchronized() {
        return getFactory().newSynchronized();
    }

    public static ThisStatement<?,?> newThis() {
        return getFactory().newThis();
    }

    public static SuperStatement<?,?> newSuper() {
        return getFactory().newSuper();
    }

    public static AssertStatement<?,?> newAssert() {
        return getFactory().newAssert();
    }

    public static SwitchStatement<?,?> newSwitch() {
        return getFactory().newSwitch();
    }
}
