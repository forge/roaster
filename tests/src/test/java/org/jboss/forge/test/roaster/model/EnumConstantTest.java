/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Strings;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Matt Benson
 */
public class EnumConstantTest
{
   private JavaEnumSource javaEnum;

   @Before
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockEnum.java";
      try (InputStream stream = EnumConstantTest.class.getResourceAsStream(fileName))
      {
         javaEnum = Roaster.parse(JavaEnumSource.class, stream);
      }
   }

   @Test
   public void testSetConstructorArguments()
   {
      int i = javaEnum.getMethods().size();
      javaEnum.addMethod().setConstructor(true).setParameters("int n, String s");
      assertEquals("int", javaEnum.getMethods().get(i).getParameters().get(0).getType().getName());
      EnumConstantSource foo = javaEnum.getEnumConstant("FOO");
      assertTrue(foo.getConstructorArguments().isEmpty());
      foo.setConstructorArguments("666", "null");
      assertEquals(2, foo.getConstructorArguments().size());
      assertEquals("666", foo.getConstructorArguments().get(0));
      assertEquals("null", foo.getConstructorArguments().get(1));
      foo.setConstructorArguments((String[]) null);
      assertTrue(foo.getConstructorArguments().isEmpty());
      foo.setConstructorArguments("0", "\"foo\"");
      assertEquals(2, foo.getConstructorArguments().size());
      assertEquals("0", foo.getConstructorArguments().get(0));
      assertEquals("\"foo\"", foo.getConstructorArguments().get(1));
      foo.setConstructorArguments();
      assertTrue(foo.getConstructorArguments().isEmpty());
   }

   @Test
   public void testAddRemoveBody()
   {
      EnumConstantSource foo = javaEnum.getEnumConstant("FOO");
      final Pattern emptyBody = Pattern.compile("\\{\\s*\\}$");
      assertFalse(emptyBody.matcher(foo.toString()).find());
      foo.getBody();
      assertTrue(emptyBody.matcher(foo.toString()).find());
      foo.removeBody();
      assertFalse(emptyBody.matcher(foo.toString()).find());
   }

   @Test
   public void testBodyAssertions()
   {
      EnumConstantSource foo = javaEnum.getEnumConstant("FOO");
      EnumConstantSource.Body body = foo.getBody();
      assertTrue(body.isClass());
      assertFalse(body.isInterface());
      assertFalse(body.isEnum());
      assertFalse(body.isAnnotation());
      assertEquals(javaEnum, body.getEnclosingType());
   }

   @Test
   public void testAddRemoveBodyImports()
   {
      EnumConstantSource foo = javaEnum.getEnumConstant("FOO");
      EnumConstantSource.Body body = foo.getBody();
      assertFalse(body.hasImport(Strings.class));
      assertFalse(javaEnum.hasImport(Strings.class));
      body.addImport(Strings.class);
      assertTrue(body.hasImport(Strings.class));
      assertTrue(javaEnum.hasImport(Strings.class));
      body.removeImport(Strings.class);
      assertFalse(body.hasImport(Strings.class));
      assertFalse(javaEnum.hasImport(Strings.class));
   }

   @Test
   public void testAddRemoveBodyMethod()
   {
      EnumConstantSource foo = javaEnum.getEnumConstant("FOO");
      EnumConstantSource.Body body = foo.getBody();
      MethodSource<EnumConstantSource.Body> fooAction = body.addMethod().setName("fooAction").setReturnType(Void.TYPE);
      assertEquals(fooAction, body.getMethods().get(0));
      assertEquals(fooAction, body.getMembers().get(0));
      body.removeMethod(fooAction);
      assertTrue(body.getMethods().isEmpty());
   }

   @Test
   public void testAddRemoveBodyField()
   {
      EnumConstantSource foo = javaEnum.getEnumConstant("FOO");
      EnumConstantSource.Body body = foo.getBody();
      FieldSource<EnumConstantSource.Body> fooField = body.addField().setName("fooField").setType(String.class);
      assertEquals(fooField, body.getFields().get(0));
      assertEquals(fooField, body.getMembers().get(0));
      body.removeField(fooField);
      assertTrue(body.getFields().isEmpty());
   }

}
