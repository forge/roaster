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

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.parser.java.util.Refactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RefactoryTest
{
   JavaClass javaClass;

   @Before
   public void before()
   {
      javaClass = JavaParser
               .parse(JavaClass.class,
                        "import java.util.Set; public class Foo { private int foo; private String firstName; private Set<String> names; private final int bar; }");
   }

   @Test
   public void testAddGettersAndSetters() throws Exception
   {
      Field<JavaClass> field = javaClass.getField("foo");
      Refactory.createGetterAndSetter(javaClass, field);

      List<Method<JavaClass>> methods = javaClass.getMethods();
      Method<JavaClass> getter = methods.get(0);
      Method<JavaClass> setter = methods.get(1);

      assertEquals("getFoo", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("setFoo", setter.getName());
   }

   @Test
   public void testAddGettersAndSettersCamelCase() throws Exception
   {
      Field<JavaClass> field = javaClass.getField("firstName");
      Refactory.createGetterAndSetter(javaClass, field);

      List<Method<JavaClass>> methods = javaClass.getMethods();
      Method<JavaClass> getter = methods.get(0);
      Method<JavaClass> setter = methods.get(1);

      assertEquals("getFirstName", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("setFirstName", setter.getName());
   }

   @Test
   public void testAddGetterNotSetterForFinalField() throws Exception
   {
      Field<JavaClass> field = javaClass.getField("bar");
      Refactory.createGetterAndSetter(javaClass, field);

      List<Method<JavaClass>> methods = javaClass.getMethods();
      Method<JavaClass> getter = methods.get(0);

      assertEquals("getBar", getter.getName());
      assertEquals(1, methods.size());
   }

   @Test
   public void testAddGettersAndSettersGeneric() throws Exception
   {
      Field<JavaClass> field = javaClass.getField("names");
      Refactory.createGetterAndSetter(javaClass, field);

      List<Method<JavaClass>> methods = javaClass.getMethods();
      Method<JavaClass> getter = methods.get(0);
      Method<JavaClass> setter = methods.get(1);

      assertEquals("getNames", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("Set", getter.getReturnType());
      assertEquals("Set<String>", getter.getReturnTypeInspector().toString());
      assertEquals("setNames", setter.getName());
      assertFalse(setter.getParameters().isEmpty());
      assertEquals("Set<String>", setter.getParameters().get(0).getType());
   }

   @Test
   public void testCreateToStringFromFields() throws Exception
   {
      assertFalse(javaClass.hasMethodSignature("toString"));
      Refactory.createToStringFromFields(javaClass);
      assertTrue(javaClass.hasMethodSignature("toString"));
      assertTrue(javaClass.getMethod("toString").getBody().contains("return"));
      assertTrue(javaClass.getMethod("toString").getBody().contains("firstName != null"));
   }
}
