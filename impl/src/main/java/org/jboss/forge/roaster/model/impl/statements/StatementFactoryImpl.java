/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.AssertStatement;
import org.jboss.forge.roaster.model.statements.AssignmentStatement;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.BreakStatement;
import org.jboss.forge.roaster.model.statements.ContinueStatement;
import org.jboss.forge.roaster.model.statements.DeclareStatement;
import org.jboss.forge.roaster.model.statements.DoWhileStatement;
import org.jboss.forge.roaster.model.statements.EvalExpressionStatement;
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
import org.jboss.forge.roaster.spi.StatementFactory;

public class StatementFactoryImpl<O extends JavaSource<O>,
      P extends BlockSource<O,? extends BlockHolder<O>,P>>
      implements StatementFactory<O>
{

   public StatementFactoryImpl()
   {
   }


   public ReturnStatement<O,P> newReturn()
   {
      return new ReturnStatementImpl<O,P>();
   }

   public AssignmentStatement<O,P> newAssign()
   {
      return new AssignStatementImpl<O,P>();
   }

   public DeclareStatement<O,P> newDeclare()
   {
      return new DeclareStatementImpl<O,P>();
   }

   public InvokeStatement<O,P> newInvoke()
   {
      return new InvokeStatementImpl<O,P>();
   }

   public IfStatement<O,P> newIf()
   {
      return new IfStatementImpl<O,P>();
   }

   public WhileStatement<O,P> newWhile()
   {
      return new WhileStatementImpl<O,P>();
   }

   public DoWhileStatement<O,P> newDoWhile()
   {
      return new DoWhileStatementImpl<O,P>();
   }

   public ForStatement<O,P> newFor()
   {
      return new ForStatementImpl<O,P>();
   }

   public ForEachStatement<O,P> newForEach()
   {
      return new ForEachStatementImpl<O,P>();
   }

   @Override
   public BlockStatement<O,P> newBlock()
   {
      return new BlockStatementImpl<O,P>();
   }

   @Override
   public BreakStatement<O,P> newBreak()
   {
      return new BreakStatementImpl<O,P>();
   }

   @Override
   public ContinueStatement<O,P> newContinue()
   {
      return new ContinueStatementImpl<O,P>();
   }

   @Override
   public ThrowStatement<O,P> newThrow()
   {
      return new ThrowStatementImpl<O,P>();
   }

   @Override
   public TryCatchStatement<O,P> newTryCatch()
   {
      return new TryCatchStatementImpl<O,P>();
   }

   @Override
   public SynchStatement<O,P> newSynchronized()
   {
      return new SynchStatementImpl<O,P>();
   }

   @Override
   public ThisStatement<O,P> newThis()
   {
      return new ThisStatementImpl<O,P>();
   }

   @Override
   public SuperStatement<O,P> newSuper()
   {
      return new SuperStatementImpl<O,P>();
   }

   @Override
   public AssertStatement<O,P> newAssert()
   {
      return new AssertStatementImpl<O,P>();
   }

   @Override
   public SwitchStatement<O,P> newSwitch()
   {
      return new SwitchStatementImpl<O,P>();
   }

   @Override
   public EvalExpressionStatement<O,P> newEval()
   {
      return new ExpressionStatementImpl<O,P>();
   }

   @Override
   public SwitchCaseStatement<O,?> newCase()
   {
      return new SwitchCaseStatementImpl();
   }
}
