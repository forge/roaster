/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import java.util.List;
import java.util.Vector;

import org.assertj.core.api.Assertions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.Importer;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.util.Types;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class TypesTest
{
   @Test
   public void testGetArraySuffix()
   {
      assertEquals("", Types.getArraySuffix(""));
      assertEquals("", Types.getArraySuffix("String"));
      assertEquals("[]", Types.getArraySuffix("String[]"));
      assertEquals("[][]", Types.getArraySuffix("String[][]"));
   }

   @Test
   public void testToResolvedType()
   {
      JavaClassSource classSource = Roaster.create(JavaClassSource.class);
      classSource.addImport("c1.OuterClass");
      classSource.addImport("g2.SecondGeneric");

      String toResolved = "c1.OuterClass<g1.FirstGeneric,g2.SecondGeneric<java.lang.String>>[][]";
      String expected = "OuterClass<g1.FirstGeneric,SecondGeneric<String>>[][]";
      assertEquals(expected, Types.toResolvedType(toResolved, classSource));
   }

   @Test
   public void testIsBasicType()
   {
      assertTrue(Types.isBasicType("String"));
      assertTrue(Types.isBasicType("Short"));
      assertTrue(Types.isBasicType("int"));
      assertTrue(Types.isBasicType("boolean"));
      assertTrue(Types.isBasicType("Boolean"));
      assertTrue(Types.isBasicType("long"));
      assertTrue(Types.isBasicType("Float"));
      assertTrue(Types.isBasicType("Double"));
   }

   @Test
   public void testAreEquivalent()
   {
      assertTrue(Types.areEquivalent("com.example.Domain", "com.example.Domain"));
      assertTrue(Types.areEquivalent("Domain", "com.example.Domain"));
      assertTrue(Types.areEquivalent("com.example.Domain", "Domain"));
      assertTrue(Types.areEquivalent("Domain", "Domain"));
      assertFalse(Types.areEquivalent("com.example.Domain", "com.other.Domain"));
      assertTrue(Types.areEquivalent("com.example.Domain<T>", "Domain"));
      assertTrue(Types.areEquivalent("Domain<T>", "com.example.Domain"));
   }

   @Test
   public void testIsQualified()
   {
      assertTrue(Types.isQualified("org.jboss.forge.roaster.JavaParser"));
      assertFalse(Types.isQualified("JavaParser"));
   }

   @Test
   public void testGetPackage()
   {
      assertEquals("com.example", Types.getPackage("com.example.Domain"));
      assertEquals("", Types.getPackage("Domain"));
   }

   @Test
   public void testIsSimpleName()
   {
      assertTrue(Types.isSimpleName("Domain$"));
      assertFalse(Types.isSimpleName("9Domain$"));
      assertFalse(Types.isSimpleName("com.Domain$"));
      assertFalse(Types.isSimpleName("99"));
      assertFalse(Types.isSimpleName(""));
      assertFalse(Types.isSimpleName("Foo-bar"));
   }

   @Test
   public void testArray()
   {
      assertTrue(Types.isArray("byte[]"));
      assertTrue(Types.isArray("java.lang.Boolean[]"));
      assertTrue(Types.isArray("java.util.Vector[]"));

      assertTrue(Types.isArray(byte[].class.getName()));
      assertTrue(Types.isArray(Boolean[].class.getName()));
      assertTrue(Types.isArray(Types[].class.getName()));

      assertTrue(Types.isArray("Map<String,List<Long>>[]"));

      assertEquals("byte", Types.stripArray(byte[].class.getSimpleName()));
      assertEquals("Boolean", Types.stripArray(Boolean[].class.getSimpleName()));
      assertEquals("Vector", Types.stripArray(Vector[].class.getSimpleName()));

      assertEquals("byte", Types.stripArray(byte[].class.getName()));
      assertEquals("java.lang.Boolean", Types.stripArray(Boolean[].class.getName()));
      assertEquals("java.util.Vector", Types.stripArray(Vector[].class.getName()));

      assertEquals("java.util.Map<org.foo.String[],T>", Types.stripArray("java.util.Map<org.foo.String[],T>[]"));

      assertEquals("int", Types.stripArray(int[][][][][].class.getName()));

      assertEquals("int", Types.stripArray(int[][][][][].class.getSimpleName()));
      assertEquals("List<Long>", Types.stripArray("List<Long>[]"));
      assertEquals("java.lang.Class<?>", Types.stripArray("java.lang.Class<?>[]"));
      assertEquals("java.lang.Class<T>", Types.stripArray("java.lang.Class<T>[]"));
      assertEquals("java.lang.Class<LONG_TYPE_VARIABLE_NAME>",
               Types.stripArray("java.lang.Class<LONG_TYPE_VARIABLE_NAME>[]"));
      assertEquals("java.lang.Class<? extends Number>", Types.stripArray("java.lang.Class<? extends Number>[]"));
      assertEquals("java.lang.Class<E extends Enum<E>>", Types.stripArray("java.lang.Class<E extends Enum<E>>[]"));
      assertEquals("java.util.Map<org.Foo.MyEnum<T>,java.lang.Object>",
               Types.stripArray("java.util.Map<org.Foo.MyEnum<T>,java.lang.Object>[][]"));
   }

   @Test
   public void testArrayDimensions()
   {
      assertEquals(0, Types.getArrayDimension(Boolean.class.getName()));
      assertEquals(1, Types.getArrayDimension(int[].class.getName()));
      assertEquals(2, Types.getArrayDimension(int[][].class.getName()));
      assertEquals(3, Types.getArrayDimension(int[][][].class.getName()));
   }

   @Test
   public void testGenerics()
   {
      assertEquals("byte", Types.stripGenerics("byte"));
      assertEquals("byte[]", Types.stripGenerics("byte[]"));
      assertEquals("java.lang.Class", Types.stripGenerics("java.lang.Class"));
      assertEquals("java.lang.Class", Types.stripGenerics("java.lang.Class<?>"));
      assertEquals("java.lang.Class", Types.stripGenerics("java.lang.Class<? extends Number>"));
      assertEquals("java.lang.Class", Types.stripGenerics("java.lang.Class<E extends Enum<E>>"));
      assertEquals("java.lang.Class[]", Types.stripGenerics("java.lang.Class[]"));
      assertEquals("java.lang.Class[]", Types.stripGenerics("java.lang.Class<?>[]"));
      assertEquals("java.lang.Class[]", Types.stripGenerics("java.lang.Class<T>[]"));
      assertEquals("java.lang.Class[]", Types.stripGenerics("java.lang.Class<LONG_TYPE_VARIABLE_NAME>[]"));
      assertEquals("java.lang.Class[]", Types.stripGenerics("java.lang.Class<? extends Number>[]"));
      assertEquals("java.lang.Class[]", Types.stripGenerics("java.lang.Class<E extends Enum<E>>[]"));
      assertEquals("int[]", Types.stripGenerics(int[].class.getName()));
      assertEquals("java.util.List", Types.stripGenerics("java.util.List<String>"));
      assertEquals("java.util.List", Types.stripGenerics("java.util.List<java.lang.String>"));
      assertEquals("java.util.List", Types.stripGenerics("java.util.List<List<String>>"));
      assertEquals("java.util.List", Types.stripGenerics("java.util.List<List<java.lang.String>>"));
      assertEquals("java.util.Map[][]", Types.stripGenerics("java.util.Map<org.Foo.MyEnum<T>,java.lang.Object>[][]"));
      assertEquals("java.util.Map[]", Types.stripGenerics("java.util.Map<org.foo.String[],T>[]"));
      assertEquals("Map[]", Types.stripGenerics("Map<String,List<Long>>[]"));
      assertTrue(Types.isGeneric("java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>"));
   }

   @Test
   public void testStringIsJavaLang()
   {
      assertTrue(Types.isJavaLang("String"));
   }

   @Test
   public void testAssertClassIsNotJavaLang()
   {
      assertFalse(Types.isJavaLang("AssertClass"));
   }

   @Test
   public void testAssertToSimpleName()
   {
      assertEquals("List<String>", Types.toSimpleName("java.util.List<java.lang.String>"));
      assertEquals("List<String>", Types.toSimpleName("java.util.List<String>"));
      assertEquals("List<List<String>>", Types.toSimpleName("java.util.List<java.util.List<String>>"));
      assertEquals("List<? extends List<String>>",
               Types.toSimpleName("java.util.List<? extends java.util.List<String>>"));
      // now identical tests with array types
      assertEquals("List<String>[]", Types.toSimpleName("List<String>[]"));
      assertEquals("List<String>[]", Types.toSimpleName("java.util.List<String>[]"));
      assertEquals("List<String>[]", Types.toSimpleName("java.util.List<java.lang.String>[]"));
      assertEquals("List<? extends List<String>>[]",
               Types.toSimpleName("java.util.List<? extends java.util.List<String>>[]"));
   }

   @Test
   public void testDefaultValuesUsingClass()
   {
      assertEquals("null", Types.getDefaultValue(Object.class));
      assertEquals("null", Types.getDefaultValue(List.class));
      assertEquals("false", Types.getDefaultValue(boolean.class));
      assertEquals("0", Types.getDefaultValue(int.class));
      assertEquals("0", Types.getDefaultValue(char.class));
   }

   @Test
   public void testDefaultValuesUsingString()
   {
      assertEquals("null", Types.getDefaultValue("Object"));
      assertEquals("null", Types.getDefaultValue("java.util.List"));
      assertEquals("false", Types.getDefaultValue("boolean"));
      assertEquals("false", Types.getDefaultValue(boolean.class.getName()));
      assertEquals("0", Types.getDefaultValue("int"));
      assertEquals("0", Types.getDefaultValue("char"));
   }

   @Test
   public void testIsGeneric()
   {
      assertTrue(Types.isGeneric("List<Map<String, String>>"));
   }

   @Test
   public void testSplitGenerics()
   {
      assertArrayEquals(new String[] {}, Types.splitGenerics("Foo"));
      assertArrayEquals(new String[] { "String", "Integer" }, Types.splitGenerics("Foo<String,Integer>"));
      assertArrayEquals(new String[] { "Bar<A>", "Bar<B>" }, Types.splitGenerics("Foo<Bar<A>, Bar<B>>"));
   }

   @Test
   public void testAreEquivalentFailsIfLeftIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.areEquivalent(null, ""));
   }

   @Test
   public void testAreEquivalentFailsIfRightIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.areEquivalent("", null));
   }

   @Test
   public void testToSimpleNameFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.toSimpleName(null));
   }

   @Test
   public void testToTokenizeClassNameFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.tokenizeClassName(null));
   }

   @Test
   public void testToIsQualifiedFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isQualified(null));
   }

   @Test
   public void testToGetPackageFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.getPackage(null));
   }

   @Test
   public void testToIsSimpleNameFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isSimpleName(null));
   }

   @Test
   public void testToIsJavaLangFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isJavaLang(null));
   }

   @Test
   public void testToIsBasicTypeFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isBasicType(null));
   }

   @Test
   public void testToIsGenericFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isGeneric(null));
   }

   @Test
   public void testToValidateGenericsFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.validateGenerics(null));
   }

   @Test
   public void testToStripGenericsFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.stripGenerics(null));
   }

   @Test
   public void testToGetGenericsFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.getGenerics(null));
   }

   @Test
   public void testToGetGenericsTypeParameterFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.getGenericsTypeParameter(null));
   }

   @Test
   public void testToIsPrimitiveFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isPrimitive(null));
   }

   @Test
   public void testToStripArrayFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.stripArray(null));
   }

   @Test
   public void testToIsArrayFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.isArray(null));
   }

   @Test
   public void testToRebuildGenericNameWithArraysFailsIfTypeNameIsNull()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      Type<?> type = javaClass.addField().setName("foo").setType("Bar").getType();
      assertThrows(NullPointerException.class, () -> Types.rebuildGenericNameWithArrays(null, type));
   }

   @Test
   public void testToRebuildGenericNameWithArraysFailsIfTypeIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.rebuildGenericNameWithArrays("abc", null));

   }

   @Test
   public void testToGetArrayDimensionFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.getArrayDimension(null));
   }

   @Test
   public void testToGetDefaultValueFailsIfStringArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.getDefaultValue((String) null));
   }

   @Test
   public void testToGetDefaultValueFailsIfClassArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.getDefaultValue((Class<?>) null));
   }

   @Test
   public void testToSplitGenericsFailsIfArgumentIsNull()
   {
      assertThrows(NullPointerException.class, () -> Types.splitGenerics(null));
   }

   // ROASTER-135
   @Test
   public void testToHandleWildcardsInToResolvedType()
   {
      Importer<?> importer = mock(Importer.class);

      when(importer.getImport("List<? extends Account>")).thenReturn(null);
      when(importer.getImport("?extendsAccount")).thenReturn(null);

      assertEquals("List<? extends Account>", Types.toResolvedType("List<? extends Account>", importer));
   }


   @Test
   public void testFQNClassDoesNotBelongToJavaLang()
   {
      assertThat(Types.isJavaLang("foo.Error")).isFalse();
   }

}