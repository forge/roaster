/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.junit.Assert;
import org.junit.Test;

public class JavaSourceCompatibilityTest
{

   @Test
   public void testSupportsGenericsSource()
   {
      JavaType<?> source = Roaster
               .parse("public class Test{public void test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}}");
      Assert.assertFalse(source.hasSyntaxErrors());
   }

   @Test
   public void testSupportsGenericsSourceFromConstructor()
   {
      JavaType<?> source = Roaster
               .parse("public class Test{public Test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}}");
      Assert.assertFalse(source.hasSyntaxErrors());
   }

   @Test
   public void testSupportsGenericsSourceFromMethod()
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Test{}");
      source.addMethod(
               "public void test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}");
      Assert.assertFalse(source.hasSyntaxErrors());
   }

   @Test
   public void testSupportsGenericsSourceFromAddedConstructor()
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Test{}");
      // Add a new method to get JDT to recognize the new ASTs
      source.addMethod().setConstructor(true)
               .setBody("java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}");
      // Forces a rewrite to happen via AbstractJavaSource
      source.toString();
      Assert.assertFalse(source.hasSyntaxErrors());
   }

   @Test
   public void testSupportsGenericsSourceFromAddedMethod()
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Test{}");
      // Add a new method to get JDT to recognize the new ASTs
      source.addMethod().setName("test")
               .setBody("java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}");
      // Forces a rewrite to happen via AbstractJavaSource
      source.toString();
      Assert.assertFalse(source.hasSyntaxErrors());
   }

   @Test
   public void testSupportsDefaultMethods()
   {
      JavaInterfaceSource source = Roaster.parse(JavaInterfaceSource.class,
               "public interface Test{ public default String blah() {return null;}}");
      // Forces a rewrite to happen via AbstractJavaSource
      source.toString();
      Assert.assertFalse(source.hasSyntaxErrors());
   }
}