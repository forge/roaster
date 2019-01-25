/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.util.List;
import java.util.Map;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldTypeTest
{
   @Test
   public void testGetReturnTypeReturnsFullTypeForJavaLang() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class).addField("public Long l;");
      Assert.assertEquals("java.lang.Long", field.getType().getQualifiedName());
      Assert.assertEquals("Long", field.getType().getName());
   }

   @Test
   public void testGetReturnTypeReturnsFullTypeForJavaLangGeneric() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<Long> list;");
      field.getOrigin().addImport(List.class);
      Assert.assertEquals("java.util.List", field.getType().getQualifiedName());
      Assert.assertEquals("List", field.getType().getName());
   }

   @Test
   public void testGetReturnTypeObjectArray() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List[] field;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertFalse(type.isParameterized());
      Assert.assertFalse(type.isWildcard());
      Assert.assertFalse(type.isPrimitive());
      Assert.assertFalse(type.isQualified());
      Assert.assertTrue(type.isArray());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(0, arguments.size());
   }

   @Test
   public void testGetReturnTypeObjectArrayParameterized() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<Long>[] list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());
      Assert.assertFalse(type.isWildcard());
      Assert.assertFalse(type.isPrimitive());
      Assert.assertFalse(type.isQualified());
      Assert.assertTrue(type.isArray());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
   }

   @Test
   public void testGetReturnTypeObjectUnparameterized() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List list;");
      field.getOrigin().addImport(List.class);
      Assert.assertEquals("java.util.List", field.getType().getQualifiedName());
      Assert.assertFalse(field.getType().isParameterized());
   }

   @Test
   public void testGetReturnTypeObjectParameterized() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<Long> list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("Long", arguments.get(0).getName());
      Assert.assertEquals("java.lang.Long", arguments.get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectWildcard() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<?> list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("?", arguments.get(0).getName());
      Assert.assertEquals("?", arguments.get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedMultiple() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public Map<String, Long> map;");
      field.getOrigin().addImport(Map.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.Map", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(2, arguments.size());
      Assert.assertEquals("String", arguments.get(0).getName());
      Assert.assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      Assert.assertEquals("Long", arguments.get(1).getName());
      Assert.assertEquals("java.lang.Long", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedNested() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<List<Long>> map;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.List", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("List", arguments.get(0).getName());
      Assert.assertEquals("java.util.List", arguments.get(0).getQualifiedName());

      Assert.assertEquals(1, arguments.size());
      Assert.assertEquals("Long", arguments.get(0).getTypeArguments().get(0).getName());
      Assert.assertEquals("java.lang.Long", arguments.get(0).getTypeArguments().get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedMultipleNested() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public Map<String, List<Long>> map;");
      field.getOrigin().addImport(List.class);
      field.getOrigin().addImport(Map.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.Map", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(2, arguments.size());
      Assert.assertEquals("String", arguments.get(0).getName());
      Assert.assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      Assert.assertEquals("List", arguments.get(1).getName());
      Assert.assertEquals("java.util.List", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedArrayMultipleNested() 
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public Map<String, List<Long>>[] maps;");
      field.getOrigin().addImport(List.class);
      field.getOrigin().addImport(Map.class);
      Type<JavaClassSource> type = field.getType();
      Assert.assertEquals("java.util.Map", type.getQualifiedName());
      Assert.assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      Assert.assertEquals(2, arguments.size());
      Assert.assertEquals("String", arguments.get(0).getName());
      Assert.assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      Assert.assertEquals("List", arguments.get(1).getName());
      Assert.assertEquals("java.util.List", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testFieldTypeByteArrayTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField();
      field.setName("content");
      field.setType(byte[].class);
      Assert.assertEquals("byte", field.getType().getQualifiedName());
      Assert.assertTrue(field.getType().isArray());
      Assert.assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testFieldMultidimensionalArray()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField();
      field.setName("content");
      field.setType(byte[][][].class);
      Assert.assertEquals("byte", field.getType().getQualifiedName());
      Type<JavaClassSource> type = field.getType();
      Assert.assertTrue(type.isArray());
      Assert.assertEquals(3, type.getArrayDimensions());
   }

   @Test
   public void testFieldMultidimensionalArray2()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField();
      field.setName("content");
      field.setType(java.util.Vector[][][].class);
      Assert.assertEquals("java.util.Vector", field.getType().getQualifiedName());
      Type<JavaClassSource> type = field.getType();
      Assert.assertTrue(type.isArray());
      Assert.assertEquals(3, type.getArrayDimensions());
      Assert.assertEquals("Vector[][][]", field.getType().getName());
   }

   @Test
   public void testFieldTypeByteArrayAlternativeDeclarationTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public byte content[];");
      Assert.assertEquals("byte[]", field.getType().getName());
      Assert.assertEquals("byte", field.getType().getQualifiedName());
      Assert.assertTrue(field.getType().isArray());
      Assert.assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testFieldTypeObjectArrayAlternativeDeclarationTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public Long content[];");
      Assert.assertEquals("Long[]", field.getType().getName());
      Assert.assertEquals("java.lang.Long", field.getType().getQualifiedName());
      Assert.assertTrue(field.getType().isArray());
      Assert.assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testFieldTypeObjectArrayMixedDimensionTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public Long[] content[];");
      Assert.assertEquals("Long[][]", field.getType().getName());
      Assert.assertEquals("java.lang.Long", field.getType().getQualifiedName());
      Assert.assertTrue(field.getType().isArray());
      Assert.assertEquals(2, field.getType().getArrayDimensions());
   }

   @Test
   public void testGenericFieldType() 
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      FieldSource<JavaClassSource> field = javaClass.addField();
      field.setPrivate().setName("email").setType("java.util.List<String>")
               .setLiteralInitializer("new java.util.ArrayList<String>()");
      Assert.assertTrue(javaClass.hasImport(List.class));
      Assert.assertEquals("List<String>", field.getType().toString());
      Assert.assertEquals("new java.util.ArrayList<String>()", field.getLiteralInitializer());
   }

   @Test
   public void testComplexFieldType() 
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass<T>");
      String type = "java.util.Map<org.foo.MyEnum<T>,java.lang.Object>";
      FieldSource<JavaClassSource> field = clazz.addField().setName("field").setType(type);
      Assert.assertEquals(type, field.getType().getQualifiedNameWithGenerics());
   }

   @Test
   public void testComplexFieldTypeArray() 
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass<T>");
      String type = "java.util.Map<org.Foo.MyEnum<T>,java.lang.Object>[][]";
      FieldSource<JavaClassSource> field = clazz.addField().setName("field").setType(type);
      Assert.assertEquals(type, field.getType().getQualifiedNameWithGenerics());
   }

   @Test
   public void testImportImpliedGenerics()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass");
      String type = "org.foo.Code<org.foo.Condition>";
      clazz.addField().setName("param").setType(type);
      Assert.assertEquals(2, clazz.getImports().size());
   }

   @Test
   public void testNestedGenerics()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass");
      String type = "java.util.List<java.util.List<java.util.List<String>>>";
      FieldSource<JavaClassSource> field = clazz.addField().setName("param").setType(type);
      Assert.assertEquals(type, field.getType().getQualifiedNameWithGenerics());
   }

   @Test
   public void testFieldFQNTypeIsMaintainedIfAnotherImportExists()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("testPackage").setName("Main");
      // Field one
      javaClass.addField().setName("field1").setType("package1.Type");
      // Field two
      javaClass.addField().setName("field2").setType("package2.Type");
      // out
      assertThat(javaClass.getField("field2").getType().getQualifiedName())
               .isEqualTo("package2.Type");
   }

   @Test
   public void testFieldFQNTypeIsMaintainedIfTypeNameMatchesClassName()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("testPackage").setName("Main");
      // Field one
      javaClass.addField().setName("field1").setType("anotherPackage.Main");
      // out
      assertThat(javaClass.getField("field1").getType().getQualifiedName())
               .isEqualTo("anotherPackage.Main");
   }

}
