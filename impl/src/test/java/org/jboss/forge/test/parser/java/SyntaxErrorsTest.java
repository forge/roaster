/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertTrue;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.JavaSource;
import org.junit.Test;

public class SyntaxErrorsTest
{

   @Test
   public void test()
   {
      JavaSource<?> source = JavaParser.parse(JavaSource.class, "public class Test{public test<>() {}}");
      assertTrue(source.hasSyntaxErrors());
   }

}
