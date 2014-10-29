/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaDoc;
import org.jboss.forge.roaster.model.JavaDocTag;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link JavaDoc} support
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaDocTest
{
   @Test
   public void testJavaDocParsing() throws Exception
   {
      String text = "/** Text */ public class MyClass{}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      Assert.assertNotNull(javaDoc);
      Assert.assertEquals("Text", javaDoc.getText());
      Assert.assertTrue(javaDoc.getTagNames().isEmpty());
   }

   @Test
   public void testJavaDocParsingTags() throws Exception
   {
      String text = "/** Do Something\n*@author George Gastaldi*/ public class MyClass{}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      Assert.assertNotNull(javaDoc);
      Assert.assertEquals("Do Something", javaDoc.getText());
      Assert.assertEquals(1, javaDoc.getTagNames().size());
      JavaDocTag authorTag = javaDoc.getTags("@author").get(0);
      Assert.assertEquals("@author", authorTag.getName());
      Assert.assertEquals("George Gastaldi", authorTag.getValue());
   }

   @Test
   public void testJavaDocRefTag() throws Exception
   {
      String text = "/** Do Something\n*@author George Gastaldi\n*@see JavaDocTest#testJavaDocCreation()\n*/ public class MyClass{}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      Assert.assertNotNull(javaDoc);
      Assert.assertEquals("Do Something", javaDoc.getText());
      Assert.assertEquals(2, javaDoc.getTagNames().size());
      JavaDocTag authorTag = javaDoc.getTags("@author").get(0);
      Assert.assertEquals("@author", authorTag.getName());
      Assert.assertEquals("George Gastaldi", authorTag.getValue());
      JavaDocTag seeTag = javaDoc.getTags("@see").get(0);
      Assert.assertEquals("@see", seeTag.getName());
      Assert.assertEquals("JavaDocTest#testJavaDocCreation()", seeTag.getValue());
   }

   @Test
   public void testJavaDocCreation() throws Exception
   {
      String expected = "/**" + System.lineSeparator()
               + " * Do Something" + System.lineSeparator()
               + " * @author George Gastaldi" + System.lineSeparator() + " */" + System.lineSeparator()
               + "public class MyClass" + System.lineSeparator() + "{" + System.lineSeparator() + "}";
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class).setPublic().setName("MyClass");
      Assert.assertFalse(javaClass.hasJavaDoc());
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      javaDoc.setText("Do Something").addTagValue("@author", "George Gastaldi");
      Assert.assertEquals(expected, javaClass.toString());
   }

   @Test
   public void testJavaDocSetFullText() throws Exception
   {
      String expected = "/**" + System.lineSeparator()
               + " * Do Something" + System.lineSeparator()
               + " * @author George Gastaldi" + System.lineSeparator() + " */" + System.lineSeparator()
               + "public class MyClass" + System.lineSeparator() + "{" + System.lineSeparator() + "}";
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class).setPublic().setName("MyClass");
      Assert.assertFalse(javaClass.hasJavaDoc());
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      javaDoc.setFullText("Do Something" + System.lineSeparator() + "* @author George Gastaldi");
      Assert.assertEquals(expected, javaClass.toString());
   }

   @Test
   public void testJavaDocGetFullText() throws Exception
   {
      String text = "/**" + System.lineSeparator()
               + " * Do Something" + System.lineSeparator()
               + " * @author George Gastaldi" + System.lineSeparator() + " */" + System.lineSeparator()
               + "public class MyClass" + System.lineSeparator() + "{" + System.lineSeparator() + "}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      Assert.assertTrue(javaClass.hasJavaDoc());
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      String expected = "Do Something" + System.lineSeparator() + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocField() throws Exception
   {
      String text =
               "public class MyClass {"
                        + "/**" + System.lineSeparator()
                        + " * Do Something" + System.lineSeparator()
                        + " * @author George Gastaldi" + System.lineSeparator() + " */" + System.lineSeparator() + ""
                        + "private String foo;}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      Assert.assertFalse(javaClass.hasJavaDoc());
      Assert.assertEquals(1, javaClass.getFields().size());
      FieldSource<JavaClassSource> field = javaClass.getFields().get(0);
      Assert.assertTrue(field.hasJavaDoc());
      JavaDocSource<FieldSource<JavaClassSource>> javaDoc = field.getJavaDoc();
      String expected = "Do Something" + System.lineSeparator() + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocMethod() throws Exception
   {
      String text =
               "public class MyClass {"
                        + "/**" + System.lineSeparator()
                        + " * Do Something" + System.lineSeparator()
                        + " * @author George Gastaldi" + System.lineSeparator() + " */" + System.lineSeparator() + ""
                        + "private void foo(){};}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      Assert.assertFalse(javaClass.hasJavaDoc());
      Assert.assertEquals(1, javaClass.getMethods().size());
      MethodSource<JavaClassSource> method = javaClass.getMethods().get(0);
      Assert.assertTrue(method.hasJavaDoc());
      JavaDocSource<MethodSource<JavaClassSource>> javaDoc = method.getJavaDoc();
      String expected = "Do Something" + System.lineSeparator() + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

}
