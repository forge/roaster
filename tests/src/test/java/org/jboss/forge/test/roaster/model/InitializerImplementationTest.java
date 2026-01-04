package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InitializerImplementationTest
{

   @Test
   public void testInitializerBodyShouldNotBeEmptyOnInvalidCode() {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      InitializerSource<JavaClassSource> initializer = source.addInitializer();
      assertThrows(ParserException.class, () -> initializer.setBody("{}{{}{dasfasdfasdfga"));
   }
   
   @Test
   public void testEmptyInitializerBodyShouldNotThrowException() {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      InitializerSource<JavaClassSource> initializer = source.addInitializer();
      initializer.setBody("");
      assertEquals("", initializer.getBody());
   }

   @Test
   public void testInitializerBodyShouldParseCorrectly()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      InitializerSource<JavaClassSource> initializer = source.addInitializer();
      initializer.setBody("System.out.println(\"Hello world!\");");
      assertEquals("System.out.println(\"Hello world!\");", initializer.getBody());
   }

}
