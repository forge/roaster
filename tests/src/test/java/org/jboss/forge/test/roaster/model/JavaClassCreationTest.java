/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.lang.annotation.Documented;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassCreationTest
{
   private static JavaClassSource jc;

   @BeforeClass
   public static void testCreateClass() 
   {
      jc = Roaster.create(JavaClassSource.class);
   }

   @Test
   public void testClassCreatesStub() 
   {
      assertEquals("JavaClass", jc.getName());
      assertTrue(jc.isPublic());
   }

   @Test
   public void testImportStatementHasEmptyLineBeforeClassDeclaration() 
   {
      String lineSeparator = System.getProperty("line.separator");
      String expected = "package org.foo;" + lineSeparator +
               lineSeparator +
               "import java.lang.annotation.Documented;" + lineSeparator + lineSeparator +
               "@Documented" + lineSeparator +
               "public class JavaClass {" + lineSeparator +
               "}";

      jc.setPackage("org.foo").addAnnotation(Documented.class);
      assertEquals(expected, jc.toString());
   }

}
