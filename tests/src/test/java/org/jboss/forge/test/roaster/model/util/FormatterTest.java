/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.Streams;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.util.Formatter;
import org.jboss.forge.test.roaster.model.FieldAnnotationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FormatterTest
{
   public static final String LINE_SEPARATOR = System.getProperty("line.separator");

   private static JavaClassSource javaClass;

   private static String content;

   @BeforeAll
   public static void resetTests() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockUnformattedClass.java";
      try (InputStream stream = FieldAnnotationTest.class.getResourceAsStream(fileName))
      {
         content = Streams.toString(stream);
         javaClass = Roaster.parse(JavaClassSource.class, content);
      }
   }

   @Test
   public void testFormatSource()
   {
      String result = Formatter.format(javaClass);
      String original = javaClass.toUnformattedString();
      assertNotEquals(original, result);
   }

   @Test
   public void testFormatterSource()
   {
      String result = Roaster.format(javaClass.toString());
      String original = javaClass.toUnformattedString();
      assertNotEquals(original, result);
   }

   @Test
   public void testToUnformattedStringAsOriginalContent()
   {
      assertEquals(content, javaClass.toUnformattedString());
   }

   @Test
   public void testFormatWithJavaDocOptions()
   {
      // ROASTER-54
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class SomeClass {}");
      JavaDocSource<JavaClassSource> javaDoc = source.getJavaDoc();
      javaDoc.setText("Class documentation text");
      javaDoc.addTagValue("@author", "George Gastaldi");

      String result1 = Formatter.format(source);

      String sb1 = "/**" + LINE_SEPARATOR +
              " * Class documentation text" + LINE_SEPARATOR +
              " * " + LINE_SEPARATOR +
              " * @author George Gastaldi" + LINE_SEPARATOR +
              " */" + LINE_SEPARATOR +
              "public class SomeClass {" + LINE_SEPARATOR +
              "}";
      assertEquals(sb1, result1);

      Properties prefs = new Properties();
      prefs.put("org.eclipse.jdt.core.formatter.comment.line_length", "25");
      prefs.put("org.eclipse.jdt.core.formatter.comment.new_lines_at_javadoc_boundaries", "false");

      // TODO: Should we explicitly do that before calling the format method?
      prefs = Formatter.applyShadedPackageName(prefs);

      String result2 = Formatter.format(prefs, source);

      String sb2 = "/** Class documentation" + LINE_SEPARATOR +
              " * text" + LINE_SEPARATOR +
              " * " + LINE_SEPARATOR +
              " * @author George" + LINE_SEPARATOR +
              " *         Gastaldi */" + LINE_SEPARATOR +
              "public class SomeClass {" + LINE_SEPARATOR +
              "}";
      assertEquals(sb2, result2);
   }

   @Test
   public void testApplyShadedPackageName()
   {
      Properties prefs = new Properties();
      prefs.put("org.eclipse.jdt.core.formatter.comment.line_length", "123");
      Properties newPrefs = Formatter.applyShadedPackageName(prefs);
      assertThat(newPrefs.keys().nextElement().toString())
               .endsWith("org.eclipse.jdt.core.formatter.comment.line_length");
   }

   @Test
   void testFormatShouldNotFail()
   {
      File prefs = new File(getClass().getResource("jdk11/eclipse-formatter.xml").getFile());
      File source = new File(getClass().getResource("jdk11/ROASTER130.java").getFile());
      assertTimeout(Duration.ofSeconds(3), () -> Formatter.format(prefs, source));
   }

   @Test
   void testFormatterShouldNotFailWhenCustomFormatterExists() throws IOException
   {
      File prefs = new File(getClass().getResource("ROASTER136.properties").getFile());
      File source = new File(getClass().getResource("jdk11/ROASTER130.java").getFile());
      assertThatCode(() -> Formatter.format(prefs,source)).doesNotThrowAnyException();
   }
}