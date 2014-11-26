/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.spi.Streams;

/**
 * Formats Java source code.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class Formatter
{
   /**
    * Format the given Java source {@link File}, using the built in code format style.
    * 
    * @throws IOException When the file cannot be read or written
    */
   public static void format(File source) throws IOException
   {
      format(null, source);
   }

   /**
    * Format the given Java source {@link File} using the given Eclipse code format properties {@link File}.
    * 
    * @throws IOException When the file cannot be read or written, or the preferences cannot be read.
    */
   public static void format(File prefs, File source) throws IOException
   {
      Properties options = readConfig(prefs);
      if (options == null)
         options = readConfigInternal("org.eclipse.jdt.core.prefs");

      InputStream in = null;
      OutputStream out = null;
      try
      {
         in = new BufferedInputStream(new FileInputStream(source));
         String content = Streams.toString(in);
         String formatted = format(options, content);

         out = new BufferedOutputStream(new FileOutputStream(source));
         Streams.write(new ByteArrayInputStream(formatted.getBytes()), out);
      }
      finally
      {
         Streams.closeQuietly(in);
         Streams.closeQuietly(out);
      }

   }

   /**
    * Format the given {@link JavaClassSource}, using the built in code format style.
    */
   public static String format(JavaClassSource javaClass)
   {
      return format(javaClass.toString());
   }

   /**
    * Format the given {@link JavaClassSource}, using the given Eclipse code format {@link Properties}.
    */
   public static String format(Properties prefs, JavaClassSource javaClass)
   {
      return format(prefs, javaClass.toString());
   }

   /**
    * Format the given {@link String} as a Java source file, using the built in code format style.
    */
   public static String format(String source)
   {
      Properties options = readConfigInternal("org.eclipse.jdt.core.prefs");
      return format(options, source);
   }

   /**
    * Format the given {@link String} as a Java source type, using the given Eclipse code format {@link Properties}.
    */
   public static String format(Properties prefs, String source)
   {
      final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(prefs);
      return _format(source, codeFormatter);
   }

   private static String _format(String contents, CodeFormatter codeFormatter)
   {
      IDocument doc = new Document(contents);
      try
      {
         TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT,
                  contents, 0, contents.length(), 0, null);
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

      return ensureCorrectNewLines(doc.get());
   }

   private static Properties readConfig(File prefs) throws IOException
   {
      if (prefs != null)
      {
         InputStream stream = new BufferedInputStream(new FileInputStream(prefs));
         try
         {
            Properties config = parseConfig(stream);
            return applyShadedPackageName(config);
         }
         catch (IOException e)
         {
            throw new IOException("Error reading preferences file: ["
                     + prefs.getAbsolutePath() + "]", e);
         }
         finally
         {
            Streams.closeQuietly(stream);
         }
      }

      return null;
   }

   public static Properties applyShadedPackageName(Properties config)
   {
      Properties modified = new Properties();
      String shadePackage = JavaCore.class.getPackage().getName().replaceAll("org\\.eclipse.*$", "");
      for (Entry<Object, Object> property : config.entrySet())
      {
         modified.put(shadePackage + property.getKey(), property.getValue());
      }

      return modified;
   }

   private static Properties readConfigInternal(String filename)
   {
      InputStream stream = new BufferedInputStream(org.jboss.forge.roaster.model.util.Formatter.class
               .getResourceAsStream(filename));
      try
      {
         return parseConfig(stream);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Error reading internal configuration: ["
                  + filename + "]", e);
      }
      finally
      {
         Streams.closeQuietly(stream);
      }
   }

   private static Properties parseConfig(InputStream stream)
            throws IOException
   {
      try
      {
         final Properties formatterOptions = new Properties();
         formatterOptions.load(stream);
         return formatterOptions;
      }
      finally
      {
         Streams.closeQuietly(stream);
      }
   }

   private static String ensureCorrectNewLines(String content)
   {
      String newLine = System.getProperty("line.separator");

      if (content.indexOf("\n") != -1
               && content.indexOf(newLine) == -1)
         return content.replaceAll("\n", newLine);

      return content;
   }

}
