package org.jboss.forge.test.roaster.model.util;

import static org.junit.Assert.assertNotNull;

import org.jboss.forge.roaster.model.util.JDTOptions;
import org.junit.Test;

public class JDTOptionsTest
{

   @Test
   public void testGetJDTOptionsDoesNotReturnNull()
   {
      assertNotNull(JDTOptions.getJDTOptions());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetJDTOptionsDoesReturnUnmodifiableMap()
   {
      JDTOptions.getJDTOptions().put("key", "value");
   }
}