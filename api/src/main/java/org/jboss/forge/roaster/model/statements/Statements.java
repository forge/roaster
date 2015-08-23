/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.StatementFactory;

import java.util.ServiceLoader;

public abstract class Statements
{

   protected static StatementFactory factory;

   @SuppressWarnings("unchecked")
   protected static <O extends JavaSource<O>> StatementFactory<O> getFactory()
   {
      synchronized (Statements.class)
      {
         ServiceLoader<StatementFactory> sl = ServiceLoader.load(StatementFactory.class, Statements.class.getClassLoader());
         if (sl.iterator().hasNext())
         {
            factory = sl.iterator().next();
         } else
         {
            throw new IllegalStateException("No StatementFactory implementation available, unable to continue");
         }
      }
      return factory;
   }

   /**
    * Creates a return statement
    * @param <O> the Java Source type 
    * @return a new, empty return statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ReturnStatement<O,?> newReturn()
   {
      return (ReturnStatement<O,?>) getFactory().newReturn();
   }

   /**
    * Creates an if statement
    * @param <O> the Java Source type
    * @return a new, empty if statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> IfStatement<O,BlockSource<O,?,?>> newIf()
   {
      return (IfStatement<O,BlockSource<O,?,?>>) getFactory().newIf();
   }

   /**
    * Creates an variable assignment statement
    * @param <O> the Java Source type
    * @return a new, empty assign statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> AssignmentStatement<O,BlockSource<O,?,?>> newAssign()
   {
      return (AssignmentStatement<O,BlockSource<O,?,?>>) getFactory().newAssign();
   }

   /**
    * Creates a variable declaration statement
    * @param <O> the Java Source type
    * @return a new, empty declaration statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> DeclareStatement<O,BlockSource<O,?,?>> newDeclare()
   {
      return (DeclareStatement<O,BlockSource<O,?,?>>) getFactory().newDeclare();
   }

   /**
    * Creates a for loop statement
    * @param <O> the Java Source type
    * @return a new, empty for statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ForStatement<O,BlockSource<O,?,?>> newFor()
   {
      return (ForStatement<O,BlockSource<O,?,?>>) getFactory().newFor();
   }

   /**
    * Creates an enhanced for loop statement
    * @param <O> the Java Source type
    * @return a new, emtpy enhanced for statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ForEachStatement<O,BlockSource<O,?,?>> newForEach()
   {
      return (ForEachStatement<O,BlockSource<O,?,?>>) getFactory().newForEach();
   }

   /**
    * Creates a specialized expression statement, which invokes a method
    * @param <O> the Java Source type
    * @return a new, empty method invocation expression statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> InvokeStatement<O,BlockSource<O,?,?>> newInvoke()
   {
      return (InvokeStatement<O,BlockSource<O,?,?>>) getFactory().newInvoke();
   }

   /**
    * Creates a while do loop statement
    * @param <O> the Java Source type
    * @return a new, empty while statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> WhileStatement<O,BlockSource<O,?,?>> newWhile()
   {
      return (WhileStatement<O,BlockSource<O,?,?>>) getFactory().newWhile();
   }

   /**
    * Creates a do while loop statement
    * @param <O> the Java Source type
    * @return a new, empty do statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> DoWhileStatement<O,BlockSource<O,?,?>> newDoWhile()
   {
      return (DoWhileStatement<O,BlockSource<O,?,?>>) getFactory().newDoWhile();
   }

   /**
    * Creates a block statement
    * @param <O> the Java Source type
    * @return a new, empty block statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> BlockStatement<O,BlockSource<O,?,?>> newBlock()
   {
      return (BlockStatement<O,BlockSource<O,?,?>>) getFactory().newBlock();
   }

   /**
    * Creates a break statement
    * @param <O> the Java Source type
    * @return a new, empty continue statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> BreakStatement<O,BlockSource<O,?,?>> newBreak()
   {
      return (BreakStatement<O,BlockSource<O,?,?>>) getFactory().newBreak();
   }

   /**
    * Creates a continue statement
    * @param <O> the Java Source type
    * @return a new, empty continue statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ContinueStatement<O,BlockSource<O,?,?>> newContinue()
   {
      return (ContinueStatement<O,BlockSource<O,?,?>>) getFactory().newContinue();
   }

   /**
    * Creates a throw statement
    * @param <O> the Java Source type
    * @return a new, empty throw statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ThrowStatement<O,BlockSource<O,?,?>> newThrow()
   {
      return (ThrowStatement<O,BlockSource<O,?,?>>) getFactory().newThrow();
   }

   /**
    * Creates a try .. catch .. finally statement
    * @param <O> the Java Source type
    * @return a new, empty try statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> TryCatchStatement<O,BlockSource<O,?,?>> newTryCatch()
   {
      return (TryCatchStatement<O,BlockSource<O,?,?>>) getFactory().newTryCatch();
   }

   /**
    * Creates a new synchronized block statement
    * @param <O> the Java Source type
    * @return a new, empty synchronized statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> SynchStatement<O,BlockSource<O,?,?>> newSynchronized()
   {
      return (SynchStatement<O,BlockSource<O,?,?>>) getFactory().newSynchronized();
   }

   /**
    * Creates a local constructor call statement 
    * @param <O> the Java Source type
    * @return a new, empty constructor call statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> ThisStatement<O,BlockSource<O,?,?>> newThis()
   {
      return (ThisStatement<O,BlockSource<O,?,?>>) getFactory().newThis();
   }

   /**
    * Creates a super constructor call statement
    * @param <O> the Java Source type
    * @return a new, empty super constructor call statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> SuperStatement<O,BlockSource<O,?,?>> newSuper()
   {
      return (SuperStatement<O,BlockSource<O,?,?>>) getFactory().newSuper();
   }

   /**
    * Creates a new assert statement
    * @param <O> the Java Source type
    * @return a new, empty assert statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> AssertStatement<O,BlockSource<O,?,?>> newAssert()
   {
      return (AssertStatement<O,BlockSource<O,?,?>>) getFactory().newAssert();
   }

   /**
    * Creates a new switch statement
    * @param <O> the Java Source type
    * @return a new, empty switch statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> SwitchStatement<O,BlockSource<O,?,?>> newSwitch()
   {
      return (SwitchStatement<O,BlockSource<O,?,?>>) getFactory().newSwitch();
   }

   /**
    * Creates a new expression evaluation statement
    * @param <O> the Java Source type
    * @return a new, empty expression statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> EvalExpressionStatement<O,BlockSource<O,?,?>> newEval()
   {
      return (EvalExpressionStatement<O,BlockSource<O,?,?>>) getFactory().newEval();
   }

   /**
    * Creates a case statement to be used within a switch statement
    * @param <O> the Java Source type
    * @return a new, empty case statement
    */
   @SuppressWarnings("unchecked")
   public static <O extends JavaSource<O>> SwitchCaseStatement<O,SwitchStatement<O,BlockSource<O,?,?>>> newCase()
   {
      return (SwitchCaseStatement<O,SwitchStatement<O,BlockSource<O,?,?>>>) getFactory().newCase();
   }

}
