/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ParameterTest
{
   @Test
   public void testMethodIsFinal()
   {
      InputStream stream = ParameterTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockFinalParameter.java");
      MethodSource<JavaClassSource> method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      Assert.assertThat(method.getParameters().size(), is(2));
      ParameterSource<JavaClassSource> parameterOne = method.getParameters().get(0);
      Assert.assertThat(parameterOne.isFinal(), is(true));
      ParameterSource<JavaClassSource> parameterTwo = method.getParameters().get(1);
      Assert.assertThat(parameterTwo.isFinal(), is(false));
   }

   @Test
   public void testParameterAsFinal() throws Exception
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      ParameterSource<JavaClassSource> parameter = clazz.addMethod().setReturnTypeVoid().setName("myMethod")
               .addParameter(String.class, "parameter");
      Assert.assertThat(parameter.isFinal(), is(false));
      parameter.setFinal(true);
      Assert.assertThat(parameter.isFinal(), is(true));
      parameter.setFinal(false);
      Assert.assertThat(parameter.isFinal(), is(false));
   }

   @Test
   public void testParameterShouldBeVarargs() throws Exception
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      ParameterSource<JavaClassSource> parameter = clazz.addMethod().setReturnTypeVoid().setName("myMethod")
               .addParameter(String.class, "parameter").setVarArgs(true);
      Assert.assertThat(parameter.isVarArgs(), is(true));
      parameter.setVarArgs(false);
      Assert.assertThat(parameter.isVarArgs(), is(false));
      parameter.setVarArgs(true);
      Assert.assertThat(parameter.isVarArgs(), is(true));
   }

   @Test
   public void testGenericTypeParameterShouldNotBeImported() throws Exception
   {
      JavaClassSource clazz = Roaster.create(JavaClassSource.class).setName("TestClass");
      MethodSource<JavaClassSource> method = clazz.addMethod().setReturnTypeVoid().setName("myMethod");
      method.addTypeVariable("T");
      method.addParameter("T", "name");
      Assert.assertThat(method.toString(), containsString("<T>void myMethod(T name)"));
   }

   @Test
   public void testPreserveParameterGenericTypes() throws Exception
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
      Assert.assertThat(clazz.toString(), containsString("public final <T> T name(int index, Class<T> as)"));
   }
}
