/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import java.util.Enumeration;

import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.test.roaster.model.common.MockAnnotatedInterface;
import org.jboss.forge.test.roaster.model.common.MockAnnotation;
import org.jboss.forge.test.roaster.model.common.MockInterface;
import org.jboss.forge.test.roaster.model.common.MockSuperType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class MethodImplementationTest
{
   @Test
   public void testJavaClassImplementInterface()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setName("Bar").setPackage("test");
      interfaceSource.addMethod().setAbstract(true).setName("doSomething").setReturnTypeVoid();
      source.implementInterface(interfaceSource);
      assertEquals(1, source.getMethods().size());
      assertNotNull(source.getMethod("doSomething"));
      assertFalse(source.getMethod("doSomething").isAbstract());
      assertTrue(source.getMethod("doSomething").isPublic());
   }

   @Test
   public void testJavaClassImplementInterfaceWithAnnotation()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setName("Bar").setPackage("test");
      MethodSource<JavaInterfaceSource> interfaceMethod = interfaceSource.addMethod().setAbstract(true)
               .setName("doSomething");
      interfaceMethod.addAnnotation(MockAnnotation.class);
      interfaceMethod.addParameter(String.class, "parameter").addAnnotation(MockAnnotation.class);
      source.implementInterface(interfaceSource);
      assertNull(source.getMethod("doSomething", String.class).getAnnotation(MockAnnotation.class));
      assertNull(source.getMethod("doSomething", String.class).getParameters().get(0)
               .getAnnotation(MockAnnotation.class));
   }

   @Test
   public void testJavaClassExtendJavaClass()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      JavaClassSource superType = Roaster.create(JavaClassSource.class).setName("Bar").setPackage("test");
      superType.addMethod().setAbstract(true).setName("doSomething").setReturnTypeVoid();
      source.extendSuperType(superType);
      assertEquals(1, source.getMethods().size());
      assertNotNull(source.getMethod("doSomething"));
      assertFalse(source.getMethod("doSomething").isAbstract());
      assertEquals("test.Bar", source.getSuperType());
   }

   @Test
   public void testJavaEnumImplementInterface()
   {
      JavaEnumSource source = Roaster.create(JavaEnumSource.class);
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setName("Bar").setPackage("test");
      interfaceSource.addMethod().setAbstract(true).setName("doSomething").setReturnTypeVoid();
      source.implementInterface(interfaceSource);
      assertEquals(1, source.getMethods().size());
      assertNotNull(source.getMethod("doSomething"));
      assertFalse(source.getMethod("doSomething").isAbstract());
   }

   @Test
   public void testJavaEnumImplementInterfaceWithAnnotation()
   {
      JavaEnumSource source = Roaster.create(JavaEnumSource.class);
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setName("Bar").setPackage("test");
      MethodSource<JavaInterfaceSource> interfaceMethod = interfaceSource.addMethod().setAbstract(true)
               .setName("doSomething");
      interfaceMethod.addAnnotation(MockAnnotation.class);
      interfaceMethod.addParameter(String.class, "parameter").addAnnotation(MockAnnotation.class);
      source.implementInterface(interfaceSource);
      MethodSource<JavaEnumSource> method = source.getMethod("doSomething", String.class);
      AnnotationSource<JavaEnumSource> annotation = method.getAnnotation(MockAnnotation.class);
      assertNull(annotation);
      assertNull(method.getParameters().get(0).getAnnotation(MockAnnotation.class));
   }

   @Test
   public void testJavaClassExtendSuperTypeWithReflectedMethod()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.extendSuperType(MockSuperType.class);
      assertEquals(2, source.getMethods().size());
      assertNotNull(source.getMethod("doSomething"));
      assertNotNull(source.getMethod("returnSomething"));
      assertFalse(source.getMethod("doSomething").isAbstract());
      assertFalse(source.getMethod("returnSomething").isAbstract());
      assertEquals("return null;", source.getMethod("returnSomething").getBody());
   }

   @Test
   public void testJavaClassImplementInterfaceWithReflectedMethod()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.implementInterface(Enumeration.class);
      assertEquals(2, source.getMethods().size());
      assertNotNull(source.getMethod("hasMoreElements"));
      assertNotNull(source.getMethod("nextElement"));
      assertFalse(source.getMethod("hasMoreElements").isAbstract());
      assertFalse(source.getMethod("nextElement").isAbstract());
   }

   @Test
   public void testJavaInterfaceImplementInterfaceWithReflectedMethod()
   {
      JavaInterfaceSource source = Roaster.create(JavaInterfaceSource.class);
      source.implementInterface(Enumeration.class);
      assertEquals(2, source.getMethods().size());
      assertNotNull(source.getMethod("hasMoreElements"));
      assertNotNull(source.getMethod("nextElement"));
      assertTrue(source.getMethod("hasMoreElements").isAbstract());
      assertTrue(source.getMethod("nextElement").isAbstract());
   }

   @Test
   public void testJavaEnumImplementInterfaceWithReflectedMethod()
   {
      JavaEnumSource source = Roaster.create(JavaEnumSource.class);
      source.implementInterface(Enumeration.class);
      assertEquals(2, source.getMethods().size());
      ;
      assertNotNull(source.getMethod("hasMoreElements"));
      assertNotNull(source.getMethod("nextElement"));
      assertFalse(source.getMethod("hasMoreElements").isAbstract());
      assertFalse(source.getMethod("nextElement").isAbstract());
   }

   @Test
   public void testCopyMethod()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo").setReturnTypeVoid();
      method.addParameter(String.class, "bar");
      Methods.implementMethod(method);

      JavaClassSource target = Roaster.create(JavaClassSource.class);
      target.addMethod(method);

      assertEquals(1, target.getMethods().size());
      assertNotNull(source.getMethod("foo", String.class));
   }

   @Test
   public void testJavaClassImplementInterfaceWithReflectedMethods()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.implementInterface(MockInterface.class);
      assertEquals(3, source.getMethods().size());
      assertNotNull(source.getMethod("lookup", String.class, boolean.class));
      assertNotNull(source.getMethod("lookup", int.class, boolean.class));
      assertNotNull(source.getMethod("lookup", int.class, int.class, boolean.class));
   }

   @Test
   public void testJavaClassImplementInterfaceWithReflectedMethodsWithAnnotation()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.implementInterface(MockAnnotatedInterface.class);
      assertNull(source.getMethod("lookup", String.class, boolean.class).getAnnotation(MockAnnotation.class));
      assertNull(source.getMethod("lookup", String.class, boolean.class).getParameters().get(0)
               .getAnnotation(MockAnnotation.class));
      assertNull(source.getMethod("lookup", String.class, boolean.class).getParameters().get(1)
               .getAnnotation(MockAnnotation.class));
   }

   @Test
   public void testJavaClassSourceImplementJavaInterfaceSourceMethods()
   {
      JavaInterfaceSource javaInterface = Roaster.create(JavaInterfaceSource.class);
      javaInterface.setPackage("foo");
      javaInterface.addMethod().setName("bar");
      javaInterface.addMethod().setName("aDefaultMethod").setDefault(true).setReturnTypeVoid();

      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.implementInterface(javaInterface);

      assertEquals(1, javaClass.getMethods().size());
   }

   @Test
   public void testImplementedMethodShouldBePublic()
   {
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setPackage("test");
      interfaceSource.addMethod().setName("foo");
      JavaClassSource implSource = Roaster.create(JavaClassSource.class);
      implSource.implementInterface(interfaceSource);
      assertEquals(Visibility.PUBLIC, interfaceSource.getMethod("foo").getVisibility());
      assertEquals(Visibility.PUBLIC, implSource.getMethod("foo").getVisibility());
   }

   @Test
   public void testOmitImportsOfDefaultImplementations()
   {
      final String packageName = "test";
      final String className = "java.util.List";

      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setPackage(packageName);
      interfaceSource.addMethod().setDefault(true).setName("foo").addParameter(className, "list");
      assertEquals(1, interfaceSource.getImports().size(), "Interface should contain a single import");
      final Import listImport = interfaceSource.getImport(className);
      assertNotNull(listImport, "Import of '" + className + "' not found");

      JavaClassSource implSource = Roaster.create(JavaClassSource.class).setPackage(packageName);
      implSource.implementInterface(interfaceSource);

      final Import implListImport = implSource.getImport(className);
      assertNull(implListImport, "Import of '" + className + "' should not exist.");
   }

   @Test
   public void testMethodBodyShouldNotBeEmptyOnInvalidCode()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo");
      assertThrows(ParserException.class, () -> method.setBody("{}{{}{dasfasdfasdfga"));
   }

   @Test
   public void testEmptyMethodBodyShouldNotThrowException()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo");
      method.setBody("");
      assertEquals("", method.getBody());
   }

   @Test
   public void testMethodBodyShouldParseCorrectly()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo");
      method.setBody("System.out.println(\"Hello World\");");
      assertEquals("System.out.println(\"Hello World\");", method.getBody());
   }
}