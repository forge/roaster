/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.util.Formatter;
import org.jboss.forge.roaster.spi.Streams;
import org.jboss.forge.test.roaster.model.FieldAnnotationTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FormatterTest
{
   private static JavaClassSource javaClass;

   private static String content;

   @BeforeClass
   public static void resetTests()
   {
      InputStream stream = FieldAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockUnformattedClass.java");
      content = Streams.toString(stream);
      javaClass = Roaster.parse(JavaClassSource.class, content);
   }

   @Test
   public void testFormatSource() throws Exception
   {
      String result = Formatter.format(javaClass);
      String original = javaClass.toUnformattedString();
      Assert.assertNotEquals(original, result);
   }

   @Test
   public void testFormatterSource() throws Exception
   {
      String result = Roaster.format(javaClass.toString());
      String original = javaClass.toUnformattedString();
      Assert.assertNotEquals(original, result);
   }

   @Test
   public void testToUnformattedStringAsOriginalContent()
   {
      Assert.assertEquals(content, javaClass.toUnformattedString());
   }
}
