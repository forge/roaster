package org.jboss.forge.roaster.model.util;

/**
 * 
 *
 */
public class FormatterException extends RuntimeException
{
   private static final long serialVersionUID = -312503102097390478L;

   private String source;

   /**
    * Constructs a new {@code FormatterException} with the give message, cause and problematic source.
    * 
    * @param message the error message of the exception
    * @param source the source which lead to this exception
    * @param cause the cause of the exception
    */
   public FormatterException(String message, String source, Throwable cause)
   {
      super(message, cause);
      this.source = source;
   }

   /**
    * Constructs a new {@code FormatterException} with a default message, cause and problematic source.
    * 
    * @param source the source which lead to this exception
    * @param cause the cause of the exception
    */
   public FormatterException(String source, Throwable cause)
   {
      this("The source cannot be formatted", source, cause);
   }

   /**
    * Returns the source which coudn't be formatted.
    * 
    * @return the source which coudn't be formatted
    */
   public String getSource()
   {
      return source;
   }
}