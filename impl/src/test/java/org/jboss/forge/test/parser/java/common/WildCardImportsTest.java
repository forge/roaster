/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java.common;

import static org.junit.Assert.assertTrue;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.junit.Test;

public class WildCardImportsTest
{

   @Test
   public void testImportWithWildCard() throws ClassNotFoundException
   {
      JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addImport("org.junit.Assert.*");
      assertTrue(javaClass.getImport("org.junit.Assert") != null);
      assertTrue(javaClass.getImport("org.junit.Assert").isWildcard());
   }

   @Test
   public void testImportStaticAndWithWildCard() throws ClassNotFoundException
   {
      JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addImport("org.junit.Assert.*")
               .setStatic(true);
      assertTrue(javaClass.getImport("org.junit.Assert") != null);
      assertTrue(javaClass.getImport("org.junit.Assert").isStatic());
      assertTrue(javaClass.getImport("org.junit.Assert").isWildcard());
   }

}
