package org.jboss.forge.test.roaster.model;

import java.net.URL;
import java.time.Duration;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class ParsingPerformanceTest
{
   @Test
   public void testRoaster106() throws Exception
   {
      URL resource = getClass().getResource("ROASTER106.java");
      // force the creation of the object by doing something with it
      assertTimeout(Duration.ofSeconds(5), () -> Roaster.parse(JavaClassSource.class, resource));
   }
}