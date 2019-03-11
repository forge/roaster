/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.ValuePair;
import org.jboss.forge.roaster.model.impl.TypeImpl;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaPackageInfoSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaPackageInfoTest
{

   @Test
   public void testCanCreatePackageInfo()
   {
      JavaPackageInfoSource packageInfo = Roaster.create(JavaPackageInfoSource.class);
      packageInfo.setPackage("org.jboss.forge.roaster");
      assertEquals("org.jboss.forge.roaster", packageInfo.getPackage());
      assertEquals("package-info", packageInfo.getName());
   }

   @Test
   public void testLocation()
   {
      JavaPackageInfoSource packageInfo = Roaster.create(JavaPackageInfoSource.class);
      packageInfo.setPackage("abc");
      assertEquals(0, packageInfo.getStartPosition());
      assertEquals(20, packageInfo.getEndPosition()); // TODO check this
      assertEquals(0, packageInfo.getColumnNumber());
      assertEquals(1, packageInfo.getLineNumber());
   }

   @Test
   public void testSetPackageInfoNameThrowsUnsupportedOperation()
   {
      JavaPackageInfoSource packageInfo = Roaster.create(JavaPackageInfoSource.class);
      assertThrows(UnsupportedOperationException.class, () -> packageInfo.setName("anything"));
   }

   @Test
   public void testCanParsePackageInfo() throws IOException
   {
      JavaPackageInfoSource javaPkg;
      String fileName = "/org/jboss/forge/grammar/java/package-info.java";
      try (InputStream stream = JavaPackageInfoTest.class.getResourceAsStream(fileName))
      {
         javaPkg = Roaster.parse(JavaPackageInfoSource.class, stream);
      }

      assertEquals("org.jboss.forge.test.roaster.model", javaPkg.getPackage());
      assertEquals("package-info", javaPkg.getName());
      assertNotNull(javaPkg.getImport("javax.xml.bind.annotation.XmlSchema"));
      Import XmlAccessTypeField = javaPkg.getImport("javax.xml.bind.annotation.XmlAccessType.FIELD");
      assertNotNull(XmlAccessTypeField);
      assertTrue(XmlAccessTypeField.isStatic());
      List<AnnotationSource<JavaPackageInfoSource>> annotations = javaPkg.getAnnotations();
      assertEquals(2, annotations.size());
      Annotation<JavaPackageInfoSource> annotation = javaPkg.getAnnotation("XmlSchema");
      List<ValuePair> values = annotation.getValues();
      assertEquals(3, values.size());
      String namespace = annotation.getLiteralValue("namespace");
      assertEquals(namespace, "\"http://forge.org/Test\"");

      AnnotationSource<JavaPackageInfoSource> annotationXmlOrder = javaPkg
               .addAnnotation("javax.xml.bind.annotation.XmlAccessorOrder");
      AnnotationSource<JavaPackageInfoSource> annotationXmlAccessorOrder = javaPkg.getAnnotation("XmlAccessorOrder");
      assertEquals(annotationXmlOrder.getName(), annotationXmlAccessorOrder.getName());
   }

   @Test
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public void testPackageInfoWithImportedAnnotations()
   {
      JavaPackageInfoSource packageInfo = Roaster.create(JavaPackageInfoSource.class);
      packageInfo.setPackage("org.jboss.forge.roaster");
      packageInfo.addImport(new TypeImpl(packageInfo, null, MyPLAnnotation.class.getName()));
      packageInfo.addAnnotation("my.custom.Annotation");
      packageInfo.getEnclosingType();

      assertEquals(2, packageInfo.getImports().size());
   }

   public @interface MyPLAnnotation
   {
      // empty for testing
   }
}