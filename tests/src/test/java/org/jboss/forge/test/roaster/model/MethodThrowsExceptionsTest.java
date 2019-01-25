/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ConcurrentModificationException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodThrowsExceptionsTest
{
   @Test
   public void testParseThrowsNone()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");
      assertTrue(method.getThrownExceptions().isEmpty());
   }

   @Test
   public void testParseThrowsOne()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception");
      assertEquals(1, method.getThrownExceptions().size());
   }

   @Test
   public void testParseThrowsMany()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception, RuntimeException");
      assertEquals(2, method.getThrownExceptions().size());
   }

   @Test
   public void testAddThrowsOne()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)").addThrows(ConcurrentModificationException.class);
      assertEquals(1, method.getThrownExceptions().size());
      assertTrue(method.getOrigin().hasImport(ConcurrentModificationException.class));
   }

   @Test
   public void testAddThrowsMany()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)").addThrows(Exception.class).addThrows(RuntimeException.class);
      assertEquals(2, method.getThrownExceptions().size());
   }

   @Test
   public void testRemoveThrowsNone()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)").removeThrows(Exception.class);
      assertEquals(0, method.getThrownExceptions().size());
   }

   @Test
   public void testRemoveThrowsOne()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception").removeThrows(Exception.class);
      assertEquals(0, method.getThrownExceptions().size());
   }

   @Test
   public void testRemoveThrowsMany()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception, RuntimeException")
               .removeThrows(Exception.class);
      assertEquals(1, method.getThrownExceptions().size());
   }

   @Test
   public void testAddThrowsExceptionShouldNotImportJavaLang()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addMethod().setName("hello").addThrows(Exception.class);
      assertEquals(0, javaClass.getImports().size());
   }
}
