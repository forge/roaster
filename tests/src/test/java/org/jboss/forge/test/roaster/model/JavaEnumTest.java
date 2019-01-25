/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.EnumConstant;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumTest
{
   private JavaEnumSource javaEnum;

   @Before
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockEnum.java";
      try (InputStream stream = JavaEnumTest.class.getResourceAsStream(fileName))
      {
         javaEnum = Roaster.parse(JavaEnumSource.class, stream);
      }
   }

   @Test
   public void testCanParseEnum()
   {
      assertEquals("MockEnum", javaEnum.getName());
   }

   @Test
   @SuppressWarnings("rawtypes")
   public void testAddEnumConstant()
   {
      int i = javaEnum.getEnumConstants().size();
      EnumConstant enumConstant = javaEnum.addEnumConstant().setName("BLAH");
      assertEquals(i + 1, javaEnum.getEnumConstants().size());
      assertEquals("BLAH", enumConstant.getName());
   }

   @Test
   @SuppressWarnings("rawtypes")
   public void testAddEnumConstantFromDeclaration()
   {
      int i = javaEnum.getEnumConstants().size();
      EnumConstant enumConstant = javaEnum.addEnumConstant("BLAH");
      assertEquals(i + 1, javaEnum.getEnumConstants().size());
      assertEquals("BLAH", enumConstant.getName());
   }

   @Test
   public void testAddEnumField()
   {
      int i = javaEnum.getFields().size();
      FieldSource<JavaEnumSource> fld = javaEnum.addField().setName("fld").setType(Integer.TYPE)
               .setVisibility(Visibility.PRIVATE);
      assertEquals(i + 1, javaEnum.getFields().size());
      assertEquals("fld", fld.getName());
      assertEquals(Integer.TYPE.getName(), fld.getType().getName());
      assertSame(Visibility.PRIVATE, fld.getVisibility());
   }

   @Test
   public void testAddEnumMethod()
   {
      int i = javaEnum.getMethods().size();
      MethodSource<JavaEnumSource> method = javaEnum.addMethod().setName("something").setReturnType(Void.TYPE)
               .setVisibility(Visibility.PUBLIC);
      assertEquals(i + 1, javaEnum.getMethods().size());
      assertEquals("something", method.getName());
      assertTrue(method.getParameters().isEmpty());
      assertSame(Visibility.PUBLIC, method.getVisibility());
   }

   @Test
   public void testStatic()
   {
      javaEnum.setStatic(true);
      assertTrue(javaEnum.isStatic());
      javaEnum.setStatic(false);
      assertFalse(javaEnum.isStatic());
   }

}
