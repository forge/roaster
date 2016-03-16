package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Test;

public class NonAsciiCharactersTest
{

   String nonAsciiCharactersClass = "public class NonAsciiCharacters {" +

   "   public int punctuations_example_áéíóú = 0; " +

   "}";

   @Test
   public void testNonAsciiCharacters()
   {
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, nonAsciiCharactersClass);
      Assert.assertTrue(!javaClass.hasSyntaxErrors());
      Assert.assertEquals("punctuations_example_áéíóú", javaClass.getFields().get(0).getName());
   }

}
