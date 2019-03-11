/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaClassGenericsTest
{

   @Test
   public void addAndRemoveGenericType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T");
      assertTrue(javaClass.getTypeVariables().get(0).getBounds().isEmpty());
      assertTrue(javaClass.toString().contains("<T>"));
      javaClass.removeTypeVariable("T");
      assertFalse(javaClass.toString().contains("<T>"));
   }

   @Test
   public void addGenericSuperTypeWithPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<T>");
      assertTrue(javaClass.toString().contains("Bar<T>"));
      assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
   }

   @Test
   public void addConcreteGenericSuperTypeWithPackageAndArray()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass[][]>");
      assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass[][]>"));
      assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
   }

   @Test
   public void addConcreteGenericSuperTypeWithPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass>");
      assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass>"));
      assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
   }

   @Test
   public void addMultipleConcreteGenericSuperTypeWithPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType(
               "it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass,com.coopservice.test.MyOtherClass>");
      assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass, MyOtherClass>"));
      assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
      assertNotNull(javaClass.getImport("com.coopservice.test.MyOtherClass"));
   }

   @Test
   public void addGenericSuperTypeWithoutPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("Bar<T>");
      assertTrue(javaClass.toString().contains("Bar<T>"));
      assertNull(javaClass.getImport("it.coopservice.test.Bar"));
   }

   @Test
   public void removeGenericSuperType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addImport("it.coopservice.test.Bar");
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("Bar<T>");
      assertTrue(javaClass.toString().contains("Bar<T>"));
      javaClass.setSuperType("");
      assertTrue(!javaClass.toString().contains("Bar<T>"));
   }

   @Test
   public void addMultipleGenerics()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("I");
      javaClass.addTypeVariable().setName("O");
      assertTrue(javaClass.toString().contains("<I, O>"));
      javaClass.removeTypeVariable("I");
      assertTrue(javaClass.toString().contains("<O>"));
   }

   @Test
   public void classTypeVariableBounds()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T").setBounds(CharSequence.class);
      assertTrue(javaClass.toString().contains("<T extends CharSequence>"));
      javaClass.getTypeVariable("T").setBounds(CharSequence.class, Serializable.class);
      assertTrue(javaClass.toString().contains("<T extends CharSequence & Serializable>"));
      javaClass.getTypeVariable("T").removeBounds();
      assertTrue(javaClass.toString().contains("<T>"));
   }

   @Test
   public void javaTypeTypeVariableBounds()
   {
      JavaInterface<?> foo = Roaster.create(JavaInterfaceSource.class).setPackage("it.coopservice.test").setName("Foo");
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T").setBounds(foo);
      assertTrue(javaClass.toString().contains("<T extends Foo>"));
      JavaInterface<?> bar = Roaster.create(JavaInterfaceSource.class).setPackage("it.coopservice.test").setName("Bar");
      javaClass.getTypeVariable("T").setBounds(foo, bar);
      assertTrue(javaClass.toString().contains("<T extends Foo & Bar>"));
      javaClass.getTypeVariable("T").removeBounds();
      assertTrue(javaClass.toString().contains("<T>"));
   }

   @Test
   public void stringTypeVariableBounds()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T").setBounds("com.something.Foo");
      assertTrue(javaClass.toString().contains("<T extends com.something.Foo>"));
      javaClass.getTypeVariable("T").setBounds("com.something.Foo", "com.something.Bar<T>");
      assertTrue(javaClass.toString().contains("<T extends com.something.Foo & com.something.Bar<T>>"));
      javaClass.getTypeVariable("T").removeBounds();
      assertTrue(javaClass.toString().contains("<T>"));
   }

   @Test
   public void getClassGenerics()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("I");
      javaClass.addTypeVariable().setName("O");
      List<TypeVariableSource<JavaClassSource>> typeVariables = javaClass.getTypeVariables();
      assertNotNull(typeVariables);
      assertEquals(2, typeVariables.size());
      assertEquals("I", typeVariables.get(0).getName());
      assertTrue(typeVariables.get(0).getBounds().isEmpty());
      assertEquals("O", typeVariables.get(1).getName());
      assertTrue(typeVariables.get(1).getBounds().isEmpty());
   }

   @Test
   public void getClassGenericsName()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable("I");
      javaClass.addTypeVariable("O");
      List<TypeVariableSource<JavaClassSource>> typeVariables = javaClass.getTypeVariables();
      assertNotNull(typeVariables);
      assertEquals(2, typeVariables.size());
      assertEquals("I", typeVariables.get(0).getName());
      assertTrue(typeVariables.get(0).getBounds().isEmpty());
      assertEquals("O", typeVariables.get(1).getName());
      assertTrue(typeVariables.get(1).getBounds().isEmpty());
   }

   @Test
   public void testNestedGenericsInSuperType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setSuperType("SomeClass<java.util.Map<String,java.util.Date>>");
      assertTrue(javaClass.hasImport(Map.class));
      assertTrue(javaClass.hasImport(Date.class));
      assertEquals("SomeClass<Map<String,Date>>", javaClass.getSuperType());
   }
}