package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaClassInitializerTest
{
   
   private JavaClassSource javaClass;
   private InitializerSource<JavaClassSource> initializer;

   @BeforeEach
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockClass.java";
      try (InputStream stream = JavaEnumMethodTest.class.getResourceAsStream(fileName))
      {
         javaClass = Roaster.parse(JavaClassSource.class, stream);
         javaClass.addInitializer("{ System.out.println(\"Hello world!\") }");
         initializer = javaClass.getInitializers().get(javaClass.getInitializers().size() - 1);
      }
   }
   
   @Test
   public void testIsNonStatic() {
      assertFalse(initializer.isStatic());
   }
   
   @Test
   public void testSetStatic() {
      assertFalse(initializer.isStatic());
      initializer.setStatic(true);
      assertTrue(initializer.isStatic());
   }

}
