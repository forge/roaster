/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import java.util.Date;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.util.DesignPatterns;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class DesignPatternsTest
{

   @Test
   public void testCreateDecorator()
   {
      JavaInterfaceSource interfaceClass = Roaster
               .create(JavaInterfaceSource.class).setPackage("org.test.demo")
               .setName("CoolOMeter");
      MethodSource<JavaInterfaceSource> coolMethod = interfaceClass
               .addMethod().setReturnTypeVoid().setName("calculateCoolness");
      coolMethod.addParameter(String.class, "name");
      coolMethod.addParameter(Integer.class, "age");

      MethodSource<JavaInterfaceSource> uncoolMethod = interfaceClass
               .addMethod().setReturnTypeVoid().setName("calculateUnCoolness");
      uncoolMethod.addParameter(String.class, "name");
      uncoolMethod.addParameter(Integer.class, "age");
      JavaClassSource decorator = DesignPatterns.createDecorator(interfaceClass);
      Assert.assertNotNull(decorator);
      Assert.assertEquals("CoolOMeterDecorator", decorator.getName());
   }

   @Test
   public void testCreateBuilder()
   {
      JavaClassSource javaClass = Roaster
               .create(JavaClassSource.class).setPackage("org.test.demo")
               .setName("Customer");
      javaClass.addProperty(String.class, "id");
      javaClass.addProperty(String.class, "name");
      javaClass.addProperty(Date.class, "birthDate");
      JavaClassSource builder = DesignPatterns.createBuilder(javaClass);
      Assert.assertNotNull(builder);
      Assert.assertEquals("CustomerBuilder", builder.getName());
   }
}