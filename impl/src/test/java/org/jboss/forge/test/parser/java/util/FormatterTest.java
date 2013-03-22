/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.parser.java.util;

import static org.junit.Assert.assertNotSame;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.util.Formatter;
import org.jboss.forge.test.parser.java.FieldAnnotationTest;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FormatterTest
{
   private static JavaClass javaClass;

   @BeforeClass
   public static void resetTests()
   {
      InputStream stream = FieldAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockUnformattedClass.java");
      javaClass = JavaParser.parse(JavaClass.class, stream);
   }

   @Test
   public void testFormatSource() throws Exception
   {
      // TODO figure out some sort of way to test that this actually works
      String result = Formatter.format(javaClass);
      String original = javaClass.toString();
      assertNotSame(original, result);
   }
}
