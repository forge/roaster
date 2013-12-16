/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.FieldSource;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.jboss.forge.parser.java.source.MethodSource;
import org.jboss.forge.parser.java.util.Refactory;
import org.jboss.forge.parser.java.util.Strings;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RefactoryTest
{
   JavaClassSource javaClass;

   @Before
   public void before()
   {
      javaClass = JavaParser
               .parse(JavaClassSource.class,
                        "import java.util.Set; public class Foo { private int foo; private String firstName; private Set<String> names; private final int bar; }");
   }

   @Test
   public void testAddGettersAndSetters() throws Exception
   {
      FieldSource<JavaClassSource> field = javaClass.getField("foo");
      Refactory.createGetterAndSetter(javaClass, field);

      List<MethodSource<JavaClassSource>> methods = javaClass.getMethods();
      MethodSource<JavaClassSource> getter = methods.get(0);
      MethodSource<JavaClassSource> setter = methods.get(1);

      assertEquals("getFoo", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("setFoo", setter.getName());
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testAddGettersAndSettersCamelCase() throws Exception
   {
      FieldSource<JavaClassSource> field = javaClass.getField("firstName");
      Refactory.createGetterAndSetter(javaClass, field);

      List<MethodSource<JavaClassSource>> methods = javaClass.getMethods();
      MethodSource<JavaClassSource> getter = methods.get(0);
      MethodSource<JavaClassSource> setter = methods.get(1);

      assertEquals("getFirstName", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("setFirstName", setter.getName());
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testAddGetterNotSetterForFinalField() throws Exception
   {
      FieldSource<JavaClassSource> field = javaClass.getField("bar");
      Refactory.createGetterAndSetter(javaClass, field);

      List<MethodSource<JavaClassSource>> methods = javaClass.getMethods();
      MethodSource<JavaClassSource> getter = methods.get(0);

      assertEquals("getBar", getter.getName());
      assertEquals(1, methods.size());
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testAddGettersAndSettersGeneric() throws Exception
   {
      FieldSource<JavaClassSource> field = javaClass.getField("names");
      Refactory.createGetterAndSetter(javaClass, field);

      List<MethodSource<JavaClassSource>> methods = javaClass.getMethods();
      MethodSource<JavaClassSource> getter = methods.get(0);
      MethodSource<JavaClassSource> setter = methods.get(1);

      assertEquals("getNames", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("Set", getter.getReturnType().getName());
      assertEquals("Set<String>", getter.getReturnType().toString());
      assertEquals("setNames", setter.getName());
      assertFalse(setter.getParameters().isEmpty());
      assertEquals("Set<String>", setter.getParameters().get(0).getType().toString());
      assertEquals("Set", setter.getParameters().get(0).getType().getName());
      assertTrue(setter.getParameters().get(0).getType().isParameterized());
      assertEquals("String", setter.getParameters().get(0).getType().getTypeArguments().get(0).getName());
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testCreateToStringFromFields() throws Exception
   {
      assertFalse(javaClass.hasMethodSignature("toString"));
      Refactory.createToStringFromFields(javaClass);
      assertTrue(javaClass.hasMethodSignature("toString"));
      assertTrue(javaClass.getMethod("toString").getBody().contains("return"));
      assertTrue(javaClass.getMethod("toString").getBody().contains("firstName != null"));
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateHashCodeAndEqualsForStatics() throws Exception
   {
      JavaClassSource aClass = JavaParser
               .parse(JavaClassSource.class,
                        "public class Foo { private static boolean flag;}");
      FieldSource<JavaClassSource> booleanField = aClass.getField("flag");
      Refactory.createHashCodeAndEquals(aClass, booleanField);
   }

   @Test
   public void testCreateHashCodeAndEqualsForPrimitives() throws Exception
   {
      JavaClassSource aClass = JavaParser
               .parse(JavaClassSource.class,
                        "public class Foo { private boolean aBoolean; private byte aByte; private char aChar; private short aShort; private int anInt; private long aLong; private float aFloat; private double aDouble; }");
      FieldSource<JavaClassSource> booleanField = aClass.getField("aBoolean");
      FieldSource<JavaClassSource> byteField = aClass.getField("aByte");
      FieldSource<JavaClassSource> charField = aClass.getField("aChar");
      FieldSource<JavaClassSource> shortField = aClass.getField("aShort");
      FieldSource<JavaClassSource> intField = aClass.getField("anInt");
      FieldSource<JavaClassSource> longField = aClass.getField("aLong");
      FieldSource<JavaClassSource> floatField = aClass.getField("aFloat");
      FieldSource<JavaClassSource> doubleField = aClass.getField("aDouble");
      Refactory.createHashCodeAndEquals(aClass, booleanField, byteField, charField, shortField, intField, longField,
               floatField, doubleField);

      List<MethodSource<JavaClassSource>> methods = aClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(equals.getBody(), containsString("if (aBoolean != other.aBoolean) {\n  return false;\n}"));
      assertThat(equals.getBody(), containsString("if (aByte != other.aByte) {\n  return false;\n}"));
      assertThat(equals.getBody(), containsString("if (aChar != other.aChar) {\n  return false;\n}"));
      assertThat(equals.getBody(), containsString("if (aShort != other.aShort) {\n  return false;\n}"));
      assertThat(equals.getBody(), containsString("if (anInt != other.anInt) {\n  return false;\n}"));
      assertThat(equals.getBody(), containsString("if (aLong != other.aLong) {\n  return false;\n}"));
      assertThat(
               equals.getBody(),
               containsString("if (Float.floatToIntBits(aFloat) != Float.floatToIntBits(other.aFloat)) {\n  return false;\n}"));
      assertThat(
               equals.getBody(),
               containsString("if (Double.doubleToLongBits(aDouble) != Double.doubleToLongBits(other.aDouble)) {\n  return false;\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertThat(hashcode.getBody(), containsString("result=prime * result + (aBoolean ? 1231 : 1237);"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + aByte;"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + aChar;"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + aShort;"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + anInt;"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + (int)(aLong ^ (aLong >>> 32));"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + Float.floatToIntBits(aFloat);"));
      assertThat(hashcode.getBody(), containsString("long temp;"));
      assertThat(hashcode.getBody(), containsString("temp=Double.doubleToLongBits(aDouble);"));
      assertThat(hashcode.getBody(), containsString("prime * result + (int)(temp ^ (temp >>> 32));"));
      assertFalse(aClass.hasSyntaxErrors());
   }

   @Test
   public void testCreateHashCodeAndEqualsForArrays() throws Exception
   {
      JavaClassSource aClass = JavaParser
               .parse(JavaClassSource.class,
                        "public class Foo { private boolean[] flags; private Object[] objects;}");
      FieldSource<JavaClassSource> primitiveArrayField = aClass.getField("flags");
      FieldSource<JavaClassSource> objectArrayField = aClass.getField("objects");
      Refactory.createHashCodeAndEquals(aClass, primitiveArrayField, objectArrayField);

      List<MethodSource<JavaClassSource>> methods = aClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(equals.getBody(), containsString("if (!Arrays.equals(flags,other.flags)) {\n  return false;\n}"));
      assertThat(equals.getBody(), containsString("if (!Arrays.equals(objects,other.objects)) {\n  return false;\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertThat(hashcode.getBody(), containsString("result=prime * result + Arrays.hashCode(flags);"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + Arrays.hashCode(objects);"));
      assertFalse(aClass.hasSyntaxErrors());
   }

   @Test
   public void testCreateHashCodeAndEqualsForObjects() throws Exception
   {
      JavaClassSource aClass = JavaParser
               .parse(JavaClassSource.class,
                        "import java.util.Date; public class Foo { private Object object; private Date date;}");
      FieldSource<JavaClassSource> identityBasedField = aClass.getField("object");
      FieldSource<JavaClassSource> nonIdentityBasedField = aClass.getField("date");
      Refactory.createHashCodeAndEquals(aClass, identityBasedField, nonIdentityBasedField);

      List<MethodSource<JavaClassSource>> methods = aClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(
               equals.getBody(),
               containsString("if (object != null) {\n  if (!object.equals(other.object)) {\n    return false;\n  }\n}"));
      assertThat(equals.getBody(),
               containsString("if (date != null) {\n  if (!date.equals(other.date)) {\n    return false;\n  }\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertThat(hashcode.getBody(),
               containsString("result=prime * result + ((object == null) ? 0 : object.hashCode());"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + ((date == null) ? 0 : date.hashCode());"));
      assertFalse(aClass.hasSyntaxErrors());
   }

   @SuppressWarnings("deprecation")
   @Test
   public void testCreateHashCodeAndEqualsNoArgs() throws Exception
   {
      Refactory.createHashCodeAndEquals(javaClass);

      List<MethodSource<JavaClassSource>> methods = javaClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testCreateHashCodeAndEqualsSubclass() throws Exception
   {
      JavaClassSource subClass = JavaParser
               .parse(JavaClassSource.class,
                        "import java.util.Set;import java.util.Date; public class Foo extends Date { private int foo; private String firstName; private Set<String> names; private final int bar; }");
      FieldSource<JavaClassSource> intField = subClass.getField("foo");
      FieldSource<JavaClassSource> stringField = subClass.getField("firstName");
      Refactory.createHashCodeAndEquals(subClass, intField, stringField);

      List<MethodSource<JavaClassSource>> methods = subClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(equals.getBody(), containsString("if (!super.equals(obj)) {\n  return false;\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertThat(hashcode.getBody(), containsString("int result=super.hashCode();"));
      assertFalse(subClass.hasSyntaxErrors());
   }

   @Test
   public void testCreateHashCodeAndEqualsOuterClass() throws Exception
   {
      JavaClassSource outerClass = JavaParser
               .parse(JavaClassSource.class,
                        "public class Foo { private Foo.Bar bar; class Bar{ private Boolean flag; } }");
      FieldSource<JavaClassSource> outerField = outerClass.getField("bar");
      Refactory.createHashCodeAndEquals(outerClass, outerField);

      List<MethodSource<JavaClassSource>> methods = outerClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(equals.getBody(),
               containsString("if (bar != null) {\n  if (!bar.equals(other.bar)) {\n    return false;\n  }\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertThat(hashcode.getBody(), containsString("result=prime * result + ((bar == null) ? 0 : bar.hashCode());"));
      assertFalse(outerClass.hasSyntaxErrors());
   }

   @Test
   @Ignore
   // This is not supported for now
   public void testCreateHashCodeAndEqualsInnerClass() throws Exception
   {
      JavaClassSource outerClass = JavaParser
               .parse(JavaClassSource.class,
                        "public class Foo { private Foo.Bar bar; class Bar{ private Boolean flag; } }");
      JavaClassSource innerClass = (JavaClassSource) outerClass.getNestedClasses().get(0);
      FieldSource<JavaClassSource> innerField = innerClass.getField("flag");
      Refactory.createHashCodeAndEquals(innerClass, innerField);

      List<MethodSource<JavaClassSource>> methods = innerClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);
      MethodSource<JavaClassSource> outerTypeAccessor = methods.get(2);

      assertEquals("getOuterType", outerTypeAccessor.getName());

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(equals.getBody(),
               containsString("if ((!getOuterType().equals(other.getOuterType()))) {\n  return false;\n}"));
      assertThat(equals.getBody(),
               containsString("if (flag != null) {\n  if (!flag.equals(other.flag)) {\n    return false;\n  }\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType());
      assertThat(hashcode.getBody(), containsString("result=prime * result + getOuterType().hashCode();"));
      assertThat(hashcode.getBody(), containsString("result=prime * result + ((flag == null) ? 0 : flag.hashCode());"));
      assertFalse(outerClass.hasSyntaxErrors());
   }

   @Test
   public void testCreateHashCodeAndEqualsMultipleLongFields() throws Exception
   {
      JavaClassSource aClass = JavaParser
               .parse(JavaClassSource.class,
                        "public class Foo { private double firstDouble; private double secondDouble;}");
      FieldSource<JavaClassSource> firstLongField = aClass.getField("firstDouble");
      FieldSource<JavaClassSource> secondLongField = aClass.getField("secondDouble");
      Refactory.createHashCodeAndEquals(aClass, firstLongField, secondLongField);

      List<MethodSource<JavaClassSource>> methods = aClass.getMethods();
      MethodSource<JavaClassSource> equals = methods.get(0);
      MethodSource<JavaClassSource> hashcode = methods.get(1);

      assertEquals("equals", equals.getName());
      assertEquals(1, equals.getParameters().size());
      assertThat(
               equals.getBody(),
               containsString("if (Double.doubleToLongBits(firstDouble) != Double.doubleToLongBits(other.firstDouble)) {\n  return false;\n}"));
      assertThat(
               equals.getBody(),
               containsString("if (Double.doubleToLongBits(secondDouble) != Double.doubleToLongBits(other.secondDouble)) {\n  return false;\n}"));

      assertEquals("hashCode", hashcode.getName());
      assertEquals(0, hashcode.getParameters().size());
      assertEquals("int", hashcode.getReturnType().getName());
      assertThat(hashcode.getBody(), containsString("long temp;"));
      assertEquals(1, Strings.countNumberOfOccurences(hashcode.getBody(), "long temp;"));
      assertThat(hashcode.getBody(), containsString("temp=Double.doubleToLongBits(firstDouble);"));
      assertThat(hashcode.getBody(), containsString("temp=Double.doubleToLongBits(secondDouble);"));
      assertThat(hashcode.getBody(), containsString("prime * result + (int)(temp ^ (temp >>> 32));"));
      assertEquals(2,
               Strings.countNumberOfOccurences(hashcode.getBody(), "prime * result + (int)(temp ^ (temp >>> 32));"));
   }
}
