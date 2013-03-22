/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.junit.Assert;
import org.junit.Test;

public class JavaClassGenericsTest
{

   @Test
   public void addAndRemoveGenericType() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addGenericType("T");
      Assert.assertTrue(javaClass.toString().contains("<T>"));
      javaClass.removeGenericType("T");
      Assert.assertTrue(!javaClass.toString().contains("<T>"));
   }

   @Test
   public void addGenericSuperTypeWithPackage() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("Bar<T>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
   }
   
   @Test
   public void addConcreteGenericSuperTypeWithPackage() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass>");
      Assert.assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
   }
   
   @Test
   public void addMultipleConcreteGenericSuperTypeWithPackage() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("it.coopservice.test.Bar<com.coopservice.test.MyConcreteSuperClass,com.coopservice.test.MyOtherClass>");
      Assert.assertTrue(javaClass.toString().contains("extends Bar<MyConcreteSuperClass, MyOtherClass>"));
      Assert.assertNotNull(javaClass.getImport("it.coopservice.test.Bar"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyConcreteSuperClass"));
      Assert.assertNotNull(javaClass.getImport("com.coopservice.test.MyOtherClass"));
   }

   @Test
   public void addGenericSuperTypeWithoutPackage() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("Bar<T>"));
      Assert.assertNull(javaClass.getImport("it.coopservice.test.Bar"));
   }

   @Test
   public void removeGenericSuperType() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.addImport("it.coopservice.test.Bar");
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.setSuperType("Bar<T>");
      Assert.assertTrue(javaClass.toString().contains("Bar<T>"));
      javaClass.setSuperType("");
      Assert.assertTrue(!javaClass.toString().contains("Bar<T>"));
   }

   @Test
   public void addMultipleGenerics() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addGenericType("I");
      javaClass.addGenericType("O");
      Assert.assertTrue(javaClass.toString().contains("<I, O>"));
      javaClass.removeGenericType("I");
      Assert.assertTrue(javaClass.toString().contains("<O>"));
   }

   @Test
   public void getClassGenerics() throws ClassNotFoundException
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.setPackage("it.coopservice.test");
      javaClass.setName("SimpleClass");
      javaClass.addGenericType("I");
      javaClass.addGenericType("O");
      List<String> genericTypes = javaClass.getGenericTypes();
      Assert.assertNotNull(genericTypes);
      Assert.assertEquals(2, genericTypes.size());
      Assert.assertTrue(genericTypes.contains("I"));
      Assert.assertTrue(genericTypes.contains("O"));
   }

}
