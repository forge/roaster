/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Enumeration;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.test.roaster.model.common.MockSuperType;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class MethodImplementationTest
{
   @Test
   public void testJavaClassImplementInterface() throws Exception
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setName("Bar").setPackage("test");
      interfaceSource.addMethod().setAbstract(true).setName("doSomething").setReturnTypeVoid();
      source.implementInterface(interfaceSource);
      Assert.assertThat(source.getMethods().size(), is(1));
      Assert.assertNotNull(source.getMethod("doSomething"));
      Assert.assertThat(source.getMethod("doSomething").isAbstract(), is(false));
   }

   @Test
   public void testJavaClassExtendJavaClass() throws Exception
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      JavaClassSource superType = Roaster.create(JavaClassSource.class).setName("Bar").setPackage("test");
      superType.addMethod().setAbstract(true).setName("doSomething").setReturnTypeVoid();
      source.extendSuperType(superType);
      Assert.assertThat(source.getMethods().size(), is(1));
      Assert.assertNotNull(source.getMethod("doSomething"));
      Assert.assertThat(source.getMethod("doSomething").isAbstract(), is(false));
      Assert.assertEquals("test.Bar", source.getSuperType());
   }

   @Test
   public void testJavaEnumImplementInterface() throws Exception
   {
      JavaEnumSource source = Roaster.create(JavaEnumSource.class);
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setName("Bar").setPackage("test");
      interfaceSource.addMethod().setAbstract(true).setName("doSomething").setReturnTypeVoid();
      source.implementInterface(interfaceSource);
      Assert.assertThat(source.getMethods().size(), is(1));
      Assert.assertNotNull(source.getMethod("doSomething"));
      Assert.assertThat(source.getMethod("doSomething").isAbstract(), is(false));
   }

   @Test
   public void testJavaClassExtendSuperTypeWithReflectedMethod() throws Exception
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.extendSuperType(MockSuperType.class);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("doSomething"));
      Assert.assertNotNull(source.getMethod("returnSomething"));
      Assert.assertThat(source.getMethod("doSomething").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("returnSomething").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("returnSomething").getBody(), equalTo("return null;"));
   }

   @Test
   public void testJavaClassImplementInterfaceWithReflectedMethod() throws Exception
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.implementInterface(Enumeration.class);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("hasMoreElements"));
      Assert.assertNotNull(source.getMethod("nextElement"));
      Assert.assertThat(source.getMethod("hasMoreElements").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("nextElement").isAbstract(), is(false));
   }

   @Test
   public void testJavaInterfaceImplementInterfaceWithReflectedMethod() throws Exception
   {
      JavaInterfaceSource source = Roaster.create(JavaInterfaceSource.class);
      source.implementInterface(Enumeration.class);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("hasMoreElements"));
      Assert.assertNotNull(source.getMethod("nextElement"));
      Assert.assertThat(source.getMethod("hasMoreElements").isAbstract(), is(true));
      Assert.assertThat(source.getMethod("nextElement").isAbstract(), is(true));
   }

   @Test
   public void testJavaEnumImplementInterfaceWithReflectedMethod() throws Exception
   {
      JavaEnumSource source = Roaster.create(JavaEnumSource.class);
      source.implementInterface(Enumeration.class);
      Assert.assertThat(source.getMethods().size(), is(2));
      Assert.assertNotNull(source.getMethod("hasMoreElements"));
      Assert.assertNotNull(source.getMethod("nextElement"));
      Assert.assertThat(source.getMethod("hasMoreElements").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("nextElement").isAbstract(), is(false));
   }

   @Test
   public void testCopyMethod() throws Exception
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo").setReturnTypeVoid();
      method.addParameter(String.class, "bar");
      Methods.implementMethod(method);

      JavaClassSource target = Roaster.create(JavaClassSource.class);
      target.addMethod(method);

      Assert.assertThat(target.getMethods().size(), is(1));
      Assert.assertNotNull(source.getMethod("foo", String.class));
   }

}
