/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.statements;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

import static org.jboss.forge.roaster.model.statements.Statements.*;
import static org.jboss.forge.roaster.model.expressions.Expressions.*;

public class ReturnStatementsTest
{
   @Test
   public void testReturn() throws Exception
   {
      String target = "return;";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newReturn() );

      assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testReturnArg() throws Exception
   {
      String target = "return x;";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String echo( String x )" );

      method.setBody( newReturn().setReturn( var( "x" ) ) );

      assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testReturnExpr() throws Exception
   {
      String target = "return x + 5;";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String echo( String x )" );

      method.setBody( newReturn().setReturn( operator( Op.PLUS ).addArgument( var( "x" ) ).addArgument( literal( 5 ) ) ) );

      assertEquals( target, method.getBody().trim() );
   }

    @Test
    public void testReturnInvoke() throws Exception
    {
        String target = "return java.util.UUID.randomUUID();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( invoke( "randomUUID" ).setTarget( classStatic( UUID.class ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnInvokeWithArgs() throws Exception
    {
        String target = "return java.util.UUID.fromString(\"xyz\");";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( invoke( "fromString" )
                                                    .setTarget( classStatic( UUID.class ) )
                                                    .addArgument( literal( "xyz" ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnTernary() throws Exception
    {
        String target = "return x > 0 ? 4 : 5;";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public int foo( int x )" );

        method.setBody( newReturn().setReturn( ternary()
                                                       .setCondition( operator( Op.GREATER ).addArgument( var( "x" ) ).addArgument( literal( 0 ) ) )
                                                       .setIfExpression( literal( 4 ) )
                                                       .setElseExpression( literal( 5 ) ) ) );
        ;

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnTernaryWithParen() throws Exception
    {
        String target = "return 1 + (x > 3 ? 4 : 5) + 2;";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public int foo( in x )" );

        method.setBody( newReturn().setReturn(
                operator( Op.PLUS )
                        .addArgument( literal( 1 ) )
                        .addArgument( paren( ternary()
                                                     .setCondition( operator( Op.GREATER ).addArgument( var( "x" ) ).addArgument( literal( 3 ) ) )
                                                     .setIfExpression( literal( 4 ) )
                                                     .setElseExpression( literal( 5 ) ) ) )
                        .addArgument( literal( 2 ) ) ) );
        ;

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnCast() throws Exception
    {
        String target = "return (long)(x);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public long foo( int x )" );

        method.setBody( newReturn().setReturn( cast( long.class, var( "x" ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnNull() throws Exception
    {
        String target = "return null;";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( nullLiteral() ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnChain() throws Exception
    {
        String target = "return getName().bar();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( getter( "name", "String" ).invoke( "bar" ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnChainWithField() throws Exception
    {
        String target = "return getName().baz;";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( getter( "name", "String" ).field( "baz" ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnChainWithFields() throws Exception
    {
        String target = "return this.foo.bar.baz;";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( field( "foo" ).field( "bar" ).field( "baz" ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnChainWithFieldsAndMethods() throws Exception
    {
        String target = "return this.foo.getBar().baz.doSomething(this.x.y).getRes();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( field( "foo" ).getter( "bar", String.class.getName() ).field( "baz" )
                                                       .invoke( "doSomething" ).addArgument( field( "x" ).field( "y" ) ).dot().invoke( "getRes" ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnUnary() throws Exception
    {
        String target = "return 3 * -2;";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( operator( Op.TIMES ).addArgument( literal( 3 ) ).addArgument( operator( PrefixOp.MINUS, literal( 2 ) ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnUnaryParen() throws Exception
    {
        String target = "return 3 * -(2 + 1);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public String foo()" );

        method.setBody( newReturn().setReturn( operator( Op.TIMES )
                                                       .addArgument( literal( 3 ) )
                                                       .addArgument( operator( PrefixOp.MINUS,
                                                                                    paren( operator( Op.PLUS )
                                                                                              .addArgument( literal( 2 ) ).addArgument( literal( 1 ) ) ) ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnArray() throws Exception
    {
        String target = "return new int[2][3];";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public int[][] foo()" );

        method.setBody( newReturn().setReturn( newArray( int.class ).addDimension( literal( 2 ) ).addDimension( literal( 3 ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnArrayWithImpliedInit() throws Exception
    {
        String target = "return new int[][]{{1,2},{3,4}};";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public int[][] foo()" );

        method.setBody( newReturn().setReturn( newArray( int.class ).init(
                vec().addElement(
                        vec().addElement( literal( 1 ) ).addElement( literal( 2 ) ) )
                    .addElement(
                        vec().addElement( literal( 3 ) ).addElement( literal( 4 ) )
                    )
        ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testReturnArrayAccess() throws Exception
    {
        String target = "return x[4][2].getY();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public int[][] foo()" );

        method.setBody( newReturn().setReturn(
                var( "x" ).itemAt( literal( 4 ) ).itemAt( literal( 2 ) ).getter( "y", String.class )
        ) );

        assertEquals( target, method.getBody().trim() );
    }

}
