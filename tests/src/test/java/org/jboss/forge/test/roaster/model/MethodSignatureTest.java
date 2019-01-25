/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodSignatureTest
{
   @Test
   public void testEmptyMethodSignature()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public void hello()");
      String signature = method.toSignature();
      assertEquals("public hello() : void", signature);
   }

   @Test
   public void testMethodSignatureParams()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar)");
      String signature = method.toSignature();
      assertEquals("public hello(String, int) : void", signature);
   }

   @Test
   public void testMethodParams()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public void hello(String foo, int bar, char[] array, char value[])");
      List<ParameterSource<JavaClassSource>> parameters = method.getParameters();

      Assert.assertEquals("String", parameters.get(0).getType().toString());
      Assert.assertEquals("int", parameters.get(1).getType().toString());

      Assert.assertEquals("char[]", parameters.get(2).getType().toString());
      Assert.assertTrue(parameters.get(2).getType().isArray());

      Assert.assertEquals("char", parameters.get(3).getType().toString());
      Assert.assertTrue(parameters.get(3).getType().isArray());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testUnmodifiableMethodParams()
   {
      Roaster.create(JavaClassSource.class).addMethod("public void hello(String foo, int bar)").getParameters()
               .add(null);
   }

   @Test
   public void testMethodVisibility()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);

      MethodSource<JavaClassSource> method = javaClass.addMethod("public void hello()");
      assertVisibility(Visibility.PUBLIC, method);
      assertVisibility("public", method);

      method = javaClass.addMethod("protected void hello()");
      assertVisibility(Visibility.PROTECTED, method);
      assertVisibility("protected", method);

      method = javaClass.addMethod("private void hello()");
      assertVisibility(Visibility.PRIVATE, method);
      assertVisibility("private", method);

      method = javaClass.addMethod("void hello()");
      assertVisibility(Visibility.PACKAGE_PRIVATE, method);
      assertVisibility("", method);
   }

   @Test
   public void testMethodVisibilityWithSetter()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod().setName("hello");
      assertVisibility("", method);

      method.setVisibility(Visibility.PUBLIC);
      assertVisibility("public", method);

      method.setVisibility(Visibility.PROTECTED);
      assertVisibility("protected", method);

      method.setVisibility(Visibility.PRIVATE);
      assertVisibility("private", method);

      method.setVisibility(Visibility.PACKAGE_PRIVATE);
      assertVisibility("", method);
   }

   @Test
   public void testMethodWithPrimitiveParameters()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod().setPublic().setName("doSomething")
               .setReturnType(Integer.TYPE).setBody("return 0;");
      method.addParameter(Integer.TYPE, "initValue");
      method.addParameter(int.class, "intValueClass");
      method.addParameter(int[].class, "intValueClassArray");
      Assert.assertEquals(1, javaClass.getMethods().size());
      List<ParameterSource<JavaClassSource>> parameters = javaClass.getMethods().get(0).getParameters();
      Assert.assertEquals(3, parameters.size());
      Assert.assertTrue(parameters.get(0).getType().isPrimitive());
      Assert.assertTrue(parameters.get(1).getType().isPrimitive());
      Assert.assertTrue(parameters.get(2).getType().isArray());
   }

   private void assertVisibility(Visibility visibility, MethodSource<JavaClassSource> method)
   {
      Assert.assertEquals(visibility, method.getVisibility());
   }

   private void assertVisibility(String visibility, MethodSource<JavaClassSource> method)
   {
      Assert.assertEquals(visibility, method.getVisibility().toString());
   }

   public static class Inner
   {
      //empty for testing
   }

   @Test
   public void testMethodWithInnerClassParameter()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod().setPublic().setName("doSomething")
               .setReturnTypeVoid();
      method.addParameter(Inner.class, "inner");
      Assert.assertEquals(1, javaClass.getMethods().size());
      assertEquals(Inner.class.getCanonicalName(), method.getParameters().get(0).getType().getQualifiedName());
      assertEquals(Inner.class.getCanonicalName(), javaClass.getImports().get(0).getQualifiedName());
   }

   @Test
   public void testMethodWithGenericParameters()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod().setPublic().setName("hello")
               .setReturnType("java.util.List<String>");
      method.addParameter("java.util.Map<java.util.Set<String>,Object>", "map");

      String signature = method.toSignature();
      assertEquals("public hello(Map) : List", signature);

      ParameterSource<?> ps = method.getParameters().get(0);
      assertEquals(2, ps.getType().getTypeArguments().size());

      assertEquals(1, method.getReturnType().getTypeArguments().size());
   }

   @Test
   public void testMethodSignatureParamsWithGenerics()
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public java.util.List<String> hello(java.util.Map<java.util.Set<String>,Object> map)");
      String signature = method.toSignature();
      assertEquals("public hello(java.util.Map) : java.util.List", signature);

      ParameterSource<?> ps = method.getParameters().get(0);
      assertEquals(2, ps.getType().getTypeArguments().size());

      assertEquals("public java.util.List<String> hello(java.util.Map<java.util.Set<String>,Object> map){\n}",
               method.toString().trim());
      assertEquals(1, method.getReturnType().getTypeArguments().size());
   }
}