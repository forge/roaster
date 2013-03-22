/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.util.List;
import java.util.Map;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Type;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldTypeTest
{
   @Test
   public void testGetReturnTypeReturnsFullTypeForJavaLang() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class).addField("public Long l;");
      Assert.assertEquals("java.lang.Long", field.getQualifiedType());
      Assert.assertEquals("Long", field.getType());
   }

   @Test
   public void testGetReturnTypeReturnsFullTypeForJavaLangGeneric() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List<Long> list;");
      field.getOrigin().addImport(List.class);
      Assert.assertEquals("java.util.List", field.getQualifiedType());
      Assert.assertEquals("List", field.getType());
   }

   @Test
   public void testGetReturnTypeObjectArray() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List[] field;");
      field.getOrigin().addImport(List.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertFalse(type.isParameterized());
      Assert.assertFalse(type.isWildcard());
      Assert.assertFalse(type.isPrimitive());
      Assert.assertFalse(type.isQualified());
      Assert.assertTrue(type.isArray());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(0, arguments.size());
   }

   @Test
   public void testGetReturnTypeObjectArrayParameterized() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List<Long>[] list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());
      Assert.assertFalse(type.isWildcard());
      Assert.assertFalse(type.isPrimitive());
      Assert.assertFalse(type.isQualified());
      Assert.assertTrue(type.isArray());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
   }

   @Test
   public void testGetReturnTypeObjectUnparameterized() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List list;");
      field.getOrigin().addImport(List.class);
      Assert.assertEquals("java.util.List", field.getTypeInspector().getQualifiedName());
      Assert.assertFalse(field.getTypeInspector().isParameterized());
   }

   @Test
   public void testGetReturnTypeObjectParameterized() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List<Long> list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("Long", arguments.get(0).getName());
      Assert.assertEquals("java.lang.Long", arguments.get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectWildcard() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List<?> list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("?", arguments.get(0).getName());
      Assert.assertEquals("?", arguments.get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedMultiple() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public Map<String, Long> map;");
      field.getOrigin().addImport(Map.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.Map", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(2, arguments.size());
      Assert.assertEquals("String", arguments.get(0).getName());
      Assert.assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      Assert.assertEquals("Long", arguments.get(1).getName());
      Assert.assertEquals("java.lang.Long", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedNested() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public List<List<Long>> map;");
      field.getOrigin().addImport(List.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("List", arguments.get(0).getName());
      Assert.assertEquals("java.util.List", arguments.get(0).getQualifiedName());

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("Long", arguments.get(0).getTypeArguments().get(0).getName());
      Assert.assertEquals("java.lang.Long", arguments.get(0).getTypeArguments().get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedMultipleNested() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public Map<String, List<Long>> map;");
      field.getOrigin().addImport(List.class);
      field.getOrigin().addImport(Map.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.Map", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(2, arguments.size());
      Assert.assertEquals("String", arguments.get(0).getName());
      Assert.assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      Assert.assertEquals("List", arguments.get(1).getName());
      Assert.assertEquals("java.util.List", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedArrayMultipleNested() throws Exception
   {
      Field<JavaClass> field = JavaParser.create(JavaClass.class)
               .addField("public Map<String, List<Long>>[] maps;");
      field.getOrigin().addImport(List.class);
      field.getOrigin().addImport(Map.class);
      Type<JavaClass> type = field.getTypeInspector();
      Assert.assertEquals("java.util.Map", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClass>> arguments = type.getTypeArguments();

      Assert.assertEquals(2, arguments.size());
      Assert.assertEquals("String", arguments.get(0).getName());
      Assert.assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      Assert.assertEquals("List", arguments.get(1).getName());
      Assert.assertEquals("java.util.List", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testFieldTypeByteArrayTest()
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      final Field<JavaClass> field = javaClass.addField();
      field.setName("content");
      field.setType(byte[].class);
      Assert.assertEquals("byte", field.getQualifiedType());
      Assert.assertTrue(field.getTypeInspector().isArray());
   }

   @Test
   public void testFieldMultidimensionalArray()
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      final Field<JavaClass> field = javaClass.addField();
      field.setName("content");
      field.setType(byte[][][].class);
      Assert.assertEquals("byte", field.getQualifiedType());
      Type<JavaClass> typeInspector = field.getTypeInspector();
      Assert.assertTrue(typeInspector.isArray());
      Assert.assertEquals(3, typeInspector.getArrayDimensions());
   }


   @Test
   public void testFieldMultidimensionalArray2()
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      final Field<JavaClass> field = javaClass.addField();
      field.setName("content");
      field.setType(java.util.Vector[][][].class);
      Assert.assertEquals("java.util.Vector", field.getQualifiedType());
      Type<JavaClass> typeInspector = field.getTypeInspector();
      Assert.assertTrue(typeInspector.isArray());
      Assert.assertEquals(3, typeInspector.getArrayDimensions());
      Assert.assertEquals("Vector", field.getType());
   }
}
