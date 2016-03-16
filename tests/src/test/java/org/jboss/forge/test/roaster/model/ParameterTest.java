/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

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

}
