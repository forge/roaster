/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.parser.java.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.jboss.forge.parser.java.util.Types;
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
   public void testAreEquivalent() throws Exception
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
   public void testIsQualified() throws Exception
   {
      assertTrue(Types.isQualified("org.jboss.forge.parser.JavaParser"));
      assertFalse(Types.isQualified("JavaParser"));
   }

   @Test
   public void testGetPackage() throws Exception
   {
      assertEquals("com.example", Types.getPackage("com.example.Domain"));
      assertEquals("", Types.getPackage("Domain"));
   }

   @Test
   public void testIsSimpleName() throws Exception
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

      assertEquals("byte", Types.stripArray(byte[].class.getSimpleName()));
      assertEquals("Boolean", Types.stripArray(Boolean[].class.getSimpleName()));
      assertEquals("Vector", Types.stripArray(Vector[].class.getSimpleName()));

      assertEquals("byte", Types.stripArray(byte[].class.getName()));
      assertEquals("java.lang.Boolean", Types.stripArray(Boolean[].class.getName()));
      assertEquals("java.util.Vector", Types.stripArray(Vector[].class.getName()));

      assertEquals("int", Types.stripArray(int[][][][][].class.getName()));

      assertEquals("int", Types.stripArray(int[][][][][].class.getSimpleName()));
      assertEquals("List<Long>", Types.stripArray("List<Long>[]"));
      assertEquals("java.lang.Class<?>", Types.stripArray("java.lang.Class<?>[]"));
      assertEquals("java.lang.Class<T>", Types.stripArray("java.lang.Class<T>[]"));
      assertEquals("java.lang.Class<LONG_TYPE_VARIABLE_NAME>",
               Types.stripArray("java.lang.Class<LONG_TYPE_VARIABLE_NAME>[]"));
      assertEquals("java.lang.Class<? extends Number>", Types.stripArray("java.lang.Class<? extends Number>[]"));
      assertEquals("java.lang.Class<E extends Enum<E>>", Types.stripArray("java.lang.Class<E extends Enum<E>>[]"));
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
   }

}
