/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:salemelrahal@gmail.com">Salem Elrahal</a>
 */
public class MethodModifierTest
{
   @Test
   public void testDuplicateMethodModifier()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public static void test()");
      method.setStatic(true);
      Assert.assertThat(method.toString(), not(containsString("static static")));
      Assert.assertThat(method.toString(), containsString("static"));
   }

   @Test
   public void testNativeModifier()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void test(){}");
      Assert.assertFalse(method.isNative());
      method.setNative(true);
      Assert.assertTrue(method.isNative());
      Assert.assertThat(method.toString(), containsString("public native void test();"));
      Assert.assertNull(method.getBody());
      String body = "int a=2;";
      method.setNative(false).setBody(body);
      Assert.assertFalse(method.isNative());
      Assert.assertEquals(body, method.getBody());
   }

   @Test
   public void testDefaultModifier()
   {
      MethodSource<JavaInterfaceSource> method = Roaster.create(JavaInterfaceSource.class).addMethod(
               "public void test()");
      Assert.assertFalse(method.isDefault());
      method.setDefault(true);
      Assert.assertTrue(method.isDefault());
      Assert.assertThat(method.toString(), containsString("public default void test()"));
      Assert.assertThat(method.getBody(), equalTo(""));
      String body = "int a=2;";
      method.setDefault(false).setBody(body);
      Assert.assertFalse(method.isDefault());
      Assert.assertEquals(body, method.getBody());
   }

   @Test
   public void testNativeMethodBody()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public native void test();");
      Assert.assertTrue(method.isNative());
      Assert.assertNull(method.getBody());
      method.setNative(false);
      Assert.assertFalse(method.isNative());
      Assert.assertNotNull(method.getBody());
   }

   @Test
   public void testAbstractMethodBody()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public abstract void test();");
      Assert.assertTrue(method.isAbstract());
      Assert.assertNull(method.getBody());
      method.setAbstract(false);
      Assert.assertFalse(method.isAbstract());
      Assert.assertNotNull(method.getBody());
   }

   @Test
   public void testSynchronizedMethod()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void test(){}");
      Assert.assertFalse(method.isSynchronized());
      method.setSynchronized(true);
      Assert.assertTrue(method.isSynchronized());
      method.setSynchronized(false);
      Assert.assertFalse(method.isSynchronized());
   }
}