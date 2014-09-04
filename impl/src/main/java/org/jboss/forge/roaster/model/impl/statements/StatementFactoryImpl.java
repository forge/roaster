package org.jboss.forge.roaster.model.impl.statements;

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
import org.jboss.forge.roaster.spi.StatementFactory;

public class StatementFactoryImpl<O extends JavaSource<O>, T extends BlockHolder<O,?>> implements StatementFactory<O,T> {


    public StatementFactoryImpl() { }


    public ReturnStatement<O, Block<O, T>> newReturn() {
        ReturnStatementImpl<O,Block<O,T>> returnSt = new ReturnStatementImpl<O, Block<O, T>>();
        return returnSt;
    }

    public AssignStatement<O, Block<O, T>> newAssign() {
        AssignStatementImpl<O,Block<O,T>> assign = new AssignStatementImpl<O, Block<O, T>>();
        return assign;
    }

    public DeclareStatement<O, Block<O, T>> newDeclare() {
        DeclareStatementImpl<O,Block<O,T>> declare = new DeclareStatementImpl<O, Block<O, T>>();
        return declare;
    }

    public InvokeStatement<O, Block<O, T>> newInvoke() {
        InvokeStatementImpl<O,Block<O,T>> invoke = new InvokeStatementImpl<O, Block<O, T>>();
        return invoke;
    }

    public IfStatement<O, Block<O, T>> newIf() {
        IfStatementImpl<O,Block<O,T>> iff = new IfStatementImpl<O, Block<O, T>>();
        return iff;
    }

    public WhileStatement<O, Block<O, T>> newWhile() {
        WhileStatementImpl<O,Block<O,T>> loop = new WhileStatementImpl<O, Block<O, T>>();
        return loop;
    }

    public DoWhileStatement<O, Block<O, T>> newDoWhile() {
        DoWhileStatementImpl<O,Block<O,T>> loop = new DoWhileStatementImpl<O, Block<O, T>>();
        return loop;
    }

    public ForStatement<O, Block<O, T>> newFor() {
        ForStatementImpl<O,Block<O,T>> loop = new ForStatementImpl<O, Block<O, T>>();
        return loop;
    }

    public ForEachStatement<O, Block<O, T>> newForEach() {
        ForEachStatementImpl<O,Block<O,T>> loop = new ForEachStatementImpl<O, Block<O, T>>();
        return loop;
    }

    @Override
    public BlockStatement<O, Block<O, T>> newBlock() {
        BlockStatementImpl<O,Block<O,T>> child = new BlockStatementImpl<O, Block<O, T>>();
        return child;
    }

    @Override
    public BreakStatement<O, Block<O, T>> newBreak() {
        BreakStatement<O,Block<O,T>> stop = new BreakStatementImpl<O, Block<O, T>>();
        return stop;
    }

    @Override
    public ContinueStatement<O, Block<O, T>> newContinue() {
        ContinueStatement<O,Block<O,T>> goon = new ContinueStatementImpl<O, Block<O, T>>();
        return goon;
    }
    
    @Override
    public ThrowStatement<O, Block<O, T>> newThrow() {
        ThrowStatement<O,Block<O,T>> throwX = new ThrowStatementImpl<O, Block<O, T>>();
        return throwX;
    }

    @Override
    public TryCatchStatement<O, Block<O, T>> newTryCatch() {
        TryCatchStatement<O,Block<O,T>> tryCatch = new TryCatchStatementImpl<O,Block<O,T>>();
        return tryCatch;
    }

    @Override
    public SynchStatement<O, Block<O, T>> newSynchronized() {
        SynchStatement<O,Block<O,T>> synch = new SynchStatementImpl<O, Block<O, T>>();
        return synch;
    }

    @Override
    public ThisStatement<O, Block<O, T>> newThis() {
        ThisStatement<O,Block<O,T>> self = new ThisStatementImpl<O, Block<O, T>>();
        return self;
    }

    @Override
    public SuperStatement<O, Block<O, T>> newSuper() {
        SuperStatement<O,Block<O,T>> sup = new SuperStatementImpl<O, Block<O, T>>();
        return sup;
    }

    @Override
    public AssertStatement<O, Block<O, T>> newAssert() {
        AssertStatement<O,Block<O,T>> sup = new AssertStatementImpl<O, Block<O, T>>();
        return sup;
    }

    @Override
    public SwitchStatement<O, Block<O, T>> newSwitch() {
        SwitchStatement<O,Block<O,T>> sup = new SwitchStatementImpl<O, Block<O, T>>();
        return sup;
    }

}
