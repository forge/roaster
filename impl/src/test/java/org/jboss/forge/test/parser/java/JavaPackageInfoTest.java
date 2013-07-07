package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.JavaPackageInfo;
import org.jboss.forge.parser.java.ValuePair;
import org.junit.Assert;
import org.junit.Test;

public class JavaPackageInfoTest
{

   @Test
   public void testCanParsePackageInfo() throws Exception
   {
      InputStream stream = JavaPackageInfoTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/package-info.java");
      JavaPackageInfo javaPkg = JavaParser.parse(JavaPackageInfo.class, stream);
      assertEquals("org.jboss.forge.test.parser.java", javaPkg.getPackage());
      Assert.assertEquals("package-info", javaPkg.getName());
      Assert.assertNotNull(javaPkg.getImport("javax.xml.bind.annotation.XmlSchema"));
      Import XmlAccessTypeField = javaPkg.getImport("javax.xml.bind.annotation.XmlAccessType.FIELD");
      Assert.assertNotNull(XmlAccessTypeField);
      Assert.assertTrue(XmlAccessTypeField.isStatic());
      List<Annotation<JavaPackageInfo>> annotations = javaPkg.getAnnotations();
      Assert.assertEquals(2, annotations.size());
      Annotation<JavaPackageInfo> annotation = javaPkg.getAnnotation("XmlSchema");
      List<ValuePair> values = annotation.getValues();
      Assert.assertEquals(3, values.size());
      String namespace = annotation.getLiteralValue("namespace");
      Assert.assertEquals(namespace, "\"http://forge.org/Test\"");
      
      Annotation<JavaPackageInfo> annotationXmlOrder = javaPkg.addAnnotation("javax.xml.bind.annotation.XmlAccessorOrder");
      Annotation<JavaPackageInfo> annotationXmlAccessorOrder = javaPkg.getAnnotation("XmlAccessorOrder");
      Assert.assertEquals(annotationXmlOrder.getName(), annotationXmlAccessorOrder.getName());
      
   }
}
