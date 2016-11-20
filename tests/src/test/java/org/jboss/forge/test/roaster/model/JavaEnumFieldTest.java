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

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumFieldTest
{
   private JavaEnumSource javaEnum;
   private FieldSource<JavaEnumSource> field;

   @Before
   public void reset()
   {
      InputStream stream = JavaEnumFieldTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockEnum.java");
      javaEnum = Roaster.parse(JavaEnumSource.class, stream);
      field = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
   }

   @Test
   public void testParse() throws Exception
   {
      assertNotNull(field);
      assertEquals("field", field.getName());
      assertEquals("String", field.getType().getName());
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
      FieldSource<JavaEnumSource> field = javaEnum.addField().setType(JavaEnumFieldTest.class).setPublic()
               .setName("test");
      assertTrue(field.getType().isType(JavaEnumFieldTest.class));
      assertTrue(field.getType().isType(JavaEnumFieldTest.class.getName()));
      assertTrue(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsIgnoresJavaLang() throws Exception
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField("private Boolean bar;").setPublic().setName("test");
      assertTrue(field.getType().isType(Boolean.class));
      assertTrue(field.getType().isType("Boolean"));
      assertTrue(field.getType().isType(Boolean.class.getName()));
      assertFalse(javaEnum.hasImport(Boolean.class));
   }

   @Test
   public void testIsTypeStringChecksImports() throws Exception
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField().setType(JavaEnumFieldTest.class.getName()).setPublic()
               .setName("test");
      assertTrue(field.getType().isType(JavaEnumFieldTest.class.getSimpleName()));
      assertTrue(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsTypes() throws Exception
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField("private org.jboss.JavaEnumFieldTest test;");
      FieldSource<JavaEnumSource> field2 = javaEnum.addField().setType(JavaEnumFieldTest.class).setName("test2")
               .setPrivate();

      assertTrue(field.getType().isType(JavaEnumFieldTest.class.getSimpleName()));
      assertFalse(field.getType().isType(JavaEnumFieldTest.class));
      assertTrue(field.getType().isType("org.jboss.JavaEnumFieldTest"));

      assertTrue(field2.getType().isType(JavaEnumFieldTest.class.getSimpleName()));
      assertTrue(field2.getType().isType(JavaEnumFieldTest.class));
      assertFalse(field2.getType().isType("org.jboss.JavaEnumFieldTest"));
   }

   @Test
   public void testSetTypeSimpleNameDoesNotAddImport() throws Exception
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField().setType(JavaEnumFieldTest.class.getSimpleName())
               .setPublic()
               .setName("test");
      assertFalse(field.getType().isType(JavaEnumFieldTest.class));
      assertFalse(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testSetType() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType(JavaEnumFieldTest.class);
      field.getOrigin();
      assertTrue(field.toString().contains("JavaEnumFieldTest"));
      assertEquals(JavaEnumFieldTest.class.getName(), field.getType().getQualifiedName());
   }

   @Test
   public void testSetTypeStringIntPrimitive() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType("int");
      field.getOrigin();
      assertTrue(field.toString().contains("int"));
      assertEquals("int", field.getType().getName());
   }

   @Test
   public void testSetTypeClassIntPrimitive() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType(int.class.getName());
      field.getOrigin();
      assertTrue(field.toString().contains("int"));
      assertEquals("int", field.getType().getName());
   }

   @Test
   public void testSetTypeString() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType("FooBarType");
      field.getOrigin();
      assertTrue(field.toString().contains("FooBarType"));
      assertEquals("FooBarType", field.getType().getName());
   }

   @Test
   public void testAddField() throws Exception
   {
      javaEnum.addField("public Boolean flag = false;");
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      fld.getOrigin();

      assertTrue(fld.toString().contains("Boolean"));
      assertEquals("java.lang.Boolean", fld.getType().getQualifiedName());
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
      FieldSource<JavaEnumSource> objectField = javaEnum.addField("public Boolean flag = false;");
      FieldSource<JavaEnumSource> primitiveField = javaEnum.addField("public boolean flag = false;");

      assertFalse(objectField.getType().isPrimitive());
      assertTrue(primitiveField.getType().isPrimitive());

   }

   @Test
   public void testAddFieldInitializerLiteral() throws Exception
   {
      javaEnum.addField("public int flag;").setLiteralInitializer("1234").setPrivate();
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);

      assertEquals("int", fld.getType().getName());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerLiteralIgnoresTerminator() throws Exception
   {
      javaEnum.addField("public int flag;").setLiteralInitializer("1234;").setPrivate();
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);

      assertEquals("int", fld.getType().getName());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerString() throws Exception
   {
      javaEnum.addField("public String flag;").setStringInitializer("american");
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      fld.getOrigin();

      assertEquals("String", fld.getType().getName());
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
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      fld.getOrigin();

      assertEquals(String.class.getName(), fld.getType().getQualifiedName());
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
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      assertTrue(javaEnum.hasField(fld));

      FieldSource<JavaEnumSource> notFld = Roaster.parse(JavaEnumSource.class, "public enum Foo { BAR(-1); }")
               .addField("private int foobar;");
      assertFalse(javaEnum.hasField(notFld));

   }
}
