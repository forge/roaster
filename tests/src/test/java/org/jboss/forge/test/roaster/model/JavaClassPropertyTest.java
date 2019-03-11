/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaClassPropertyTest
{
   @Test
   public void testProperties() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockUnformattedClass.java";
      try (InputStream stream = JavaClassTest.class.getResourceAsStream(fileName))
      {
         JavaClassSource source = Roaster.parse(JavaClassSource.class, stream);
         List<PropertySource<JavaClassSource>> properties = source.getProperties();
         assertEquals(2, properties.size());
      }
   }

   @Test
   public void testPrimitiveBooleanProperties() throws IOException
   {
      JavaClassSource source;
      String fileName = "/org/jboss/forge/grammar/java/BooleanPrimitiveClass.java";
      try (InputStream stream = JavaClassTest.class.getResourceAsStream(fileName))
      {
         source = Roaster.parse(JavaClassSource.class, stream);
      }

      List<PropertySource<JavaClassSource>> properties = source.getProperties();
      assertEquals(2, properties.size());
      assertNotNull(source.getProperty("myString").getAccessor());
      assertNotNull(source.getProperty("myString").getMutator());
      assertNotNull(source.getProperty("myBoolean").getAccessor());
      assertNotNull(source.getProperty("myBoolean").getMutator());
   }

   @Test
   public void testBooleanProperties() throws IOException
   {
      JavaClassSource source;
      String fileName = "/org/jboss/forge/grammar/java/BooleanClass.java";
      try (InputStream stream = JavaClassTest.class.getResourceAsStream(fileName))
      {
         source = Roaster.parse(JavaClassSource.class, stream);
      }

      List<PropertySource<JavaClassSource>> properties = source.getProperties();
      assertEquals(2, properties.size());
      assertNotNull(source.getProperty("myString").getAccessor());
      assertNotNull(source.getProperty("myString").getMutator());
      assertNotNull(source.getProperty("myBoolean").getAccessor());
      assertNotNull(source.getProperty("myBoolean").getMutator());
   }

   @Test
   public void testChangePropertyType()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class).setName("MyClass");
      PropertySource<JavaClassSource> property = source.addProperty(Date.class, "myDate");
      property.setType(Timestamp.class);
      assertEquals("Timestamp", source.getField("myDate").getType().getName());
      assertEquals("Timestamp", source.getMethod("getMyDate").getReturnType().getName());
      assertNotNull(source.getMethod("setMyDate", Timestamp.class));
   }

   @Test
   public void testFQNTypes()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class).setName("MyClass");
      source.addProperty("java.util.List<java.lang.String>", "list1");
      source.addProperty("java.util.List<String>", "list2");
      source.addProperty("java.util.List<java.util.List<String>>", "list3");
      assertTrue(source.hasProperty("list1"));
      assertTrue(source.hasProperty("list2"));
      assertTrue(source.hasProperty("list3"));
   }

   @Test
   public void testPrimitiveArrayProperty()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class).setName("MyClass");
      source.addProperty(byte[].class, "blob");
      assertTrue(source.hasProperty("blob"));
   }

   @Test
   public void testGetPropertiesByClass()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class).setName("MyClass");
      source.addProperty(Date.class, "myDate");
      source.addProperty(List.class, "myList");

      List<PropertySource<JavaClassSource>> properties = source.getProperties(List.class);
      assertEquals(1, properties.size(), "Incorrect number of properties found.");
      assertEquals(List.class.getCanonicalName(),
               properties.get(0).getType().getQualifiedName(), "Wrong property returned.");
   }
}
