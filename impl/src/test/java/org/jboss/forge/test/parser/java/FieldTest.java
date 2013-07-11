/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldTest
{
   private InputStream stream;
   private JavaClass javaClass;
   private Field<JavaClass> field;

   @Before
   public void reset()
   {
      stream = FieldTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedField.java");
      javaClass = JavaParser.parse(JavaClass.class, stream);
      field = javaClass.getFields().get(javaClass.getFields().size() - 1);
   }

   @Test
   public void testParse() throws Exception
   {
      assertNotNull(field);
      assertEquals("field", field.getName());
      assertEquals("String", field.getType());
   }

   @Test
   public void testSetName() throws Exception
   {
      assertEquals("field", field.getName());
      field.setName("newName");
      field.getOrigin();
      assertTrue(field.toString().contains("newName;"));
      assertEquals("newName", field.getName());
   }

   @Test
   public void testSetNameWithReservedWordPart() throws Exception
   {
      assertEquals("field", field.getName());
      field.setName("privateIpAddress");
      assertTrue(javaClass.hasField("privateIpAddress"));
   }

   @Test
   public void testIsTypeChecksImports() throws Exception
   {
      Field<JavaClass> field = javaClass.addField().setType(FieldTest.class).setPublic().setName("test");
      assertTrue(field.isType(FieldTest.class));
      assertTrue(field.isType(FieldTest.class.getName()));
      assertTrue(javaClass.hasImport(FieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsIgnoresJavaLang() throws Exception
   {
      Field<JavaClass> field = javaClass.addField("private Boolean bar;").setPublic().setName("test");
      assertTrue(field.isType(Boolean.class));
      assertTrue(field.isType("Boolean"));
      assertTrue(field.isType(Boolean.class.getName()));
      assertFalse(javaClass.hasImport(Boolean.class));
   }

   @Test
   public void testIsTypeStringChecksImports() throws Exception
   {
      Field<JavaClass> field = javaClass.addField().setType(FieldTest.class.getName()).setPublic().setName("test");
      assertTrue(field.isType(FieldTest.class.getSimpleName()));
      assertTrue(javaClass.hasImport(FieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsTypes() throws Exception
   {
      Field<JavaClass> field = javaClass.addField("private org.jboss.FieldTest test;");
      Field<JavaClass> field2 = javaClass.addField().setType(FieldTest.class).setName("test2").setPrivate();

      assertTrue(field.isType(FieldTest.class.getSimpleName()));
      assertFalse(field.isType(FieldTest.class));
      assertTrue(field.isType("org.jboss.FieldTest"));

      assertTrue(field2.isType(FieldTest.class.getSimpleName()));
      assertTrue(field2.isType(FieldTest.class));
      assertFalse(field2.isType("org.jboss.FieldTest"));
   }

   @Test
   public void testSetTypeSimpleNameDoesNotAddImport() throws Exception
   {
      Field<JavaClass> field = javaClass.addField().setType(FieldTest.class.getSimpleName()).setPublic()
               .setName("test");
      assertFalse(field.isType(FieldTest.class));
      assertFalse(javaClass.hasImport(FieldTest.class));
   }

   @Test
   public void testSetType() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType(FieldTest.class);
      field.getOrigin();
      assertTrue(field.toString().contains("FieldTest"));
      assertEquals(FieldTest.class.getName(), field.getQualifiedType());
   }

   @Test
   public void testSetTypeStringIntPrimitive() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType("int");
      field.getOrigin();
      assertTrue(field.toString().contains("int"));
      assertEquals("int", field.getType());
   }

   @Test
   public void testSetTypeClassIntPrimitive() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType(int.class.getName());
      field.getOrigin();
      assertTrue(field.toString().contains("int"));
      assertEquals("int", field.getType());
   }

   @Test
   public void testSetTypeString() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType("FooBarType");
      field.getOrigin();
      assertTrue(field.toString().contains("FooBarType"));
      assertEquals("FooBarType", field.getType());
   }

   @Test
   public void testAddField() throws Exception
   {
      javaClass.addField("public Boolean flag = false;");
      Field<JavaClass> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.getOrigin();

      assertTrue(fld.toString().contains("Boolean"));
      assertEquals("java.lang.Boolean", fld.getQualifiedType());
      assertEquals("flag", fld.getName());
      assertEquals("false", fld.getLiteralInitializer());
   }

   @Test
   public void testAddFieldWithVisibilityScope() throws Exception
   {
      javaClass.addField("private String privateIpAddress;");
      assertTrue(javaClass.hasField("privateIpAddress"));
   }

   @Test
   public void testIsPrimitive() throws Exception
   {
      Field<JavaClass> objectField = javaClass.addField("public Boolean flag = false;");
      Field<JavaClass> primitiveField = javaClass.addField("public boolean flag = false;");

      assertFalse(objectField.isPrimitive());
      assertTrue(primitiveField.isPrimitive());
   }
   
   @Test
   public void testIsTransient() throws Exception
   {
      Field<JavaClass> transientField = javaClass.addField("public transient boolean flag = false;");
      Field<JavaClass> nonTransientField = javaClass.addField("public boolean flag = false;");

      assertTrue(transientField.isTransient());
      assertFalse(nonTransientField.isTransient());
   }
   
   @Test
   public void testIsVolatile() throws Exception
   {
      Field<JavaClass> volatileField = javaClass.addField("public volatile boolean flag = false;");
      Field<JavaClass> nonVolatileField = javaClass.addField("public boolean flag = false;");

      assertTrue(volatileField.isVolatile());
      assertFalse(nonVolatileField.isVolatile());
   }
   
   @Test
   public void testAddFieldInitializerLiteral() throws Exception
   {
      javaClass.addField("public int flag;").setLiteralInitializer("1234").setPrivate();
      Field<JavaClass> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);

      assertEquals("int", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerLiteralIgnoresTerminator() throws Exception
   {
      javaClass.addField("public int flag;").setLiteralInitializer("1234;").setPrivate();
      Field<JavaClass> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);

      assertEquals("int", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerString() throws Exception
   {
      javaClass.addField("public String flag;").setStringInitializer("american");
      Field<JavaClass> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.getOrigin();

      assertEquals("String", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("public String flag=\"american\";", fld.toString().trim());
   }

   @Test
   public void testAddQualifiedFieldType() throws Exception
   {
      javaClass.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american")
               .setPrivate();
      Field<JavaClass> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.getOrigin();

      assertEquals(String.class.getName(), fld.getQualifiedType());
      assertFalse(javaClass.hasImport(String.class));
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("private String flag=\"american\";", fld.toString().trim());
   }

   @Test
   public void testHasField() throws Exception
   {
      javaClass.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american")
               .setPrivate();
      Field<JavaClass> fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      assertTrue(javaClass.hasField(fld));

      Field<JavaClass> notFld = JavaParser.parse(JavaClass.class, "public class Foo {}")
               .addField("private int foobar;");
      assertFalse(javaClass.hasField(notFld));

   }
}
