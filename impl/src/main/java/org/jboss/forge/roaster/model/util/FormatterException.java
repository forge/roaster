package org.jboss.forge.roaster.model.util;

class FormatterException extends RuntimeException
{
   private String source;

   FormatterException(String message, String source, Throwable cause)  {
      super(message, cause);
      this.source = source;
   }

   FormatterException(String source, Throwable cause)  {
      this("The source cannot be formatted", source, cause);
   }
   public String getSource()
   {
      return source;
   }
}
