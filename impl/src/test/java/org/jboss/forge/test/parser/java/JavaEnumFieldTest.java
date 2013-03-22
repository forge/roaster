/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaEnum;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumFieldTest
{
   private InputStream stream;
   private JavaEnum javaEnum;
   private Field<JavaEnum> field;

   @Before
   public void reset()
   {
      stream = JavaEnumFieldTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockEnum.java");
      javaEnum = JavaParser.parse(JavaEnum.class, stream);
      field = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
   }

   @Test
   public void testParse() throws Exception
   {
      assertTrue(field instanceof Field);
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
      assertTrue(javaEnum.hasField("privateIpAddress"));
   }

   @Test
   public void testIsTypeChecksImports() throws Exception
   {
      Field<JavaEnum> field = javaEnum.addField().setType(JavaEnumFieldTest.class).setPublic().setName("test");
      assertTrue(field.isType(JavaEnumFieldTest.class));
      assertTrue(field.isType(JavaEnumFieldTest.class.getName()));
      assertTrue(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsIgnoresJavaLang() throws Exception
   {
      Field<JavaEnum> field = javaEnum.addField("private Boolean bar;").setPublic().setName("test");
      assertTrue(field.isType(Boolean.class));
      assertTrue(field.isType("Boolean"));
      assertTrue(field.isType(Boolean.class.getName()));
      assertFalse(javaEnum.hasImport(Boolean.class));
   }

   @Test
   public void testIsTypeStringChecksImports() throws Exception
   {
      Field<JavaEnum> field = javaEnum.addField().setType(JavaEnumFieldTest.class.getName()).setPublic().setName("test");
      assertTrue(field.isType(JavaEnumFieldTest.class.getSimpleName()));
      assertTrue(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsTypes() throws Exception
   {
      Field<JavaEnum> field = javaEnum.addField("private org.jboss.JavaEnumFieldTest test;");
      Field<JavaEnum> field2 = javaEnum.addField().setType(JavaEnumFieldTest.class).setName("test2").setPrivate();

      assertTrue(field.isType(JavaEnumFieldTest.class.getSimpleName()));
      assertFalse(field.isType(JavaEnumFieldTest.class));
      assertTrue(field.isType("org.jboss.JavaEnumFieldTest"));

      assertTrue(field2.isType(JavaEnumFieldTest.class.getSimpleName()));
      assertTrue(field2.isType(JavaEnumFieldTest.class));
      assertFalse(field2.isType("org.jboss.JavaEnumFieldTest"));
   }

   @Test
   public void testSetTypeSimpleNameDoesNotAddImport() throws Exception
   {
      Field<JavaEnum> field = javaEnum.addField().setType(JavaEnumFieldTest.class.getSimpleName()).setPublic()
               .setName("test");
      assertFalse(field.isType(JavaEnumFieldTest.class));
      assertFalse(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testSetType() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType(JavaEnumFieldTest.class);
      field.getOrigin();
      assertTrue(field.toString().contains("JavaEnumFieldTest"));
      assertEquals(JavaEnumFieldTest.class.getName(), field.getQualifiedType());
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
      javaEnum.addField("public Boolean flag = false;");
      Field<JavaEnum> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      fld.getOrigin();

      assertTrue(fld.toString().contains("Boolean"));
      assertEquals("java.lang.Boolean", fld.getQualifiedType());
      assertEquals("flag", fld.getName());
      assertEquals("false", fld.getLiteralInitializer());
   }

   @Test
   public void testAddFieldWithVisibilityScope() throws Exception
   {
      javaEnum.addField("private String privateIpAddress;");
      assertTrue(javaEnum.hasField("privateIpAddress"));
   }

   @Test
   public void testIsPrimitive() throws Exception
   {
      Field<JavaEnum> objectField = javaEnum.addField("public Boolean flag = false;");
      Field<JavaEnum> primitiveField = javaEnum.addField("public boolean flag = false;");

      assertFalse(objectField.isPrimitive());
      assertTrue(primitiveField.isPrimitive());

   }

   @Test
   public void testAddFieldInitializerLiteral() throws Exception
   {
      javaEnum.addField("public int flag;").setLiteralInitializer("1234").setPrivate();
      Field<JavaEnum> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);

      assertEquals("int", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerLiteralIgnoresTerminator() throws Exception
   {
      javaEnum.addField("public int flag;").setLiteralInitializer("1234;").setPrivate();
      Field<JavaEnum> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);

      assertEquals("int", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerString() throws Exception
   {
      javaEnum.addField("public String flag;").setStringInitializer("american");
      Field<JavaEnum> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
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
      javaEnum.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american")
               .setPrivate();
      Field<JavaEnum> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      fld.getOrigin();

      assertEquals(String.class.getName(), fld.getQualifiedType());
      assertFalse(javaEnum.hasImport(String.class));
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("private String flag=\"american\";", fld.toString().trim());
   }

   @Test
   public void testHasField() throws Exception
   {
      javaEnum.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american")
               .setPrivate();
      Field<JavaEnum> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      assertTrue(javaEnum.hasField(fld));

      Field<JavaEnum> notFld = JavaParser.parse(JavaEnum.class, "public enum Foo { BAR(-1); }")
               .addField("private int foobar;");
      assertFalse(javaEnum.hasField(notFld));

   }
}
