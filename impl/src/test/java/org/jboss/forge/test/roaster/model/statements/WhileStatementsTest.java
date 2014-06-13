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

import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newAssign;
import static org.jboss.forge.roaster.model.statements.Statements.newDoWhile;
import static org.jboss.forge.roaster.model.statements.Statements.newWhile;
import static org.junit.Assert.assertEquals;


public class WhileStatementsTest
{
   @Test
   public void testSimpleWhile() throws Exception
   {
      String target = "while (x < 0) {\n  x=x + 1;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newWhile()
                           .setCondition(operator(Op.LESS)
                                               .addArgument(var("x"))
                                               .addArgument(literal(0)))
                           .setBody(newAssign()
                                          .setLeft(var("x"))
                                          .setRight(operator(Op.PLUS).addArgument(var("x")).addArgument(literal(1)))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testSimpleWhile2() throws Exception
   {
      String target = "while (x < 0) {\n  x=x + 1;\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newWhile()
                           .setBody(newAssign()
                                          .setLeft(var("x"))
                                          .setRight(operator(Op.PLUS).addArgument(var("x")).addArgument(literal(1))))
                           .setCondition(operator(Op.LESS)
                                               .addArgument(var("x"))
                                               .addArgument(literal(0))));

      assertEquals(target, method.getBody().trim());
   }


   @Test
   public void testSimpleDoWhile() throws Exception
   {
      String target = "do {\n  x=x + 1;\n}\n while (x < 0);";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newDoWhile()
                           .setCondition(operator(Op.LESS)
                                               .addArgument(var("x"))
                                               .addArgument(literal(0)))
                           .setBody(newAssign()
                                          .setLeft(var("x"))
                                          .setRight(operator(Op.PLUS).addArgument(var("x")).addArgument(literal(1)))));

      assertEquals(target, method.getBody().trim());
   }


}
