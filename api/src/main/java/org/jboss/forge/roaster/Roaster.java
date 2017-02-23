/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.FormatterProvider;
import org.jboss.forge.roaster.spi.JavaParser;

/**
 * Responsible for parsing data into new {@link JavaType} instances.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public final class Roaster
{
   private Roaster()
   {
   }

   private static List<JavaParser> parsers;
   private static List<FormatterProvider> formatters;

   private static List<JavaParser> getParsers()
   {
      synchronized (Roaster.class)
      {
         if (parsers == null || parsers.isEmpty())
         {
            parsers = new ArrayList<JavaParser>();
            for (JavaParser p : ServiceLoader.load(JavaParser.class, Roaster.class.getClassLoader()))
            {
               parsers.add(p);
            }
         }
         if (parsers.isEmpty())
         {
            throw new IllegalStateException("No instances of [" + JavaParser.class.getName()
                     + "] were found on the classpath.");
         }
      }
      return parsers;
   }

   private static List<FormatterProvider> getFormatters()
   {
      synchronized (Roaster.class)
      {
         if (formatters == null || formatters.isEmpty())
         {
            formatters = new ArrayList<FormatterProvider>();
            for (FormatterProvider p : ServiceLoader.load(FormatterProvider.class, Roaster.class.getClassLoader()))
            {
               formatters.add(p);
            }
         }
         if (formatters.isEmpty())
         {
            throw new IllegalStateException("No instances of [" + FormatterProvider.class.getName()
                     + "] were found on the classpath.");
         }
      }
      return formatters;
   }

   /**
    * Create a new empty {@link JavaSource} instance.
    */
   public static <T extends JavaSource<?>> T create(final Class<T> type)
   {
      for (JavaParser parser : getParsers())
      {
         final T result = parser.create(type);
         if (result != null)
         {
            return result;
         }
      }
      throw new ParserException("Cannot find JavaParserProvider capable of producing JavaSource of type "
               + type.getSimpleName(), new IllegalArgumentException(type.getName()));
   }

   /**
    * Open the given {@link File}, parsing its contents into a new {@link JavaType} instance.
    */
   public static JavaType<?> parse(final File file) throws FileNotFoundException
   {
      return parse(JavaType.class, file);
   }

   /**
    * Parse the given {@link URL} data into a new {@link JavaType} instance.
    */
   public static JavaType<?> parse(final URL data) throws IOException
   {
      return parse(JavaType.class, data);
   }

   /**
    * Read the given {@link InputStream} and parse the data into a new {@link JavaType} instance.
    */
   public static JavaType<?> parse(final InputStream data)
   {
      return parse(JavaType.class, data);
   }

   /**
    * Parse the given character array into a new {@link JavaType} instance.
    */
   public static JavaType<?> parse(final char[] data)
   {
      return parse(JavaType.class, data);
   }

   /**
    * Parse the given String data into a new {@link JavaType} instance.
    */
   public static JavaType<?> parse(final String data)
   {
      return parse(JavaType.class, data);
   }

   /**
    * Read the given {@link URL} and parse its data into a new {@link JavaType} instance of the given type.
    *
    * @throws FileNotFoundException
    */
   public static <T extends JavaType<?>> T parse(final Class<T> type, final URL url) throws IOException
   {
      return internalParse(type, url.openStream());
   }

   /**
    * Read the given {@link File} and parse its data into a new {@link JavaType} instance of the given type.
    *
    * @throws FileNotFoundException
    */
   public static <T extends JavaType<?>> T parse(final Class<T> type, final File file) throws FileNotFoundException
   {
      return internalParse(type, new FileInputStream(file));
   }

   /**
    * Read the given character array and parse its data into a new {@link JavaType} instance of the given type.
    */
   public static <T extends JavaType<?>> T parse(final Class<T> type, final char[] data)
   {
      return parse(type, new String(data));
   }

   /**
    * Validates a code snippet and returns a {@link List} of {@link Problem}. Never returns <code>null</code>.
    * 
    * @param snippet any Java code
    * @throws ParserException if no {@link JavaParser} implementation could be found
    */
   public static List<Problem> validateSnippet(String snippet) throws ParserException
   {
      for (JavaParser parser : getParsers())
      {
         return parser.validateSnippet(snippet);
      }
      throw new ParserException("Cannot find JavaParser capable of validating the requested data");
   }

   /**
    * Read the given string and parse its data into a new {@link JavaType} instance of the given type.
    */
   @SuppressWarnings("unchecked")
   public static <T extends JavaType<?>> T parse(final Class<T> type, final String data)
   {
      for (JavaParser parser : getParsers())
      {
         final JavaUnit unit = parser.parseUnit(data);

         if (type.isInstance(unit.getGoverningType()))
         {
            return (T) unit.getGoverningType();
         }
         else if (unit != null)
         {
            throw new ParserException("Source does not represent a [" + type.getSimpleName() + "], instead was ["
                     + unit.getGoverningType().getClass().getSimpleName() + "] - Cannot convert.");
         }
      }
      throw new ParserException("Cannot find JavaParser capable of parsing the requested data");
   }

   /**
    * Read the given {@link InputStream} and parse its data into a new {@link JavaType} instance of the given type. The
    * caller is responsible for closing the stream.
    */
   public static <T extends JavaType<?>> T parse(final Class<T> type, final InputStream data)
   {
      return parse(type, Streams.toString(data));
   }

   /**
    * Read the given {@link String} and parse its data into a new {@link JavaUnit} instance of the given type.
    */
   public static JavaUnit parseUnit(final String data)
   {
      for (JavaParser parser : getParsers())
      {
         final JavaUnit unit = parser.parseUnit(data);
         if (unit != null)
            return unit;
      }
      throw new ParserException("Cannot find JavaParser capable of parsing the requested data");
   }

   /**
    * Read the given {@link InputStream} and parse its data into a new {@link JavaUnit} instance of the given type. The
    * caller is responsible for closing the stream.
    */
   public static JavaUnit parseUnit(final InputStream data)
   {
      return parseUnit(Streams.toString(data));
   }

   /**
    * Format the given {@link String} as a Java source file, using the built in code format style.
    * 
    * @param source a java source code
    * @return the formatted source code
    */
   public static String format(String source)
   {
      String result = source;
      for (FormatterProvider formatter : getFormatters())
      {
         result = formatter.format(result);
      }
      return result;
   }

   /**
    * Format the given {@link String} as a Java source type, using the given code format {@link Properties}
    */
   public static String format(Properties properties, String source)
   {
      String result = source;
      for (FormatterProvider formatter : getFormatters())
      {
         result = formatter.format(properties, result);
      }
      return result;
   }

   private static <T extends JavaType<?>> T internalParse(final Class<T> type, final InputStream data)
   {
      try
      {
         return parse(type, data);
      }
      finally
      {
         Streams.closeQuietly(data);
      }
   }
}
