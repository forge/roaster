/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for method bodies
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class MethodBodyTest
{
   @Test
   public void testSetMethodBody() throws Exception
   {
      String body = "return null;";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("myMethod").setReturnType(String.class)
               .setBody(body);
      Assert.assertEquals(body, method.getBody());
   }

   @Test
   public void testMethodBodyWithLambdas() throws Exception
   {
      String body = "foo((c) -> System.out::println);";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("myMethod").setReturnType(String.class)
               .setBody(body);
      Assert.assertEquals(body, method.getBody());
   }

   @Test
   @Ignore("ROASTER-26")
   public void testSetMethodBodyWithComments() throws Exception
   {
      String body = "//TODO comments\n return null;";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("myMethod").setReturnType(String.class)
               .setBody(body);
      Assert.assertEquals(body, method.getBody());
   }
}
