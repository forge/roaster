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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldTypeTest
{
   @Test
   public void testGetReturnTypeReturnsFullTypeForJavaLang()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class).addField("public Long l;");
      assertEquals("java.lang.Long", field.getType().getQualifiedName());
      assertEquals("Long", field.getType().getName());
   }

   @Test
   public void testGetReturnTypeReturnsFullTypeForJavaLangGeneric()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<Long> list;");
      field.getOrigin().addImport(List.class);
      assertEquals("java.util.List", field.getType().getQualifiedName());
      assertEquals("List", field.getType().getName());
   }

   @Test
   public void testGetReturnTypeObjectArray()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List[] field;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.List", type.getQualifiedName());
      assertFalse(type.isParameterized());
      assertFalse(type.isWildcard());
      assertFalse(type.isPrimitive());
      assertFalse(type.isQualified());
      assertTrue(type.isArray());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(0, arguments.size());
   }

   @Test
   public void testGetReturnTypeObjectArrayParameterized()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<Long>[] list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.List", type.getQualifiedName());
      assertTrue(type.isParameterized());
      assertFalse(type.isWildcard());
      assertFalse(type.isPrimitive());
      assertFalse(type.isQualified());
      assertTrue(type.isArray());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(1, arguments.size());
   }

   @Test
   public void testGetReturnTypeObjectUnparameterized()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List list;");
      field.getOrigin().addImport(List.class);
      assertEquals("java.util.List", field.getType().getQualifiedName());
      assertFalse(field.getType().isParameterized());
   }

   @Test
   public void testGetReturnTypeObjectParameterized()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<Long> list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.List", type.getQualifiedName());
      assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(1, arguments.size());
      assertEquals("Long", arguments.get(0).getName());
      assertEquals("java.lang.Long", arguments.get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectWildcard()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<?> list;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.List", type.getQualifiedName());
      assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(1, arguments.size());
      assertEquals("?", arguments.get(0).getName());
      assertEquals("?", arguments.get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedMultiple()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public Map<String, Long> map;");
      field.getOrigin().addImport(Map.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.Map", type.getQualifiedName());
      assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(2, arguments.size());
      assertEquals("String", arguments.get(0).getName());
      assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      assertEquals("Long", arguments.get(1).getName());
      assertEquals("java.lang.Long", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedNested()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public List<List<Long>> map;");
      field.getOrigin().addImport(List.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.List", type.getQualifiedName());
      assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(1, arguments.size());
      assertEquals("List", arguments.get(0).getName());
      assertEquals("java.util.List", arguments.get(0).getQualifiedName());

      assertEquals(1, arguments.size());
      assertEquals("Long", arguments.get(0).getTypeArguments().get(0).getName());
      assertEquals("java.lang.Long", arguments.get(0).getTypeArguments().get(0).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedMultipleNested()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public Map<String, List<Long>> map;");
      field.getOrigin().addImport(List.class);
      field.getOrigin().addImport(Map.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.Map", type.getQualifiedName());
      assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(2, arguments.size());
      assertEquals("String", arguments.get(0).getName());
      assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      assertEquals("List", arguments.get(1).getName());
      assertEquals("java.util.List", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testGetReturnTypeObjectParameterizedArrayMultipleNested()
   {
      FieldSource<JavaClassSource> field = Roaster.create(JavaClassSource.class)
               .addField("public Map<String, List<Long>>[] maps;");
      field.getOrigin().addImport(List.class);
      field.getOrigin().addImport(Map.class);
      Type<JavaClassSource> type = field.getType();
      assertEquals("java.util.Map", type.getQualifiedName());
      assertTrue(type.isParameterized());

      List<Type<JavaClassSource>> arguments = type.getTypeArguments();

      assertEquals(2, arguments.size());
      assertEquals("String", arguments.get(0).getName());
      assertEquals("java.lang.String", arguments.get(0).getQualifiedName());

      assertEquals("List", arguments.get(1).getName());
      assertEquals("java.util.List", arguments.get(1).getQualifiedName());
   }

   @Test
   public void testFieldTypeByteArrayTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField();
      field.setName("content");
      field.setType(byte[].class);
      assertEquals("byte", field.getType().getQualifiedName());
      assertTrue(field.getType().isArray());
      assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testFieldMultidimensionalArray()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField();
      field.setName("content");
      field.setType(byte[][][].class);
      assertEquals("byte", field.getType().getQualifiedName());
      Type<JavaClassSource> type = field.getType();
      assertTrue(type.isArray());
      assertEquals(3, type.getArrayDimensions());
   }

   @Test
   public void testFieldMultidimensionalArray2()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField();
      field.setName("content");
      field.setType(java.util.Vector[][][].class);
      assertEquals("java.util.Vector", field.getType().getQualifiedName());
      Type<JavaClassSource> type = field.getType();
      assertTrue(type.isArray());
      assertEquals(3, type.getArrayDimensions());
      assertEquals("Vector[][][]", field.getType().getName());
   }

   @Test
   public void testFieldTypeByteArrayAlternativeDeclarationTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public byte content[];");
      assertEquals("byte[]", field.getType().getName());
      assertEquals("byte", field.getType().getQualifiedName());
      assertTrue(field.getType().isArray());
      assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testFieldTypeObjectArrayAlternativeDeclarationTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public Long content[];");
      assertEquals("Long[]", field.getType().getName());
      assertEquals("java.lang.Long", field.getType().getQualifiedName());
      assertTrue(field.getType().isArray());
      assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testFieldTypeObjectArrayMixedDimensionTest()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public Long[] content[];");
      assertEquals("Long[][]", field.getType().getName());
      assertEquals("java.lang.Long", field.getType().getQualifiedName());
      assertTrue(field.getType().isArray());
      assertEquals(2, field.getType().getArrayDimensions());
   }

   @Test
   public void testGenericFieldType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      FieldSource<JavaClassSource> field = javaClass.addField();
      field.setPrivate().setName("email").setType("java.util.List<String>")
               .setLiteralInitializer("new java.util.ArrayList<String>()");
      assertTrue(javaClass.hasImport(List.class));
      assertEquals("List<String>", field.getType().toString());
      assertEquals("new java.util.ArrayList<String>()", field.getLiteralInitializer());
   }

   @Test
   public void testComplexFieldType()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass<T>");
      String type = "java.util.Map<org.foo.MyEnum<T>,java.lang.Object>";
      FieldSource<JavaClassSource> field = clazz.addField().setName("field").setType(type);
      assertEquals(type, field.getType().getQualifiedNameWithGenerics());
   }

   @Test
   public void testComplexFieldTypeArray()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass<T>");
      String type = "java.util.Map<org.Foo.MyEnum<T>,java.lang.Object>[][]";
      FieldSource<JavaClassSource> field = clazz.addField().setName("field").setType(type);
      assertEquals(type, field.getType().getQualifiedNameWithGenerics());
   }

   @Test
   public void testImportImpliedGenerics()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass");
      String type = "org.foo.Code<org.foo.Condition>";
      clazz.addField().setName("param").setType(type);
      assertEquals(2, clazz.getImports().size());
   }

   @Test
   public void testNestedGenerics()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("MyClass");
      String type = "java.util.List<java.util.List<java.util.List<String>>>";
      FieldSource<JavaClassSource> field = clazz.addField().setName("param").setType(type);
      assertEquals(type, field.getType().getQualifiedNameWithGenerics());
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
