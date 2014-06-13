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

import static org.jboss.forge.roaster.model.expressions.Expressions.instanceOf;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.not;
import static org.jboss.forge.roaster.model.expressions.Expressions.nullLiteral;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.thisLiteral;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newAssign;
import static org.jboss.forge.roaster.model.statements.Statements.newBlock;
import static org.jboss.forge.roaster.model.statements.Statements.newIf;
import static org.jboss.forge.roaster.model.statements.Statements.newReturn;
import static org.junit.Assert.assertEquals;

public class IfStatementsTest
{
   @Test
   public void testSimpleIfWithCondition() throws Exception
   {
      String target = "if (x != null) {\n  x=null;\n}\n else {\n  x=y;\n  x=x + 1;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newIf()
                           .setCondition(operator(Op.NOT_EQUALS).addArgument(var("x")).addArgument(nullLiteral()))
                           .setThen(newAssign().setLeft(var("x")).setRight(nullLiteral()))
                           .setElse(newBlock()
                                          .addStatement(newAssign().setLeft(var("x")).setRight(var("y")))
                                          .addStatement(newAssign().setLeft(var("x")).setRight(
                                                operator(Op.PLUS)
                                                      .addArgument(var("x"))
                                                      .addArgument(literal(1))))));
      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testIfWithDotAccessor() throws Exception
   {
      String target = "if (this.x != y.z) {\n  return false;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newIf()
                           .setCondition(operator(Op.NOT_EQUALS)
                                               .addArgument(thisLiteral().field("x"))
                                               .addArgument(var("y").field("z")))
                           .setThen(newReturn().setReturn(literal(false))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testIfWithNegation() throws Exception
   {
      String target = "if (!x) {\n  return false;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newIf()
                           .setCondition(not(var("x")))
                           .setThen(newReturn().setReturn(literal(false))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testIfWithDoubleNegation() throws Exception
   {
      String target = "if (!!x) {\n  return false;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newIf()
                           .setCondition(not(not(var("x"))))
                           .setThen(newReturn().setReturn(literal(false))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testIfWithInstanceof() throws Exception
   {
      String target = "if (x instanceof Foo) {\n  return false;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newIf()
                           .setCondition(instanceOf("Foo", var("x")))
                           .setThen(newReturn().setReturn(literal(false))));
      assertEquals(target, method.getBody().trim());
   }


}
