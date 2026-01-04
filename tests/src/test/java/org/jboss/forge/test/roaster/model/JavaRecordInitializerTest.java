package org.jboss.forge.test.roaster.model;

import java.io.IOException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaRecordInitializerTest
{
   
   private JavaRecordSource javaRecord;
   private InitializerSource<JavaRecordSource> initializer;

   @BeforeEach
   public void reset() throws IOException
   {
      javaRecord = Roaster.parse(JavaRecordSource.class, "public record MockRecord(int foo, String bar) {}");
      javaRecord.addInitializer("{ System.out.println(\"Hello world!\") }");
      initializer = javaRecord.getInitializers().get(javaRecord.getInitializers().size() - 1);
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
