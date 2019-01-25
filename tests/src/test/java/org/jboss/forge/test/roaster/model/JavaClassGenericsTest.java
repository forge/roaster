/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;
import org.junit.Assert;
import org.junit.Test;

public class JavaClassGenericsTest
{

   @Test
   public void addAndRemoveGenericType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T");
      Assert.assertTrue(javaClass.getTypeVariables().get(0).getBounds().isEmpty());
      Assert.assertTrue(javaClass.toString().contains("<T>"));
      javaClass.removeTypeVariable("T");
      Assert.assertFalse(javaClass.toString().contains("<T>"));
   }

   @Test
   public void addGenericSuperTypeWithPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("Bar<T>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
   }

   @Test
   public void addConcreteGenericSuperTypeWithPackageAndArray()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass[][]>");
      Assert.assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass[][]>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
   }

   @Test
   public void addConcreteGenericSuperTypeWithPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass>");
      Assert.assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
   }

   @Test
   public void addMultipleConcreteGenericSuperTypeWithPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType(
               "it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass,com.coopservice.test.MyOtherClass>");
      Assert.assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass, MyOtherClass>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyOtherClass"));
   }

   @Test
   public void addGenericSuperTypeWithoutPackage()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("Bar<T>"));
      Assert.assertNull(javaClass.getImport("it.coopservice.test.Bar"));
   }

   @Test
   public void removeGenericSuperType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addImport("it.coopservice.test.Bar");
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("Bar<T>"));
      javaClass.setSuperType("");
      Assert.assertTrue(!javaClass.toString().contains("Bar<T>"));
   }

   @Test
   public void addMultipleGenerics()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("I");
      javaClass.addTypeVariable().setName("O");
      Assert.assertTrue(javaClass.toString().contains("<I, O>"));
      javaClass.removeTypeVariable("I");
      Assert.assertTrue(javaClass.toString().contains("<O>"));
   }

   @Test
   public void classTypeVariableBounds()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T").setBounds(CharSequence.class);
      Assert.assertTrue(javaClass.toString().contains("<T extends CharSequence>"));
      javaClass.getTypeVariable("T").setBounds(CharSequence.class, Serializable.class);
      Assert.assertTrue(javaClass.toString().contains("<T extends CharSequence & Serializable>"));
      javaClass.getTypeVariable("T").removeBounds();
      Assert.assertTrue(javaClass.toString().contains("<T>"));
   }

   @Test
   public void javaTypeTypeVariableBounds()
   {
      JavaInterface<?> foo = Roaster.create(JavaInterfaceSource.class).setPackage("it.coopservice.test").setName("Foo");
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T").setBounds(foo);
      Assert.assertTrue(javaClass.toString().contains("<T extends Foo>"));
      JavaInterface<?> bar = Roaster.create(JavaInterfaceSource.class).setPackage("it.coopservice.test").setName("Bar");
      javaClass.getTypeVariable("T").setBounds(foo, bar);
      Assert.assertTrue(javaClass.toString().contains("<T extends Foo & Bar>"));
      javaClass.getTypeVariable("T").removeBounds();
      Assert.assertTrue(javaClass.toString().contains("<T>"));
   }

   @Test
   public void stringTypeVariableBounds()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addTypeVariable().setName("T").setBounds("com.something.Foo");
      Assert.assertTrue(javaClass.toString().contains("<T extends com.something.Foo>"));
      javaClass.getTypeVariable("T").setBounds("com.something.Foo", "com.something.Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("<T extends com.something.Foo & com.something.Bar<T>>"));
      javaClass.getTypeVariable("T").removeBounds();
      Assert.assertTrue(javaClass.toString().contains("<T>"));
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
      Assert.assertNotNull(typeVariables);
      Assert.assertEquals(2, typeVariables.size());
      Assert.assertEquals("I", typeVariables.get(0).getName());
      Assert.assertTrue(typeVariables.get(0).getBounds().isEmpty());
      Assert.assertEquals("O", typeVariables.get(1).getName());
      Assert.assertTrue(typeVariables.get(1).getBounds().isEmpty());
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
      Assert.assertNotNull(typeVariables);
      Assert.assertEquals(2, typeVariables.size());
      Assert.assertEquals("I", typeVariables.get(0).getName());
      Assert.assertTrue(typeVariables.get(0).getBounds().isEmpty());
      Assert.assertEquals("O", typeVariables.get(1).getName());
      Assert.assertTrue(typeVariables.get(1).getBounds().isEmpty());
   }

   @Test
   public void testNestedGenericsInSuperType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setSuperType("SomeClass<java.util.Map<String,java.util.Date>>");
      Assert.assertThat(javaClass.hasImport(Map.class), is(true));
      Assert.assertThat(javaClass.hasImport(Date.class), is(true));
      Assert.assertThat(javaClass.getSuperType(), equalTo("SomeClass<Map<String,Date>>"));
   }
}