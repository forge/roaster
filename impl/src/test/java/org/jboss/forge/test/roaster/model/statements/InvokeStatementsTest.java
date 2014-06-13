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

import static org.jboss.forge.roaster.model.expressions.Expressions.classStatic;
import static org.jboss.forge.roaster.model.expressions.Expressions.construct;
import static org.jboss.forge.roaster.model.expressions.Expressions.invoke;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.thisLiteral;
import static org.jboss.forge.roaster.model.statements.Statements.newEval;
import static org.jboss.forge.roaster.model.statements.Statements.newInvoke;
import static org.junit.Assert.assertEquals;


public class InvokeStatementsTest
{

   @Test
   public void testInvoke() throws Exception
   {
      String target = "java.lang.Math.random();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().setMethodName("random").setInvocationTarget(classStatic(Math.class)));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testInvokeWithArg() throws Exception
   {
      String target = "java.lang.Math.round(2.4);";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().setInvocationTarget(classStatic(Math.class)).setMethodName("round").addArgument(literal(2.4f)));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testInvokeWithArgs() throws Exception
   {
      String target = "java.lang.Math.min(3,4);";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().addArgument(literal(3)).addArgument(literal(4)).setInvocationTarget(classStatic(Math.class)).setMethodName("min"));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testInvokeWithArgsRevOrder() throws Exception
   {
      String target = "java.lang.Math.min(3,4);";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().addArgument(literal(3)).addArgument(literal(4)).setInvocationTarget(classStatic(Math.class)).setMethodName("min"));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testNestedInvoke() throws Exception
   {
      String target = "java.lang.Math.min(java.lang.Math.max(java.lang.Math.round(3,4),6),4);";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().setInvocationTarget(classStatic(Math.class)).setMethodName("min")
                           .addArgument(invoke("max").setInvocationTarget(classStatic(Math.class))
                                              .addArgument(invoke("round").setInvocationTarget(classStatic(Math.class))
                                                                 .addArgument(literal(3))
                                                                 .addArgument(literal(4)))
                                              .addArgument(literal(6)))
                           .addArgument(literal(4)));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testInvokeWithArgz() throws Exception
   {
      String target = "java.lang.Math.min(2 + 3,4 * 5);";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().setInvocationTarget(classStatic(Math.class)).setMethodName("min")
                           .addArgument(operator(Op.PLUS).addArgument(literal(2)).addArgument(literal(3)))
                           .addArgument(operator(Op.TIMES).addArgument(literal(4)).addArgument(literal(5))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testInvokeOnThis() throws Exception
   {
      String target = "this.toString();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().setInvocationTarget(thisLiteral()).setMethodName("toString"));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testInvokeOnLongChain() throws Exception
   {
      String target = "this.getName().foo().bar().baz.toString();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newInvoke().setMethodName("toString")
                           .setInvocationTarget(
                                 thisLiteral().getter("name", "String").invoke("foo").invoke("bar").field("baz")));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testEval()
   {
      String target = "3 + 4;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newEval().setExpr(operator(Op.PLUS).addArgument(literal(3)).addArgument(literal(4))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testNewInstanceWithMethod()
   {
      String target = "new java.lang.Integer(42).toString();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newEval().setExpr(construct(Integer.class).addArgument(literal(42)).invoke("toString")));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testNewInstance()
   {
      String target = "new java.lang.Integer();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo()");

      method.setBody(newEval().setExpr(construct(Integer.class)));

      assertEquals(target, method.getBody().trim());
   }
}
