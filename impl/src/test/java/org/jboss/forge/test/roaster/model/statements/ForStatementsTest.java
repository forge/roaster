/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
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

import static org.junit.Assert.assertEquals;

import static org.jboss.forge.roaster.model.statements.Statements.*;
import static org.jboss.forge.roaster.model.expressions.Expressions.*;

public class ForStatementsTest
{

   @Test
   public void testSimpleForWithPrint() throws Exception
   {

      String target = "for (int j=0, k=0; j < 100; j++, k=k + 2) {\n  java.lang.System.out.println(j);\n}";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newFor()
                            .addDeclaration( declare( int.class, "j" ).init( literal( 0 ) ) )
                            .addDeclaration( declare( int.class, "k" ).init( literal( 0 ) ) )
                            .setCondition( operator( Op.LESS )
                                                   .addArgument( var( "j" ) )
                                                   .addArgument( literal( 100 ) ) )
                            .addUpdate( var( "j" ).inc() )
                            .addUpdate( assign( Assignment.ASSIGN )
                                                .setLeft( var( "k" ) )
                                                .setRight( operator( Op.PLUS )
                                                                   .addArgument( var( "k" ) )
                                                                   .addArgument( literal( 2 ) ) ) )
                            .setBody( newInvoke()
                                              .setMethod( "println" )
                                              .setTarget( classStatic( System.class ).field( "out" ) )
                                              .addArgument( var( "j" ) ) ) );

      assertEquals( target, method.getBody().trim() );
   }

   @Test
   public void testSimpleForEachWithPrint() throws Exception
   {

      String target = "for (java.lang.String name : p.getNames()) {\n  java.lang.System.out.println(name);\n}";
      MethodSource<JavaClassSource> method = Roaster.create( JavaClassSource.class ).addMethod( "public void hello()" );

      method.setBody( newForEach()
                            .setIterator( String.class, "name" )
                            .setSource( var( "p" ).getter( "names", String.class ) )
                            .setBody( newInvoke()
                                                .setTarget( classStatic( System.class ).field( "out" ) )
                                                .setMethod( "println" )
                                                .addArgument( var( "name" ) ) ) );

      assertEquals( target, method.getBody().trim() );
   }

}
