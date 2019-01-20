package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Test;

public class ParsingPerformanceTest
{
   @Test(timeout = 5000)
   public void testRoaster106() throws Exception
   {
      URL resource = getClass().getResource("ROASTER106.java");
      JavaClassSource source = Roaster.parse(JavaClassSource.class, resource);
      // force the creation of the object by doing something with it
      assertNotNull(source);
   }
}