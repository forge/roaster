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
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockEnum.java";
      try (InputStream stream = JavaEnumFieldTest.class.getResourceAsStream(fileName))
      {
         javaEnum = Roaster.parse(JavaEnumSource.class, stream);
         field = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
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
      assertTrue(javaEnum.hasField("privateIpAddress"));
   }

   @Test
   public void testIsTypeChecksImports()
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField().setType(JavaEnumFieldTest.class).setPublic()
               .setName("test");
      assertTrue(field.getType().isType(JavaEnumFieldTest.class));
      assertTrue(field.getType().isType(JavaEnumFieldTest.class.getName()));
      assertTrue(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsIgnoresJavaLang()
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField("private Boolean bar;").setPublic().setName("test");
      assertTrue(field.getType().isType(Boolean.class));
      assertTrue(field.getType().isType("Boolean"));
      assertTrue(field.getType().isType(Boolean.class.getName()));
      assertFalse(javaEnum.hasImport(Boolean.class));
   }

   @Test
   public void testIsTypeStringChecksImports()
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField().setType(JavaEnumFieldTest.class.getName()).setPublic()
               .setName("test");
      assertTrue(field.getType().isType(JavaEnumFieldTest.class.getSimpleName()));
      assertTrue(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testIsTypeChecksImportsTypes()
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
   public void testSetTypeSimpleNameDoesNotAddImport()
   {
      FieldSource<JavaEnumSource> field = javaEnum.addField().setType(JavaEnumFieldTest.class.getSimpleName())
               .setPublic()
               .setName("test");
      assertFalse(field.getType().isType(JavaEnumFieldTest.class));
      assertFalse(javaEnum.hasImport(JavaEnumFieldTest.class));
   }

   @Test
   public void testSetType()
   {
      assertEquals("field", field.getName());
      field.setType(JavaEnumFieldTest.class);
      field.getOrigin();
      assertTrue(field.toString().contains("JavaEnumFieldTest"));
      assertEquals(JavaEnumFieldTest.class.getName(), field.getType().getQualifiedName());
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
      javaEnum.addField("public Boolean flag = false;");
      FieldSource<JavaEnumSource> fld = javaEnum.getFields().get(javaEnum.getFields().size() - 1);
      fld.getOrigin();

      assertTrue(fld.toString().contains("Boolean"));
      assertEquals("java.lang.Boolean", fld.getType().getQualifiedName());
      assertEquals("flag", fld.getName());
      assertEquals("false", fld.getLiteralInitializer());
   }

   @Test
   public void testAddFieldWithVisibilityScope()
   {
      javaEnum.addField("private String privateIpAddress;");
      assertTrue(javaEnum.hasField("privateIpAddress"));
   }

   @Test
   public void testIsPrimitive()
   {
      FieldSource<JavaEnumSource> objectField = javaEnum.addField("public Boolean flag = false;");
      FieldSource<JavaEnumSource> primitiveField = javaEnum.addField("public boolean flag = false;");

      assertFalse(objectField.getType().isPrimitive());
      assertTrue(primitiveField.getType().isPrimitive());

   }

   @Test
   public void testAddFieldInitializerLiteral()
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
   public void testAddFieldInitializerLiteralIgnoresTerminator()
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
   public void testAddFieldInitializerString()
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
   public void testAddQualifiedFieldType()
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
   public void testHasField()
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