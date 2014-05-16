/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.SyntaxError;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntaxErrorsTest
{

   @Test
   public void test()
   {
      JavaSource<?> source = Roaster.parse(JavaSource.class, "public class Test{public test<>() {}}");
      assertTrue(source.hasSyntaxErrors());
   }

    @Test
    public void lineAndColumnTest()
    {

        String sourceCode = "public class Test {\n\n" +
                "    WRONG_TOKEN_1 private int field1;\n\n" +
                "    public int test() {\n" +
                "        return -1;\n" +
                "    }\n" +
                "            WRONG_TOKEN_2\n" +
                "}";

        JavaSource<?> source = Roaster.parse(JavaSource.class, sourceCode);
        assertTrue(source.hasSyntaxErrors());
        assertEquals( 2, source.getSyntaxErrors().size() );

        SyntaxError syntaxError = source.getSyntaxErrors().get( 0 );

        assertEquals( 3, syntaxError.getLine() );
        assertEquals( 4, syntaxError.getColumn() );
        assertEquals( true, syntaxError.isError() );

        syntaxError = source.getSyntaxErrors().get( 1 );

        assertEquals( 8, syntaxError.getLine() );
        assertEquals( 12, syntaxError.getColumn() );
        assertEquals( true, syntaxError.isError() );

    }

}
