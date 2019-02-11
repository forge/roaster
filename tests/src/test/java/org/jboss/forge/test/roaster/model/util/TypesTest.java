/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Vector;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.util.Types;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public class TypesTest
{
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

   @Test(expected = NullPointerException.class)
   public void testAreEquivalentFailesIfLeftIsNull()
   {
      Types.areEquivalent(null, "");
   }

   @Test(expected = NullPointerException.class)
   public void testAreEquivalentFailesIfRightIsNull()
   {
      Types.areEquivalent("", null);
   }

   @Test(expected = NullPointerException.class)
   public void testToSimpleNameFailesIfArgumentIsNull()
   {
      Types.toSimpleName(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToTokenizeClassNameFailesIfArgumentIsNull()
   {
      Types.tokenizeClassName(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsQualifiedFailesIfArgumentIsNull()
   {
      Types.isQualified(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToGetPackageFailesIfArgumentIsNull()
   {
      Types.getPackage(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsSimpleNameFailesIfArgumentIsNull()
   {
      Types.isSimpleName(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsJavaLangFailesIfArgumentIsNull()
   {
      Types.isJavaLang(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsBasicTypeFailesIfArgumentIsNull()
   {
      Types.isBasicType(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsGenericFailesIfArgumentIsNull()
   {
      Types.isGeneric(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToValidateGenericsFailesIfArgumentIsNull()
   {
      Types.validateGenerics(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToStripGenericsFailesIfArgumentIsNull()
   {
      Types.stripGenerics(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToGetGenericsFailesIfArgumentIsNull()
   {
      Types.getGenerics(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToGetGenericsTypeParameterFailesIfArgumentIsNull()
   {
      Types.getGenericsTypeParameter(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsPrimitiveFailesIfArgumentIsNull()
   {
      Types.isPrimitive(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToStripArrayFailesIfArgumentIsNull()
   {
      Types.stripArray(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToIsArrayFailesIfArgumentIsNull()
   {
      Types.isArray(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToRebuildGenericNameWithArraysFailesIfTypeNameIsNull()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      Type<?> type = javaClass.addField().setName("foo").setType("Bar").getType();
      Types.rebuildGenericNameWithArrays(null, type);
   }

   @Test(expected = NullPointerException.class)
   public void testToRebuildGenericNameWithArraysFailesIfTypeIsNull()
   {
      Types.rebuildGenericNameWithArrays("abc", null);
   }

   @Test(expected = NullPointerException.class)
   public void testToGetArrayDimensionFailesIfArgumentIsNull()
   {
      Types.getArrayDimension(null);
   }

   @Test(expected = NullPointerException.class)
   public void testToGetDefaultValueFailesIfStringArgumentIsNull()
   {
      Types.getDefaultValue((String) null);
   }

   @Test(expected = NullPointerException.class)
   public void testToGetDefaultValueFailesIfClassArgumentIsNull()
   {
      Types.getDefaultValue((Class<?>) null);
   }

   @Test(expected = NullPointerException.class)
   public void testToSplitGenericsFailesIfArgumentIsNull()
   {
      Types.splitGenerics(null);
   }
}