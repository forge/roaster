package org.jboss.forge.test.roaster.model.util;

import org.jboss.forge.roaster.model.util.JDTOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JDTOptionsTest
{

   @Test
   void testGetJDTOptionsDoesNotReturnNull()
   {
      assertNotNull(JDTOptions.getJDTOptions());
   }

   @Test
   void testGetJDTOptionsDoesReturnUnmodifiableMap()
   {
      assertThrows(UnsupportedOperationException.class, () -> JDTOptions.getJDTOptions().put("key", "value"));
   }
}