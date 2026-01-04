package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaEnumInitializerTest
{
   
   private JavaEnumSource javaEnum;
   private InitializerSource<JavaEnumSource> initializer;

   @BeforeEach
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockEnum.java";
      try (InputStream stream = JavaEnumMethodTest.class.getResourceAsStream(fileName))
      {
         javaEnum = Roaster.parse(JavaEnumSource.class, stream);
         javaEnum.addInitializer("{ System.out.println(\"Hello world!\") }");
         initializer = javaEnum.getInitializers().get(javaEnum.getInitializers().size() - 1);
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
