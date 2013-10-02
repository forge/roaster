/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassCreationTest
{
   private static JavaClass jc;

   @BeforeClass
   public static void testCreateClass() throws Exception
   {
      jc = JavaParser.create(JavaClass.class);
   }

   @Test
   public void testClassCreatesStub() throws Exception
   {
      assertEquals("JavaClass", jc.getName());
      assertTrue(jc.isPublic());
   }

}
