/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.statements;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.expressions.Assignment;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

import static org.jboss.forge.roaster.model.expressions.Expressions.assign;
import static org.jboss.forge.roaster.model.expressions.Expressions.classStatic;
import static org.jboss.forge.roaster.model.expressions.Expressions.declare;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newFor;
import static org.jboss.forge.roaster.model.statements.Statements.newForEach;
import static org.jboss.forge.roaster.model.statements.Statements.newInvoke;
import static org.junit.Assert.assertEquals;

public class ForStatementsTest
{

   @Test
   public void testSimpleForWithPrint() throws Exception
   {

      String target = "for (int j=0, k=0; j < 100; j++, k=k + 2) {\n  java.lang.System.out.println(j);\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newFor()
                           .setDeclaration(declare(int.class, "j", literal(0)).addDeclaration("k", literal(0)))
                           .setCondition(operator(Op.LESS)
                                               .addArgument(var("j"))
                                               .addArgument(literal(100)))
                           .addUpdate(var("j").inc())
                           .addUpdate(assign(Assignment.ASSIGN)
                                            .setLeft(var("k"))
                                            .setRight(operator(Op.PLUS)
                                                            .addArgument(var("k"))
                                                            .addArgument(literal(2))))
                           .setBody(newInvoke()
                                          .setMethodName("println")
                                          .setInvocationTarget(classStatic(System.class).field("out"))
                                          .addArgument(var("j"))));

      assertEquals(target, method.getBody().trim());
   }

   @Test
   public void testSimpleForEachWithPrint() throws Exception
   {

      String target = "for (java.lang.String name : p.getNames()) {\n  java.lang.System.out.println(name);\n}";
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");

      method.setBody(newForEach()
                           .setIterator(String.class, "name")
                           .setSource(var("p").getter("names", String.class))
                           .setBody(newInvoke()
                                          .setInvocationTarget(classStatic(System.class).field("out"))
                                          .setMethodName("println")
                                          .addArgument(var("name"))));

      assertEquals(target, method.getBody().trim());
   }

}
