package org.jboss.forge.roaster.model.impl;

import org.apache.commons.text.StringEscapeUtils;

/**
 * String utilities.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public class Strings
{
   static String unquote(final String value)
   {
      String result = null;
      if (value != null)
      {
         result = value.replaceAll("\"(.*)\"", "$1");
      }
      return StringEscapeUtils.unescapeJava(result);
   }

   static String enquote(final String value)
   {
      String result = null;
      if (value != null)
      {
         result = "\"" + StringEscapeUtils.escapeJava(value) + "\"";
      }
      return result;
   }

}
