package org.jboss.forge.test.roaster.model;

import java.net.URL;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by ggastald on 16/06/16.
 */
public class ParsingPerformanceTest
{
   @Test(timeout = 5000)
   @Ignore
   public void testRoaster106() throws Exception
   {
      URL resource = getClass().getResource("ROASTER106.java");
      JavaClassSource source = Roaster.parse(JavaClassSource.class, resource);
      List<PropertySource<JavaClassSource>> properties = source.getProperties();
      for (PropertySource<JavaClassSource> propertySource : properties)
      {
         if (!propertySource.hasAnnotation("Transient")) {
            propertySource.getType().toString();
         }
      }
   }

}
