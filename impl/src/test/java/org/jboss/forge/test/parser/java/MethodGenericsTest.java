/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.util.List;
import java.util.regex.Pattern;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.jboss.forge.parser.java.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

public class MethodGenericsTest
{

   @Test
   public void addAndRemoveGenericType() throws ClassNotFoundException
   {
      JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      
      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addGenericType("T");
      
      Assert.assertTrue(method.toString().contains("<T>"));
      method.removeGenericType("T");
      Assert.assertFalse(method.toString().contains("<T>"));
   }

   @Test
   public void addMultipleGenerics() throws ClassNotFoundException
   {
      JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();

      method.addGenericType("I");
      method.addGenericType("O");
      Assert.assertTrue(Pattern.compile("<I, *O>").matcher(method.toString()).find());
      method.removeGenericType("I");
      Assert.assertTrue(method.toString().contains("<O>"));
   }

   @Test
   public void getMethodGenerics() throws ClassNotFoundException
   {
      JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();

      method.addGenericType("I");
      method.addGenericType("O");
      List<String> genericTypes = method.getGenericTypes();
      Assert.assertNotNull(genericTypes);
      Assert.assertEquals(2, genericTypes.size());
      Assert.assertTrue(genericTypes.contains("I"));
      Assert.assertTrue(genericTypes.contains("O"));
   }

}
