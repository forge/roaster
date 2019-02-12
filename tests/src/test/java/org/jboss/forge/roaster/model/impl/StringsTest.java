package org.jboss.forge.roaster.model.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringsTest
{
   @Test
   public void should_quote()
   {
      assertEquals("\"a$nfq5ei1\"", Strings.enquote("a$nfq5ei1"));
      assertEquals("\"\'\'\"", Strings.enquote("\'\'"));
      assertEquals("\"&uel;\"", Strings.enquote("&uel;"));
   }

   @Test
   public void should_unquote()
   {
      assertEquals("a$nfq5ei1", Strings.unquote("\"a$nfq5ei1\""));
      assertEquals("\'\'", Strings.unquote("\'\'"));
      assertEquals("&uel;", Strings.unquote("&uel;"));
   }
}