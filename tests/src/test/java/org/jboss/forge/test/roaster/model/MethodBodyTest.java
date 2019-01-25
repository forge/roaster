/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
   public void testSetMethodBody()
   {
      String body = "return null;";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("myMethod").setReturnType(String.class)
               .setBody(body);
      Assert.assertEquals(body, method.getBody());
   }

   @Test
   public void testMethodBodyWithLambdas()
   {
      String body = "foo((c) -> System.out::println);";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("myMethod").setReturnType(String.class)
               .setBody(body);
      Assert.assertEquals(body, method.getBody());
   }

   @Test
   @Ignore("ROASTER-26")
   public void testSetMethodBodyWithComments()
   {
      String data = "public class Foo { Object bar() {\n//TODO comments\n return null;}}";
      JavaClassSource source = Roaster.parse(JavaClassSource.class, data);
      MethodSource<JavaClassSource> method = source.getMethod("bar");
      Assert.assertEquals("//TODO comments\n return null;", method.getBody());
   }

   @Test
   public void testBodyShouldBeSet()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class).setName("Foo");
      MethodSource<JavaClassSource> method = javaClass.addMethod()
               .setPublic()
               .setStatic(false)
               .setName("setupIAB")
               .setReturnTypeVoid()
               .setBody("OpenIabHelper.Options.Builder builder = new OpenIabHelper.Options.Builder(); \n\t builder.setStoreSearchStrategy(OpenIabHelper.Options.SEARCH_STRATEGY_INSTALLER);");
      assertThat(method.getBody(), notNullValue());
      assertThat(method.getBody(), not(""));
   }
}