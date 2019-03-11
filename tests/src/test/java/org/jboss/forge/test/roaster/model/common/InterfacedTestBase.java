/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import java.io.Serializable;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @param <T> the type of interface to test
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class InterfacedTestBase<T extends JavaSource<T> & InterfaceCapableSource<T>>
{

   private T source;

   @BeforeEach
   public void reset()
   {
      this.source = getSource();
   }

   protected abstract T getSource();

   @Test
   public void testAddInterfaceString()
   {
      assertFalse(this.source.hasInterface("com.foo.Bar"));
      assertFalse(this.source.hasInterface("java.io.Serializable"));
      this.source.addInterface("java.io.Serializable");
      assertTrue(this.source.hasInterface("java.io.Serializable"));
   }

   @Test
   public void testAddInterfaceClass()
   {
      assertFalse(this.source.hasInterface(Serializable.class));
      this.source.addInterface(Serializable.class);
      assertTrue(this.source.hasInterface(Serializable.class));
   }

   @Test
   public void testAddInterfaceJavaInterface()
   {
      JavaInterfaceSource i2 = Roaster.parse(JavaInterfaceSource.class, "package com.foo; public interface Bar<T> {}");
      assertFalse(this.source.hasInterface(i2));
      this.source.addInterface(i2);
      assertTrue(this.source.hasImport(i2));
      assertTrue(this.source.hasInterface(i2));
      assertTrue(this.source.hasInterface(i2.getQualifiedName()));
   }

   @Test
   public void testAddInterfaceWithConflictingImport()
   {
      final JavaInterfaceSource iface = Roaster.parse(JavaInterfaceSource.class,
               "package com.foo; public interface List {}");
      assertFalse(source.hasInterface(iface));
      assertEquals(0, source.getInterfaces().size());
      source.addImport("java.util.List");
      source.addInterface(iface);
      assertEquals(1, source.getImports().size(), "No imports should have been added.");
      assertTrue(source.hasImport("java.util.List"));
      assertFalse(source.hasImport("com.foo.List"));
      assertEquals(1, source.getInterfaces().size());
   }

   @Test
   public void testAddImproperInterface()
   {
      assertThrows(IllegalArgumentException.class, () -> this.source.addInterface("43 23omg.omg.omg"));
   }

   @Test
   public void testGetInterfaces()
   {
      this.source.addInterface(Serializable.class);
      this.source.addInterface("com.example.Custom");
      this.source.addInterface("com.other.Custom");
      assertEquals(3, this.source.getInterfaces().size());
      assertTrue(this.source.hasInterface("com.example.Custom"));
      assertTrue(this.source.hasInterface("com.other.Custom"));
      assertTrue(this.source.hasImport(Serializable.class));
      assertTrue(this.source.hasImport("com.example.Custom"));
      assertFalse(this.source.hasImport("com.other.Custom"));
   }

   @Test
   public void testRemoveInterface()
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

   @Test
   public void testHashCode()
   {
      this.source.hashCode();
   }
}