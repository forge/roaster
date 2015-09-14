/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.is;

import java.util.Enumeration;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class MethodImplementationTest
{
   @Test
   public void testJavaClassAddInterfaceWithReflectedMethod() throws Exception
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      Class<?> type = Enumeration.class;
      source.implementInterface(type);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("hasMoreElements"));
      Assert.assertNotNull(source.getMethod("nextElement"));
      Assert.assertThat(source.getMethod("hasMoreElements").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("nextElement").isAbstract(), is(false));
   }

   @Test
   public void testJavaInterfaceAddInterfaceWithReflectedMethod() throws Exception
   {
      JavaInterfaceSource source = Roaster.create(JavaInterfaceSource.class);
      Class<?> type = Enumeration.class;
      source.implementInterface(type);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("hasMoreElements"));
      Assert.assertNotNull(source.getMethod("nextElement"));
      Assert.assertThat(source.getMethod("hasMoreElements").isAbstract(), is(true));
      Assert.assertThat(source.getMethod("nextElement").isAbstract(), is(true));
   }

   @Test
   public void testJavaEnumAddInterfaceWithReflectedMethod() throws Exception
   {
      JavaEnumSource source = Roaster.create(JavaEnumSource.class);
      Class<?> type = Enumeration.class;
      source.implementInterface(type);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("hasMoreElements"));
      Assert.assertNotNull(source.getMethod("nextElement"));
      Assert.assertThat(source.getMethod("hasMoreElements").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("nextElement").isAbstract(), is(false));
   }

}
