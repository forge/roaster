/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
      assertThat(method.toString()).doesNotContain("static static");
      assertThat(method.toString()).contains("static");
   }

   @Test
   public void testNativeModifier()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void test(){}");
      assertFalse(method.isNative());
      method.setNative(true);
      assertTrue(method.isNative());
      assertThat(method.toString()).contains("public native void test();");
      assertNull(method.getBody());
      String body = "int a=2;";
      method.setNative(false).setBody(body);
      assertFalse(method.isNative());
      assertEquals(body, method.getBody());
   }

   @Test
   public void testDefaultModifier()
   {
      MethodSource<JavaInterfaceSource> method = Roaster.create(JavaInterfaceSource.class).addMethod(
               "public void test()");
      assertFalse(method.isDefault());
      method.setDefault(true);
      assertTrue(method.isDefault());
      assertThat(method.toString()).contains("public default void test()");
      assertThat(method.getBody()).isEmpty();
      String body = "int a=2;";
      method.setDefault(false).setBody(body);
      assertFalse(method.isDefault());
      assertEquals(body, method.getBody());
   }

   @Test
   public void testNativeMethodBody()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public native void test();");
      assertTrue(method.isNative());
      assertNull(method.getBody());
      method.setNative(false);
      assertFalse(method.isNative());
      assertNotNull(method.getBody());
   }

   @Test
   public void testAbstractMethodBody()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public abstract void test();");
      assertTrue(method.isAbstract());
      assertNull(method.getBody());
      method.setAbstract(false);
      assertFalse(method.isAbstract());
      assertNotNull(method.getBody());
   }

   @Test
   public void testSynchronizedMethod()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void test(){}");
      assertFalse(method.isSynchronized());
      method.setSynchronized(true);
      assertTrue(method.isSynchronized());
      method.setSynchronized(false);
      assertFalse(method.isSynchronized());
   }
}