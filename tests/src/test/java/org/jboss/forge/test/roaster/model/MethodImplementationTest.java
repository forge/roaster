/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

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
import org.junit.Assert;
import org.junit.Test;

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
      Assert.assertThat(source.getMethods().size(), is(1));
      Assert.assertNotNull(source.getMethod("doSomething"));
      Assert.assertThat(source.getMethod("doSomething").isAbstract(), is(false));
      Assert.assertThat(source.getMethod("doSomething").isPublic(), is(true));
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
      Assert.assertThat(source.getMethod("doSomething", String.class).getAnnotation(MockAnnotation.class), nullValue());
      Assert.assertThat(
               source.getMethod("doSomething", String.class).getParameters().get(0).getAnnotation(MockAnnotation.class),
               nullValue());
   }

   @Test
   public void testJavaClassExtendJavaClass()
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
   public void testJavaEnumImplementInterface()
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
      Assert.assertThat(annotation, nullValue());
      Assert.assertThat(
               method.getParameters().get(0).getAnnotation(MockAnnotation.class),
               nullValue());
   }

   @Test
   public void testJavaClassExtendSuperTypeWithReflectedMethod()
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
   public void testJavaClassImplementInterfaceWithReflectedMethod()
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
   public void testJavaInterfaceImplementInterfaceWithReflectedMethod()
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
   public void testJavaEnumImplementInterfaceWithReflectedMethod()
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
   public void testCopyMethod()
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

   @Test
   public void testJavaClassImplementInterfaceWithReflectedMethods()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.implementInterface(MockInterface.class);
      Assert.assertThat(source.getMethods().size(), is(3));
      Assert.assertThat(source.getMethod("lookup", String.class, boolean.class), notNullValue());
      Assert.assertThat(source.getMethod("lookup", int.class, boolean.class), notNullValue());
      Assert.assertThat(source.getMethod("lookup", int.class, int.class, boolean.class), notNullValue());
   }

   @Test
   public void testJavaClassImplementInterfaceWithReflectedMethodsWithAnnotation()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      source.implementInterface(MockAnnotatedInterface.class);
      Assert.assertThat(source.getMethod("lookup", String.class, boolean.class).getAnnotation(MockAnnotation.class),
               nullValue());
      Assert.assertThat(source.getMethod("lookup", String.class, boolean.class).getParameters().get(0)
               .getAnnotation(MockAnnotation.class), nullValue());
      Assert.assertThat(source.getMethod("lookup", String.class, boolean.class).getParameters().get(1)
               .getAnnotation(MockAnnotation.class), nullValue());
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

      Assert.assertThat(javaClass.getMethods().size(), equalTo(1));
   }

   @Test
   public void testImplementedMethodShouldBePublic()
   {
      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setPackage("test");
      interfaceSource.addMethod().setName("foo");
      JavaClassSource implSource = Roaster.create(JavaClassSource.class);
      implSource.implementInterface(interfaceSource);
      Assert.assertEquals(Visibility.PUBLIC, interfaceSource.getMethod("foo").getVisibility());
      Assert.assertEquals(Visibility.PUBLIC, implSource.getMethod("foo").getVisibility());
   }

   @Test
   public void testOmitImportsOfDefaultImplementations()
   {
      final String packageName = "test";
      final String className = "java.util.List";

      JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class).setPackage(packageName);
      interfaceSource.addMethod().setDefault(true).setName("foo").addParameter(className, "list");
      Assert.assertEquals("Interface should contain a single import", 1, interfaceSource.getImports().size());
      final Import listImport = interfaceSource.getImport(className);
      Assert.assertNotNull("Import of '" + className + "' not found", listImport);

      JavaClassSource implSource = Roaster.create(JavaClassSource.class).setPackage(packageName);
      implSource.implementInterface(interfaceSource);

      final Import implListImport = implSource.getImport(className);
      Assert.assertNull("Import of '" + className + "' should not exist.", implListImport);
   }

   @Test(expected = ParserException.class)
   public void testMethodBodyShouldNotBeEmptyOnInvalidCode()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo");
      method.setBody("{}{{}{dasfasdfasdfga");
   }

   @Test
   public void testEmptyMethodBodyShouldNotThrowException()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo");
      method.setBody("");
      Assert.assertThat(method.getBody(), equalTo(""));
   }

   @Test
   public void testMethodBodyShouldParseCorrectly()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("foo");
      method.setBody("System.out.println(\"Hello World\");");
      Assert.assertThat(method.getBody(), equalTo("System.out.println(\"Hello World\");"));
   }
}