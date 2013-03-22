/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.parser.java.JavaClass;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class Formatter
{
   public static String format(JavaClass javaClass)
   {
      return format(javaClass.toString());
   }
   
   public static String format(String source)
   {
       // TODO locate user's eclipse project settings, use those if we can.
       Properties options = readConfig("org.eclipse.jdt.core.prefs");

       final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
       return ensureCorrectNewLines(formatFile(source, codeFormatter));
   }

   private static String formatFile(String contents, CodeFormatter codeFormatter)
   {
      IDocument doc = new Document(contents);
      try
      {
         TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, contents, 0, contents.length(), 0, null);
         if (edit != null)
         {
            edit.apply(doc);
         }
         else
         {
            return contents;
         }
      }
      catch (BadLocationException e)
      {
         throw new RuntimeException(e);
      }

      return doc.get();
   }

   private static Properties readConfig(String filename)
   {
      BufferedInputStream stream = null;
      try
      {
         stream = new BufferedInputStream(Formatter.class.getResourceAsStream(filename));
         final Properties formatterOptions = new Properties();
         formatterOptions.load(stream);
         return formatterOptions;
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      finally
      {
         if (stream != null)
         {
            try
            {
               stream.close();
            }
            catch (IOException e)
            {
               /* ignore */
            }
         }
      }
   }
   
   
   private static String ensureCorrectNewLines(String documentString) 
   {
       String newLine = System.getProperty("line.separator");
       
       if (documentString.indexOf("\n") != -1 && documentString.indexOf(newLine) == -1)       
           return documentString.replaceAll("\n", newLine);
       
       return documentString;
   }

}
