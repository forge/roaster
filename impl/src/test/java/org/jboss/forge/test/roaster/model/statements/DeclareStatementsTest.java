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
import static org.jboss.forge.roaster.model.expressions.Expressions.field;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newAssign;
import static org.jboss.forge.roaster.model.statements.Statements.newDeclare;
import static org.junit.Assert.assertEquals;


public class DeclareStatementsTest
{

   @Test
   public void testDeclare() throws Exception
   {
      String target = "java.lang.Integer y=new java.lang.Integer();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newDeclare().setVariable(Integer.class, "y", construct(Integer.class)));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testDeclarePrimitive() throws Exception
   {
      String target = "int y;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newDeclare().setVariable(int.class, "y"));

      assertEquals(target, method.getBody().trim());
   }


   @Test
   public void testDeclareAndInit() throws Exception
   {
      String target = "java.lang.Integer y=0;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newDeclare().setVariable(Integer.class, "y", literal(0)));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testDeclareManyAndInit() throws Exception
   {
      String target = "int x, y=0, z=3, w;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newDeclare().setVariable(int.class, "x").addVariable("y", literal(0)).addVariable("z", literal(3)).addVariable("w"));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testSimpleAssignment() throws Exception
   {
      String target = "this.name=x;";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newAssign().setLeft(field("name")).setRight(var("x")));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testAssignmentExpr() throws Exception
   {
      String target = "this.name=this.name + \" Doe\";";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newAssign()
                           .setLeft(field("name"))
                           .setRight(operator(Op.PLUS).addArgument(field("name")).addArgument(literal(" Doe"))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testDeclareWithGenerics() throws Exception
   {
      String target = "java.util.Map<String,java.util.List<Object>> y=new java.util.HashMap<String,java.util.List<Object>>();";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void foo( String x )");

      method.setBody(newDeclare()
                           .setVariable("java.util.Map<String,java.util.List<Object>>",
                                        "y",
                                        construct("java.util.HashMap<String,java.util.List<Object>>")));

      assertEquals(target, method.getBody().trim());
   }


}
