/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaInterfaceTest
{
   @Test
   public void testCanParseInterface() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockInterface.java";
      try (InputStream stream = JavaInterfaceTest.class.getResourceAsStream(fileName))
      {
         JavaInterfaceSource javaClass = Roaster.parse(JavaInterfaceSource.class, stream);
         String name = javaClass.getName();
         assertEquals("MockInterface", name);
      }
   }

   @Test
   public void testCanParseBigInterface() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/BigInterface.java";
      try (InputStream stream = JavaInterfaceTest.class.getResourceAsStream(fileName))
      {
         JavaInterfaceSource javaClass = Roaster.parse(JavaInterfaceSource.class, stream);
         String name = javaClass.getName();
         assertEquals("BigInterface", name);
         List<MemberSource<JavaInterfaceSource, ?>> members = javaClass.getMembers();
         assertFalse(members.isEmpty());
      }
   }

   @Test
   public void testImportJavaSource()
   {
      JavaInterfaceSource foo = Roaster.parse(JavaInterfaceSource.class,
               "package org.jboss.forge; public interface Foo{}");
      JavaInterfaceSource bar = Roaster.parse(JavaInterfaceSource.class,
               "package org.jboss.forge; public interface Bar{}");

      assertFalse(foo.hasImport(bar));
      assertFalse(bar.hasImport(foo));

      Import importBar = foo.addImport(bar);
      assertTrue(foo.hasImport(bar));
      assertFalse(bar.hasImport(foo));

      assertEquals("org.jboss.forge.Bar", importBar.getQualifiedName());
      assertEquals(importBar, foo.getImport(bar));

      foo.removeImport(bar);
      assertFalse(foo.hasImport(bar));
      assertFalse(bar.hasImport(foo));
   }

   @Test
   public void testImportImport()
   {
      JavaInterfaceSource foo = Roaster.parse(JavaInterfaceSource.class, "public interface Foo{}");
      Import i = foo.addImport(getClass());

      foo.removeImport(getClass());
      Import i2 = foo.addImport(i);
      assertNotSame(i, i2);
      assertEquals(i.getQualifiedName(), i2.getQualifiedName());
   }

   @Test
   public void testStatic()
   {
      JavaInterfaceSource iface = Roaster.parse(JavaInterfaceSource.class, "public interface Foo{}");
      iface.setStatic(true);
      assertTrue(iface.isStatic());
      iface.setStatic(false);
      assertFalse(iface.isStatic());
   }

   @Test
   public void testImportInterface()
   {
      JavaInterfaceSource javaInterface = Roaster.create(JavaInterfaceSource.class);
      javaInterface.setName("MyInterface");
      javaInterface.setPackage("org.jboss.forge");

      MethodSource<JavaInterfaceSource> methodSource = javaInterface.addMethod();
      methodSource.setName("methodExample");
      methodSource.addParameter("java.util.List<String>", "listParameter");

      JavaClassSource javaImplementation = Roaster.create(JavaClassSource.class);
      javaImplementation.implementInterface(javaInterface);

      assertTrue("The interface does not import java.util.List properly", javaInterface.hasImport("java.util.List"));
      assertTrue("The implementation does not import java.util.List properly",
               javaImplementation.hasImport("java.util.List"));

   }

   @Test
   public void testGenericTypesExtendsInterfaceDeclaration()
   {
      String data = "import com.foo.Bar;\n"
               + "public interface Foo extends Bar<String,Integer>{}";
      JavaInterfaceSource iface = Roaster.parse(JavaInterfaceSource.class, data);
      assertThat(iface.getInterfaces(), hasItem("com.foo.Bar<String,Integer>"));
   }

   @Test
   public void testImportInterfaceFromSamePackage()
   {
      JavaClassSource javaImplementation = Roaster.parse(JavaClassSource.class,
               "package com.foo.forge; public class MockClass implements MyInterface {}");
      assertThat(javaImplementation.getInterfaces(), hasItem("com.foo.forge.MyInterface"));
      javaImplementation.addImport("com.foo.forge.MyInterface");
      assertThat(javaImplementation.getInterfaces(), hasItem("com.foo.forge.MyInterface"));
   }

}
