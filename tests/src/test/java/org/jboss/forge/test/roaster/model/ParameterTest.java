/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ParameterTest
{
   @Test
   public void testMethodIsFinal() throws IOException
   {
      MethodSource<JavaClassSource> method;
      String fileName = "/org/jboss/forge/grammar/java/MockFinalParameter.java";
      try (InputStream stream = ParameterTest.class.getResourceAsStream(fileName))
      {
         method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      }
      assertEquals(2, method.getParameters().size());
      ParameterSource<JavaClassSource> parameterOne = method.getParameters().get(0);
      assertTrue(parameterOne.isFinal());
      ParameterSource<JavaClassSource> parameterTwo = method.getParameters().get(1);
      assertFalse(parameterTwo.isFinal());
   }

   @Test
   public void testParameterAsFinal()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      ParameterSource<JavaClassSource> parameter = clazz.addMethod().setReturnTypeVoid().setName("myMethod")
               .addParameter(String.class, "parameter");
      assertFalse(parameter.isFinal());
      parameter.setFinal(true);
      assertTrue(parameter.isFinal());
      parameter.setFinal(false);
      assertFalse(parameter.isFinal());
   }

   @Test
   public void testParameterShouldBeVarargs()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      ParameterSource<JavaClassSource> parameter = clazz.addMethod().setReturnTypeVoid().setName("myMethod")
               .addParameter(String.class, "parameter").setVarArgs(true);
      assertTrue(parameter.isVarArgs());
      parameter.setVarArgs(false);
      assertFalse(parameter.isVarArgs());
      parameter.setVarArgs(true);
      assertTrue(parameter.isVarArgs());
   }

   @Test
   public void testGenericTypeParameterShouldNotBeImported()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      MethodSource<JavaClassSource> method = clazz.addMethod().setReturnTypeVoid().setName("myMethod");
      method.addTypeVariable("T");
      method.addParameter("T", "name");
      assertThat(method.toString()).contains("<T>void myMethod(T name)");
   }

   @Test
   public void testPreserveParameterGenericTypes()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      final MethodSource<JavaClassSource> newMethod = clazz.addMethod()
               .setName("name")
               .setPublic()
               .setFinal(true);
      newMethod.addTypeVariable("T");
      newMethod.setReturnType("T").setBody("String.class.as(((Class<T>) as));");
      newMethod.addParameter(int.class, "index");
      newMethod.addParameter("Class<T>", "as");
      assertThat(clazz.toString()).contains("public final <T> T name(int index, Class<T> as)");
   }

   @Test
   public void testEllipsisSupport()
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      final MethodSource<JavaClassSource> newMethod = clazz.addMethod()
               .setName("name")
               .setPublic()
               .setReturnTypeVoid()
               .setFinal(true);
      newMethod.addParameter("String", "name").setVarArgs(true);
      assertThat(clazz.toString()).contains("public final void name(String... name)");
   }

}
