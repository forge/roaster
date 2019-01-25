/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Test;

public class WildCardImportsTest
{

   @Test
   public void testImportWithWildCard()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addImport("org.junit.Assert.*");
      assertTrue(javaClass.getImport("org.junit.Assert") != null);
      assertTrue(javaClass.getImport("org.junit.Assert").isWildcard());
   }

   @Test
   public void testImportWithGenerics()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addImport("package1.Class1<package2.Class2>");
      assertThat(javaClass.getImports().size(), is(2));
   }

   @Test
   public void testImportStaticAndWithWildCard()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addImport("org.junit.Assert.*")
               .setStatic(true);
      assertTrue(javaClass.getImport("org.junit.Assert") != null);
      assertTrue(javaClass.getImport("org.junit.Assert").isStatic());
      assertTrue(javaClass.getImport("org.junit.Assert").isWildcard());
   }

   @Test
   public void testWildcardImportResolverMissing()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addImport("java.util.*");
      javaClass.addField().setName("field").setType("Date");
      FieldSource<JavaClassSource> field = javaClass.getField("field");
      Assert.assertNotNull(field);
      Assert.assertNotNull(field.getType());
      Assert.assertEquals("java.util.Date", field.getType().getQualifiedName());
   }
}