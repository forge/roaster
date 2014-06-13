/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
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

import static org.jboss.forge.roaster.model.expressions.Expressions.field;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.newInstance;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newAssign;
import static org.jboss.forge.roaster.model.statements.Statements.newDeclare;
import static org.jboss.forge.roaster.model.statements.Statements.newSuper;
import static org.jboss.forge.roaster.model.statements.Statements.newThis;
import static org.junit.Assert.assertEquals;


public class ConstructorStatementsTest
{

   @Test
   public void testEmptyConstructor() throws Exception
   {
       String target = "this(x);";
       MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public Foo()" );

       method.setBody( newThis().addArgument( var( "x" ) ) );
       assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testSuperConstructor() throws Exception
   {
       String target = "super(x);";
       MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public Foo()" );

       method.setBody( newSuper().addArgument( var( "x" ) ) );
       assertEquals( target, method.getBody().trim() );
   }

}
