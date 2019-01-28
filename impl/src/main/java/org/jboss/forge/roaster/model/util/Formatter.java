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
import java.util.Properties;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.Streams;

/**
 * Formatter for java source code.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class Formatter
{
   /**
    * Format the given Java source {@link File}, using the built in code format style.
    * 
    * @param source the file which contains the source to format
    * @throws FormatterException if the source coudn't be formatted
    * @throws IOException When the file cannot be read or written
    */
   public static void format(File source) throws IOException
   {
      format(null, source);
   }

   /**
    * Format the given Java source {@link File} using the given Eclipse code format properties {@link File}.
    * 
    * @param prefs the format properties file
    * @param source the file which contains the source to format
    * @throws FormatterException if the source coudn't be formatted
    * @throws IOException When the file cannot be read or written, or the preferences cannot be read.
    */
   public static void format(File prefs, File source) throws IOException
   {
      Properties options = readConfig(prefs);
      if (options == null)
         options = readConfigInternal();

      try (InputStream in = new BufferedInputStream(new FileInputStream(source));
               OutputStream out = new BufferedOutputStream(new FileOutputStream(source)))
      {
         String content = Streams.toString(in);
         String formatted = format(options, content);
         Streams.write(new ByteArrayInputStream(formatted.getBytes()), out);
      }
   }

   /**
    * Format the given {@link JavaClassSource}, using the built in code format style.
    * 
    * @param javaClass the class to format
    * @return the formatted source code
    * @throws FormatterException if the source coudn't be formatted
    */
   public static String format(JavaClassSource javaClass)
   {
      return format(javaClass.toString());
   }

   /**
    * Format the given {@link JavaClassSource}, using the given Eclipse code format {@link Properties}.
    * 
    * @param prefs the format properties
    * @param javaClass the class to format
    * @return the formatted source code
    * @throws FormatterException if the source coudn't be formatted
    */
   public static String format(Properties prefs, JavaClassSource javaClass)
   {
      return format(prefs, javaClass.toString());
   }

   /**
    * Format the given {@link String} as a Java source file, using the built in code format style.
    * 
    * @param source the source to format
    * @return the formatted source code
    * @throws FormatterException if the source coudn't be formatted
    */
   public static String format(String source)
   {
      Properties options = readConfigInternal();
      return format(options, source);
   }

   /**
    * Format the given {@link String} as a Java source type, using the given Eclipse code format {@link Properties}.
    * 
    * @param prefs the format properties
    * @param source the source to format
    * @return the formatted source code
    * @throws FormatterException if the source coudn't be formatted
    */
   public static String format(Properties prefs, String source)
   {
      CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(prefs);
      IDocument doc = new Document(source);
      try
      {
         TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS,
                  source, 0, source.length(), 0, System.lineSeparator());
         if (edit != null)
         {
            edit.apply(doc);
         }
         else
         {
            throw new FormatterException(source, null);
         }
      }
      catch (BadLocationException e)
      {
         throw new FormatterException(source, e);
      }
      return doc.get();
   }

   private static Properties readConfig(File prefs) throws IOException
   {
      if (prefs != null)
      {
         try (InputStream stream = new BufferedInputStream(new FileInputStream(prefs)))
         {
            Properties config;
            if (prefs.getName().toLowerCase().endsWith(".xml"))
            {
               config = parseXmlConfig(stream);
            }
            else
            {
               config = parseConfig(stream);
            }
            return applyShadedPackageName(config);
         }
         catch (IOException e)
         {
            throw new IOException("Error reading preferences file: ["
                     + prefs.getAbsolutePath() + "]", e);
         }
      }

      return null;
   }

   public static Properties applyShadedPackageName(final Properties config)
   {
      Properties modified = new Properties();
      String shadePackage = JavaCore.class.getPackage().getName().replaceAll("org\\.eclipse.*$", "");
      for (String property : config.stringPropertyNames())
      {
         if (property.startsWith(shadePackage))
         {
            modified.put(property, config.getProperty(property));
         }
         else
         {
            modified.put(shadePackage + property, config.getProperty(property));
         }
      }
      return modified;
   }

   /**
    * The given options should at least provide the source level (JavaCore.COMPILER_SOURCE), the compiler compliance
    * level (JavaCore.COMPILER_COMPLIANCE) and the target platform (JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM).
    *
    * Without these options, it is not possible for the code formatter to know what kind of source it needs to format.
    */
   private static Properties readConfigInternal()
   {
      Properties properties = new Properties();

      properties.setProperty(JavaCore.COMPILER_SOURCE, JDTOptions.getOption(JavaCore.COMPILER_SOURCE));
      properties.setProperty(JavaCore.COMPILER_COMPLIANCE, JDTOptions.getOption(JavaCore.COMPILER_COMPLIANCE));
      properties.setProperty(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
               JDTOptions.getOption(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM));
      // ROASTER-96: Add a blank line after imports. "1" is equivalent to TRUE in the formatter XML file
      properties.setProperty(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "1");
      return properties;
   }

   private static Properties parseConfig(InputStream stream) throws IOException
   {
      final Properties formatterOptions = readConfigInternal();
      formatterOptions.load(stream);
      return formatterOptions;
   }

   private static Properties parseXmlConfig(InputStream stream) throws IOException
   {
      Properties properties = readConfigInternal();
      Properties defaultProperties = FormatterProfileReader.fromEclipseXml(stream).getDefaultProperties();
      properties.putAll(defaultProperties);
      return properties;
   }
}