package org.jboss.forge.grammar.java;

import java.net.URL;
import static java.util.Collections.*;

public class MockClass
{
   private String field;
   private URL urlField;
   
   public String valueOf(URL url)
   {
      return url.getPath();
   }
}
