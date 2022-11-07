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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
      assertEquals(body, method.getBody());
   }

   @Test
   public void testMethodBodyWithLambdas()
   {
      String body = "foo((c) -> System.out::println);";
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod().setName("myMethod").setReturnType(String.class)
               .setBody(body);
      assertEquals(body, method.getBody());
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
      assertNotNull(method.getBody());
      assertThat(method.getBody()).isNotEmpty();
   }

   @Test
   @Disabled
   public void testGetMethodBodyWithComments()
   {
      String data = "public class Foo { Object bar() {\n//TODO comments\n return null;}}";
      JavaClassSource source = Roaster.parse(JavaClassSource.class, data);
      MethodSource<JavaClassSource> method = source.getMethod("bar");
      assertThat(method.getBody()).isEqualTo("\n//TODO comments\n return null;");
   }

   @Test
   @Disabled
   public void testSetMethodBodyWithComments() {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = source.addMethod();
      String body = "// this is a comment\n" +
                    "System.out.println(\"Success\");\n" +
                    "System.out.println(\"Success Again\");\n" +
                    "// this is another comment\n" +
                    "int a=21;\n" +
                    "System.out.println(a);";
      method.setName("myMethod").setReturnType(void.class).setBody(body);
      assertThat(method.getBody()).isEqualTo(body);
   }

}