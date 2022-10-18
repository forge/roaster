/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

      assertTrue(javaInterface.hasImport("java.util.List"), "The interface does not import java.util.List properly");
      assertTrue(javaImplementation.hasImport("java.util.List"),
               "The implementation does not import java.util.List properly");

   }

   @Test
   public void testGenericTypesExtendsInterfaceDeclaration()
   {
      String data = "import com.foo.Bar;\n"
               + "public interface Foo extends Bar<String,Integer>{}";
      JavaInterfaceSource iface = Roaster.parse(JavaInterfaceSource.class, data);
      assertThat(iface.getInterfaces()).contains("com.foo.Bar<String,Integer>");
   }

   @Test
   public void testImportInterfaceFromSamePackage()
   {
      JavaClassSource javaImplementation = Roaster.parse(JavaClassSource.class,
               "package com.foo.forge; public class MockClass implements MyInterface {}");
      assertThat(javaImplementation.getInterfaces()).contains("com.foo.forge.MyInterface");
      javaImplementation.addImport("com.foo.forge.MyInterface");
      assertThat(javaImplementation.getInterfaces()).contains("com.foo.forge.MyInterface");
   }

   @Test
   public void testExtendDifferentPackage() {
      JavaInterfaceSource outer = Roaster.create(JavaInterfaceSource.class)
               .setPackage("outer")
               .setName("Buggy")
               .setPublic();

      JavaInterfaceSource inner = Roaster.create(JavaInterfaceSource.class)
               .setPackage("outer.inner")
               .setName("Buggy")
               .setPublic();

      inner.addInterface(outer);
      assertThat(inner.toString()).contains("public interface Buggy extends outer.Buggy");
   }

   @Test
   public void testExtendIfImportAlreadyExists() {

      JavaInterfaceSource foo = Roaster.create(JavaInterfaceSource.class)
               .setPackage("org.pkg")
               .setName("Foo")
               .setPublic();

      JavaInterfaceSource bar = Roaster.create(JavaInterfaceSource.class)
               .setPackage("org.pkg")
               .setName("Bar")
               .setPublic();

      foo.addImport(bar);
      foo.addInterface(bar);
      assertThat(foo.toString()).contains("public interface Foo extends Bar");
   }
}
