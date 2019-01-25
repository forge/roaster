/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldTest
{
   private JavaClassSource javaClass;
   private FieldSource<JavaClassSource> field;

   @Before
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockAnnotatedField.java";
      try (InputStream stream = FieldTest.class.getResourceAsStream(fileName))
      {
         javaClass = Roaster.parse(JavaClassSource.class, stream);
         field = javaClass.getFields().get(javaClass.getFields().size() - 1);
      }
   }

   @Test
   public void testParse()
   {
      assertNotNull(field);
      assertEquals("field", field.getName());
      assertEquals("String", field.getType().getName());
   }

   @Test
   public void testSetName()
   {
      assertEquals("field", field.getName());
      field.setName("newName");
      field.getOrigin();
      assertTrue(field.toString().contains("newName;"));
      assertEquals("newName", field.getName());
   }

   @Test
   public void testSetNameWithReservedWordPart()
   {
      assertEquals("field", field.getName());
      field.setName("privateIpAddress");
      assertTrue(javaClass.hasField("privateIpAddress"));
   }

   @Test
   public void testIsTypeChecksImports()
   {
      FieldSource<JavaClassSource> field = javaClass.addField().setType(FieldTest.class).setPublic().setName("test");
      assertTrue(field.getType().isType(FieldTest.class));
      assertTrue(field.getType().isType(FieldTest.class.getName()));
      assertTrue(javaClass.hasImport(FieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsIgnoresJavaLang()
   {
      FieldSource<JavaClassSource> field = javaClass.addField("private Boolean bar;").setPublic().setName("test");
      assertTrue(field.getType().isType(Boolean.class));
      assertTrue(field.getType().isType("Boolean"));
      assertTrue(field.getType().isType(Boolean.class.getName()));
      assertFalse(javaClass.hasImport(Boolean.class));
   }

   @Test
   public void testIsTypeStringChecksImports()
   {
      FieldSource<JavaClassSource> field = javaClass.addField().setType(FieldTest.class.getName()).setPublic()
               .setName("test");
      assertTrue(field.getType().isType(FieldTest.class.getSimpleName()));
      assertTrue(javaClass.hasImport(FieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsTypes()
   {
      FieldSource<JavaClassSource> field = javaClass.addField("private org.jboss.FieldTest test;");
      FieldSource<JavaClassSource> field2 = javaClass.addField().setType(FieldTest.class).setName("test2").setPrivate();

      assertTrue(field.getType().isType(FieldTest.class.getSimpleName()));
      assertFalse(field.getType().isType(FieldTest.class));
      assertTrue(field.getType().isType("org.jboss.FieldTest"));

      assertTrue(field2.getType().isType(FieldTest.class.getSimpleName()));
      assertTrue(field2.getType().isType(FieldTest.class));
      assertFalse(field2.getType().isType("org.jboss.FieldTest"));
   }

   @Test
   public void testSetTypeSimpleNameDoesNotAddImport()
   {
      FieldSource<JavaClassSource> field = javaClass.addField().setType(FieldTest.class.getSimpleName()).setPublic()
               .setName("test");
      assertFalse(field.getType().isType(FieldTest.class));
      assertFalse(javaClass.hasImport(FieldTest.class));
   }

   @Test
   public void testSetType()
   {
      assertEquals("field", field.getName());
      field.setType(FieldTest.class);
      field.getOrigin();
      assertTrue(field.toString().contains("FieldTest"));
      assertEquals(FieldTest.class.getName(), field.getType().getQualifiedName());
   }

   @Test
   public void testSetTypeStringIntPrimitive()
   {
      assertEquals("field", field.getName());
      field.setType("int");
      field.getOrigin();
      assertTrue(field.toString().contains("int"));
      assertEquals("int", field.getType().getName());
   }

   @Test
   public void testSetTypeClassIntPrimitive()
   {
      assertEquals("field", field.getName());
      field.setType(int.class.getName());
      field.getOrigin();
      assertTrue(field.toString().contains("int"));
      assertEquals("int", field.getType().getName());
   }

   @Test
   public void testSetTypeString()
   {
      assertEquals("field", field.getName());
      field.setType("FooBarType");
      field.getOrigin();
      assertTrue(field.toString().contains("FooBarType"));
      assertEquals("FooBarType", field.getType().getName());
   }

   @Test
   public void testAddField()
   {
      javaClass.addField("public Boolean flag = false;");
      FieldSource<JavaClassSource> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.getOrigin();

      assertTrue(fld.toString().contains("Boolean"));
      assertEquals("java.lang.Boolean", fld.getType().getQualifiedName());
      assertEquals("flag", fld.getName());
      assertEquals("false", fld.getLiteralInitializer());
   }

   @Test
   public void testAddFieldWithVisibilityScope()
   {
      javaClass.addField("private String privateIpAddress;");
      assertTrue(javaClass.hasField("privateIpAddress"));
   }

   @Test
   public void testIsPrimitive()
   {
      FieldSource<JavaClassSource> objectField = javaClass.addField("public Boolean flag = false;");
      FieldSource<JavaClassSource> primitiveField = javaClass.addField("public boolean flag = false;");

      assertFalse(objectField.getType().isPrimitive());
      assertTrue(primitiveField.getType().isPrimitive());
   }

   @Test
   public void testTransient()
   {
      FieldSource<JavaClassSource> transientField = javaClass.addField("public transient boolean flag = false;");
      FieldSource<JavaClassSource> nonTransientField = javaClass.addField("public boolean flag = false;");

      assertTrue(transientField.isTransient());
      assertFalse(nonTransientField.isTransient());

      transientField.setTransient(false);
      nonTransientField.setTransient(true);

      assertFalse(transientField.isTransient());
      assertTrue(nonTransientField.isTransient());
   }

   @Test
   public void testVolatile()
   {
      FieldSource<JavaClassSource> volatileField = javaClass.addField("public volatile boolean flag = false;");
      FieldSource<JavaClassSource> nonVolatileField = javaClass.addField("public boolean flag = false;");

      assertTrue(volatileField.isVolatile());
      assertFalse(nonVolatileField.isVolatile());

      volatileField.setVolatile(false);
      nonVolatileField.setVolatile(true);

      assertFalse(volatileField.isVolatile());
      assertTrue(nonVolatileField.isVolatile());
   }

   @Test
   public void testAddFieldInitializerLiteral()
   {
      javaClass.addField("public int flag;").setLiteralInitializer("1234").setPrivate();
      FieldSource<JavaClassSource> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);

      assertEquals("int", fld.getType().getName());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerLiteralIgnoresTerminator()
   {
      javaClass.addField("public int flag;").setLiteralInitializer("1234;").setPrivate();
      FieldSource<JavaClassSource> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);

      assertEquals("int", fld.getType().getName());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerString()
   {
      javaClass.addField("public String flag;").setStringInitializer("american");
      FieldSource<JavaClassSource> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.getOrigin();

      assertEquals("String", fld.getType().getName());
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("public String flag=\"american\";", fld.toString().trim());
   }

   @Test
   public void testAddQualifiedFieldType()
   {
      javaClass.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american")
               .setPrivate();
      FieldSource<JavaClassSource> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.getOrigin();

      assertEquals(String.class.getName(), fld.getType().getQualifiedName());
      assertFalse(javaClass.hasImport(String.class));
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("private String flag=\"american\";", fld.toString().trim());
   }

   @Test
   public void testHasField()
   {
      javaClass.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american")
               .setPrivate();
      FieldSource<JavaClassSource> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      assertTrue(javaClass.hasField(fld));

      FieldSource<JavaClassSource> notFld = Roaster.parse(JavaClassSource.class, "public class Foo {}")
               .addField("private int foobar;");
      assertFalse(javaClass.hasField(notFld));
   }

   @Test
   public void testGenericField()
   {
      javaClass.addImport(HashMap.class);
      javaClass.addField()
               .setFinal(true)
               .setName("AS_MAP")
               .setPrivate()
               .setStatic(true)
               .setType("HashMap<Class<?>,Object>")
               .setLiteralInitializer("new HashMap<>();");
      FieldSource<JavaClassSource> mapField = javaClass.getField("AS_MAP");
      Assert.assertNotNull(mapField);
      Assert.assertEquals("HashMap<Class<?>,Object>", mapField.getType().toString());
   }

}
