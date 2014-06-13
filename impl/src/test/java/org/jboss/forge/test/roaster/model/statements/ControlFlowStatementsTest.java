/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.statements;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

import static org.jboss.forge.roaster.model.expressions.Expressions.construct;
import static org.jboss.forge.roaster.model.expressions.Expressions.declare;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.thisLiteral;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newAssert;
import static org.jboss.forge.roaster.model.statements.Statements.newAssign;
import static org.jboss.forge.roaster.model.statements.Statements.newBlock;
import static org.jboss.forge.roaster.model.statements.Statements.newBreak;
import static org.jboss.forge.roaster.model.statements.Statements.newContinue;
import static org.jboss.forge.roaster.model.statements.Statements.newIf;
import static org.jboss.forge.roaster.model.statements.Statements.newInvoke;
import static org.jboss.forge.roaster.model.statements.Statements.newReturn;
import static org.jboss.forge.roaster.model.statements.Statements.newSwitch;
import static org.jboss.forge.roaster.model.statements.Statements.newSynchronized;
import static org.jboss.forge.roaster.model.statements.Statements.newThrow;
import static org.jboss.forge.roaster.model.statements.Statements.newTryCatch;
import static org.jboss.forge.roaster.model.statements.Statements.newWhile;
import static org.junit.Assert.assertEquals;


public class ControlFlowStatementsTest
{
   @Test
   public void testSimpleLabeledStatement() throws Exception
   {
      String target = "Out: return x;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newReturn().setLabel("Out").setReturn(var("x")));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testBreak() throws Exception
   {
      String target = "if (true) {\n  break;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newBlock()
                           .addStatement(newIf().setCondition(literal(true)).setThen(newBreak())));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testContinue() throws Exception
   {
      String target = "while (true) {\n  continue;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newBlock()
                           .addStatement(newWhile().setCondition(literal(true)).setBody(newContinue())));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testBreakWithLabel() throws Exception
   {
      String target = "while (true) {\n  break exit;\n}\n exit: return x;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newBlock()
                           .addStatement(newWhile().setCondition(literal(true)).setBody(
                                 newBreak().setBreakToLabel("exit")))
                           .addStatement(newReturn().setReturn(var("x")).setLabel("exit")));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testContinueWithLabel() throws Exception
   {
      String target = "while (true) {\n  test:   x=x + 1;\n  continue test;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newBlock()
                           .addStatement(newWhile().setCondition(literal(true)).setBody(
                                 newBlock()
                                       .addStatement(
                                             newAssign().setLeft(var("x")).setRight(operator(Op.PLUS).addArgument(var("x")).addArgument(literal(1)))
                                                   .setLabel("test"))
                                       .addStatement(newContinue().setContinueToLabel("test"))
                           )));

      assertEquals(target, method.getBody().trim());
   }


   @Test
   public void testThrowable() throws Exception
   {
      String target = "throw new java.lang.UnsupportedOperationException(\"sorry\");";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newThrow().setThrowable(construct(UnsupportedOperationException.class).addArgument(literal("sorry"))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testTryCatch() throws Exception
   {
      String target = "try {\n" +
                      "  x.foo();\n" +
                      "}\n catch (java.lang.NullPointerException npe) {\n" +
                      "  npe.printStackTrace();\n" +
                      "}\ncatch (java.lang.IllegalStateException ise) {\n" +
                      "  ise.printStackTrace();\n" +
                      "}\n finally {\n" +
                      "  return;\n" +
                      "}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newTryCatch()
                           .setBody(newInvoke().setMethodName("foo").setInvocationTarget(var("x")))
                           .addCatch(declare(NullPointerException.class, "npe"), newInvoke().setMethodName("printStackTrace").setInvocationTarget(var("npe")))
                           .addCatch(declare(IllegalStateException.class, "ise"), newInvoke().setMethodName("printStackTrace").setInvocationTarget(var("ise")))
                           .setFinally(newReturn())
      );

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testSynchronized() throws Exception
   {
      String target = "synchronized (this) {\n  x++;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newSynchronized().setSynchOn(thisLiteral()).setBody(newBlock().addStatement(var("x").inc())));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testSwitchCase() throws Exception
   {
      String target = "switch (x) {\n" +
                      "case 0:\n" +
                      "  return true;\n" +
                      "case 1:\n" +
                      "break;\n" +
                      "case 2:\n" +
                      "default :\n" +
                      "return false;\n" +
                      "}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newSwitch().setSwitch(var("x"))
                           .addCase(literal(0)).addStatement(newReturn().setReturn(literal(true)))
                           .addCase(literal(1)).addStatement(newBreak())
                           .addCase(literal(2))
                           .addDefault()
                           .addStatement(newReturn().setReturn(literal(false)))
      );

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testAssertion() throws Exception
   {
      String target = "assert x > 0 : \"x GT 0\";";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newAssert()
                           .setAssertion(operator(Op.GREATER).addArgument(var("x")).addArgument(literal(0)))
                           .setMessage(literal("x GT 0")));

      assertEquals(target, method.getBody().trim());
   }


}
