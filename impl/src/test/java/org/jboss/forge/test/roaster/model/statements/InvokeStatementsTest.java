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

import static org.junit.Assert.assertEquals;

import static org.jboss.forge.roaster.model.statements.Statements.*;
import static org.jboss.forge.roaster.model.expressions.Expressions.*;


public class InvokeStatementsTest
{

    @Test
    public void testInvoke() throws Exception
    {
        String target = "java.lang.Math.random();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().setMethod( "random" ).setTarget( classStatic( Math.class ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testInvokeWithArg() throws Exception
    {
        String target = "java.lang.Math.round(2.4);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().setTarget( classStatic( Math.class ) ).setMethod( "round" ).addArgument( literal( 2.4f ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testInvokeWithArgs() throws Exception
    {
        String target = "java.lang.Math.min(3,4);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().addArgument( literal( 3 ) ).addArgument( literal( 4 ) ).setTarget( classStatic( Math.class ) ).setMethod( "min" ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testInvokeWithArgsRevOrder() throws Exception
    {
        String target = "java.lang.Math.min(3,4);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().addArgument( literal( 3 ) ).addArgument( literal( 4 ) ).setTarget( classStatic( Math.class ) ).setMethod( "min" ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testNestedInvoke() throws Exception
    {
        String target = "java.lang.Math.min(java.lang.Math.max(java.lang.Math.round(3,4),6),4);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().setTarget( classStatic( Math.class ) ).setMethod( "min" )
                                .addArgument( invoke( "max" ).setTarget( classStatic( Math.class ) )
                                                      .addArgument( invoke( "round" ).setTarget( classStatic( Math.class ) )
                                                                            .addArgument( literal( 3 ) )
                                                                            .addArgument( literal( 4 ) ) )
                                                      .addArgument( literal( 6 ) ) )
                                .addArgument( literal( 4 ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testInvokeWithArgz() throws Exception
    {
        String target = "java.lang.Math.min(2 + 3,4 * 5);";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().setTarget( classStatic( Math.class ) ).setMethod( "min" )
                                .addArgument( operator( Op.PLUS ).addArgument( literal( 2 ) ).addArgument( literal( 3 ) ) )
                                .addArgument( operator( Op.TIMES ).addArgument( literal( 4 ) ).addArgument( literal( 5 ) ) ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testInvokeOnThis() throws Exception
    {
        String target = "this.toString();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().setTarget( thisLiteral() ).setMethod( "toString" ) );

        assertEquals( target, method.getBody().trim() );
    }

    @Test
    public void testInvokeOnLongChain() throws Exception
    {
        String target = "this.getName().foo().bar().baz.toString();";
        MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void foo()" );

        method.setBody( newInvoke().setMethod( "toString" )
                            .setTarget(
                                    thisLiteral().getter( "name", "String" ).invoke( "foo" ).dot().invoke( "bar" ).dot().field( "baz" ) ) );

        assertEquals( target, method.getBody().trim() );
    }

}
