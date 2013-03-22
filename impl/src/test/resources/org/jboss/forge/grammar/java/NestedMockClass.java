package org.jboss.forge.grammar.java;

import java.net.URL;

public class OuterClass
{
   public class MockClass
   {
      private String field;
      private URL urlField;

      public String valueOf(URL url)
      {
         return url.getPath();
      }
   }
}
