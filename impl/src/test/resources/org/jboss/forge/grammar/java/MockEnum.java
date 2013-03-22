package org.jboss.forge.grammar.java;

import java.net.URL;

@SuppressWarnings
public enum MockEnum
{
   FOO, BAR, BAZ;

   private String field;

   private MockEnum()
   {
   }

   String getName()
   {
      return name();
   }
}
