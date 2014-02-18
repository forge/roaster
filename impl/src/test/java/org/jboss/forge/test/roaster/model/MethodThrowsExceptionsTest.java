/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
   public void testParseThrowsNone() throws Exception
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");
      assertTrue(method.getThrownExceptions().isEmpty());
   }

   @Test
   public void testParseThrowsOne() throws Exception, RuntimeException
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception");
      assertEquals(1, method.getThrownExceptions().size());
   }

   @Test
   public void testParseThrowsMany() throws Exception
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception, RuntimeException");
      assertEquals(2, method.getThrownExceptions().size());
   }

   @Test
   public void testAddThrowsOne() throws Exception, RuntimeException
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)").addThrows(Exception.class);
      assertEquals(1, method.getThrownExceptions().size());
      assertTrue(method.getOrigin().hasImport(Exception.class));
   }

   @Test
   public void testAddThrowsMany() throws Exception
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)").addThrows(Exception.class).addThrows(RuntimeException.class);
      assertEquals(2, method.getThrownExceptions().size());
   }

   @Test
   public void testRemoveThrowsNone() throws Exception, RuntimeException
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)").removeThrows(Exception.class);
      assertEquals(0, method.getThrownExceptions().size());
   }

   @Test
   public void testRemoveThrowsOne() throws Exception, RuntimeException
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception").removeThrows(Exception.class);
      assertEquals(0, method.getThrownExceptions().size());
   }

   @Test
   public void testRemoveThrowsMany() throws Exception
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar) throws Exception, RuntimeException")
               .removeThrows(Exception.class);
      assertEquals(1, method.getThrownExceptions().size());
   }
}
