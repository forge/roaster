/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public abstract class InterfacedTestBase<T extends JavaSource<T> & InterfaceCapableSource<T>>
{

   private T source;

   @Before
   public void reset()
   {
      this.source = getSource();
   }

   protected abstract T getSource();

   @Test
   public void testAddInterfaceString() throws Exception
   {
      assertFalse(this.source.hasInterface("com.foo.Bar"));
      assertFalse(this.source.hasInterface("java.io.Serializable"));
      this.source.addInterface("java.io.Serializable");
      assertTrue(this.source.hasInterface("java.io.Serializable"));
   }

   @Test
   public void testAddInterfaceClass() throws Exception
   {
      assertFalse(this.source.hasInterface(Serializable.class));
      this.source.addInterface(Serializable.class);
      assertTrue(this.source.hasInterface(Serializable.class));
   }

   @Test
   public void testAddInterfaceJavaInterface() throws Exception
   {
      JavaInterfaceSource i2 = Roaster.parse(JavaInterfaceSource.class, "package com.foo; public interface Bar<T> {}");
      assertFalse(this.source.hasInterface(i2));
      this.source.addInterface(i2);
      assertTrue(this.source.hasImport(i2));
      assertTrue(this.source.hasInterface(i2));
      assertTrue(this.source.hasInterface(i2.getQualifiedName()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddImproperInterface() throws Exception
   {
      this.source.addInterface("43 23omg.omg.omg");
      fail();
   }

   @Test
   public void testGetInterfaces() throws Exception
   {
      this.source.addInterface(Serializable.class);
      this.source.addInterface("com.example.Custom");
      this.source.addInterface("com.other.Custom");
      assertEquals(3, this.source.getInterfaces().size());
      assertTrue(this.source.hasInterface("com.example.Custom"));
      assertTrue(this.source.hasInterface("com.other.Custom"));
      assertTrue(this.source.hasImport(Serializable.class));
      assertTrue(this.source.hasImport("com.example.Custom"));
      assertTrue(this.source.hasImport("com.other.Custom"));
   }

   @Test
   public void testRemoveInterface() throws Exception
   {
      this.source.addInterface(Serializable.class);
      this.source.addInterface("com.example.Custom");
      this.source.addInterface("com.other.Custom");
      assertEquals(3, this.source.getInterfaces().size());
      assertTrue(this.source.hasInterface("com.example.Custom"));
      assertTrue(this.source.hasInterface("com.other.Custom"));
      assertTrue(this.source.hasImport(Serializable.class));

      this.source.removeInterface(Serializable.class);
      assertFalse(this.source.hasInterface(Serializable.class));
      assertEquals(2, this.source.getInterfaces().size());
   }
}
