/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.statements;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

import java.util.UUID;

import static org.jboss.forge.roaster.model.expressions.Expressions.cast;
import static org.jboss.forge.roaster.model.expressions.Expressions.classStatic;
import static org.jboss.forge.roaster.model.expressions.Expressions.field;
import static org.jboss.forge.roaster.model.expressions.Expressions.getter;
import static org.jboss.forge.roaster.model.expressions.Expressions.invoke;
import static org.jboss.forge.roaster.model.expressions.Expressions.literal;
import static org.jboss.forge.roaster.model.expressions.Expressions.nullLiteral;
import static org.jboss.forge.roaster.model.expressions.Expressions.operator;
import static org.jboss.forge.roaster.model.expressions.Expressions.paren;
import static org.jboss.forge.roaster.model.expressions.Expressions.sup;
import static org.jboss.forge.roaster.model.expressions.Expressions.ternary;
import static org.jboss.forge.roaster.model.expressions.Expressions.var;
import static org.jboss.forge.roaster.model.statements.Statements.newReturn;
import static org.junit.Assert.assertEquals;

public class SuperTest
{
   @Test
   public void testSuperField() throws Exception
   {
      String target = "return super.x;";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newReturn().setReturn( sup().field( "x" ) ) );

      assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testSuperGetter() throws Exception
   {
      String target = "return super.getX();";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newReturn().setReturn( sup().getter( "x", String.class ) ) );

      assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testSuperSetter() throws Exception
   {
      String target = "return super.setX(y);";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newReturn().setReturn( sup().setter( "x", String.class, var( "y" ) ) ) );

      assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testSuperInvoke() throws Exception
   {
      String target = "return super.foo(x);";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newReturn().setReturn( sup().invoke( "foo" ).addArgument( var( "x" ) ) ) );

      assertEquals( target, method.getBody().trim() );
   }


}
