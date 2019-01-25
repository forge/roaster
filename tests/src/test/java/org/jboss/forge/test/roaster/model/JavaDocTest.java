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
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
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
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");

   @Test
   public void testJavaDocParsing()
   {
      String text = "/** Text */ public class MyClass{}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      Assert.assertNotNull(javaDoc);
      Assert.assertEquals("Text", javaDoc.getText());
      Assert.assertTrue(javaDoc.getTagNames().isEmpty());
   }

   @Test
   public void testJavaDocParsingTags()
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
   public void testJavaDocRefTag()
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
   public void testJavaDocCreation()
   {
      String expected = "/**" + LINE_SEPARATOR
               + " * Do Something" + LINE_SEPARATOR
               + " * " + LINE_SEPARATOR
               + " * @author George Gastaldi" + LINE_SEPARATOR + " */" + LINE_SEPARATOR
               + "public class MyClass {" + LINE_SEPARATOR + "}";
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class).setPublic().setName("MyClass");
      Assert.assertFalse(javaClass.hasJavaDoc());
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      javaDoc.setText("Do Something").addTagValue("@author", "George Gastaldi");
      Assert.assertEquals(expected, javaClass.toString());
   }

   @Test
   public void testJavaDocSetFullText()
   {
      String expected = "/**" + LINE_SEPARATOR
               + " * Do Something" + LINE_SEPARATOR
               + " * " + LINE_SEPARATOR
               + " * @author George Gastaldi" + LINE_SEPARATOR + " */" + LINE_SEPARATOR
               + "public class MyClass {" + LINE_SEPARATOR + "}";
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class).setPublic().setName("MyClass");
      Assert.assertFalse(javaClass.hasJavaDoc());
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      javaDoc.setFullText("Do Something" + LINE_SEPARATOR + "* @author George Gastaldi");
      Assert.assertEquals(expected, javaClass.toString());
   }

   @Test
   public void testJavaDocGetFullText()
   {
      String text = "/**" + LINE_SEPARATOR
               + " * Do Something" + LINE_SEPARATOR
               + " * @author George Gastaldi" + LINE_SEPARATOR + " */" + LINE_SEPARATOR
               + "public class MyClass" + LINE_SEPARATOR + "{" + LINE_SEPARATOR + "}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      Assert.assertTrue(javaClass.hasJavaDoc());
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      String expected = "Do Something" + LINE_SEPARATOR + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocField()
   {
      String text = "public class MyClass {"
               + "/**" + LINE_SEPARATOR
               + " * Do Something" + LINE_SEPARATOR
               + " * @author George Gastaldi" + LINE_SEPARATOR + " */" + LINE_SEPARATOR + ""
               + "private String foo;}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      Assert.assertFalse(javaClass.hasJavaDoc());
      Assert.assertEquals(1, javaClass.getFields().size());
      FieldSource<JavaClassSource> field = javaClass.getFields().get(0);
      Assert.assertTrue(field.hasJavaDoc());
      JavaDocSource<FieldSource<JavaClassSource>> javaDoc = field.getJavaDoc();
      String expected = "Do Something" + LINE_SEPARATOR + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocMethod()
   {
      String text = "public class MyClass {"
               + "/**" + LINE_SEPARATOR
               + " * Do Something" + LINE_SEPARATOR
               + " * @author George Gastaldi" + LINE_SEPARATOR + " */" + LINE_SEPARATOR + ""
               + "private void foo(){};}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      Assert.assertFalse(javaClass.hasJavaDoc());
      Assert.assertEquals(1, javaClass.getMethods().size());
      MethodSource<JavaClassSource> method = javaClass.getMethods().get(0);
      Assert.assertTrue(method.hasJavaDoc());
      JavaDocSource<MethodSource<JavaClassSource>> javaDoc = method.getJavaDoc();
      String expected = "Do Something" + LINE_SEPARATOR + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocEnumConstant()
   {
      String text = "public enum MyEnum {"
               + "/**" + LINE_SEPARATOR
               + " * Do Something" + LINE_SEPARATOR
               + " * @author George Gastaldi" + LINE_SEPARATOR + " */" + LINE_SEPARATOR + ""
               + "MY_CONSTANT;}";
      JavaEnumSource javaEnum = Roaster.parse(JavaEnumSource.class, text);
      Assert.assertFalse(javaEnum.hasJavaDoc());
      EnumConstantSource enumConstant = javaEnum.getEnumConstant("MY_CONSTANT");
      Assert.assertTrue(enumConstant.hasJavaDoc());
      JavaDocSource<EnumConstantSource> javaDoc = enumConstant.getJavaDoc();
      String expected = "Do Something" + LINE_SEPARATOR + "@author George Gastaldi";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocMultiLineShouldNotConcatenateWords()
   {
      String text = "/**" + LINE_SEPARATOR
               + "* The country where this currency is used mostly. This field is just for" + LINE_SEPARATOR
               + "* informational purposes and have no effect on any processing." + LINE_SEPARATOR
               + "*/" + LINE_SEPARATOR
               + "public class MyClass{}";
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, text);
      JavaDocSource<JavaClassSource> javaDoc = javaClass.getJavaDoc();
      String expected = "The country where this currency is used mostly. This field is just for informational purposes and have no effect on any processing.";
      Assert.assertEquals(expected, javaDoc.getFullText());
   }

   @Test
   public void testJavaDocFullTextShouldFormatParamWithSpace()
   {
      JavaClassSource src = Roaster.parse(JavaClassSource.class,
               "package issue;\npublic class Issue { \n" +
                        "   /**\n" +
                        "     * Creates a new instance of CLASS\n" +
                        "     *\n" +
                        "     * @param actual the actual value.\n" +
                        "     * @return the modified text\n" +
                        "     */\n" +
                        "    public static String someMethod(String actual) {\n" +
                        "        return actual;\n" +
                        "    }}");
      MethodSource<JavaClassSource> method = src.getMethods().get(0);
      Assert.assertEquals(
               "Creates a new instance of CLASS" + LINE_SEPARATOR + "@param actual the actual value." + LINE_SEPARATOR
                        + "@return the modified text",
               method.getJavaDoc().getFullText());
   }
}