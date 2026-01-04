/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test for initializer bodies
 */
public class InitializerBodyTest
{
   @Test
   public void testSetInitializerBody()
   {
      String body = "System.out.println(\"Hello world!\");";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      InitializerSource<JavaClassSource> initializer = source.addInitializer().setBody(body);
      assertEquals(body, initializer.getBody());
   }

   @Test
   public void testInitializerBodyWithLambdas()
   {
      String body = "foo((c) -> System.out::println);";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      InitializerSource<JavaClassSource> method = source.addInitializer().setBody(body);
      assertEquals(body, method.getBody());
   }

   @Test
   public void testBodyShouldBeSet()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      InitializerSource<JavaClassSource> initializer = javaClass.addInitializer()
         .setBody("OpenIabHelper.Options.Builder builder = new OpenIabHelper.Options.Builder(); \n\t builder.setStoreSearchStrategy(OpenIabHelper.Options.SEARCH_STRATEGY_INSTALLER);");
      assertNotNull(initializer.getBody());
      assertThat(initializer.getBody()).isNotEmpty();
   }
}