/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.jboss.forge.parser.java.JavaType;
import org.jboss.forge.parser.java.source.JavaSource;
import org.jboss.forge.parser.spi.JavaParserProvider;

/**
 * Responsible for parsing data into new {@link JavaType} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public final class JavaParser
{
   private static List<JavaParserProvider> parsers;

   private static List<JavaParserProvider> getParsers()
   {
      synchronized (JavaParser.class)
      {
         if (parsers == null)
         {
            parsers = new ArrayList<JavaParserProvider>();
            for (JavaParserProvider p : ServiceLoader.load(JavaParserProvider.class))
            {
               parsers.add(p);
            }
         }
         if (parsers.size() == 0)
         {
            throw new IllegalStateException("No instances of [" + JavaParserProvider.class.getName()
                     + "] were found on the classpath.");
         }
      }
      return parsers;
   }

   /**
    * Create a new empty {@link JavaSource} instance.
    */
   public static <T extends JavaSource<?>> T create(final Class<T> type)
   {
      for (JavaParserProvider parser : getParsers())
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
    * Read the given string and parse its data into a new {@link JavaType} instance of the given type.
    */
   public static <T extends JavaType<?>> T parse(final Class<T> type, final String data)
   {
      return parse(type, Streams.fromString(data));
   }

   /**
    * Read the given {@link InputStream} and parse its data into a new {@link JavaType} instance of the given type.
    * The caller is responsible for closing the stream.
    */
   public static <T extends JavaType<?>> T parse(final Class<T> type, final InputStream data)
   {
      for (JavaParserProvider parser : getParsers())
      {
         final JavaType<?> source = parser.parse(data);

         if (type.isInstance(source))
         {
            @SuppressWarnings("unchecked")
            final T result = (T) source;
            return result;
         }
         else if (source != null)
         {
            throw new ParserException("Source does not represent a [" + type.getSimpleName() + "], instead was ["
                     + source.getClass().getSimpleName() + "] - Cannot convert.");
         }
      }
      throw new ParserException("Cannot find JavaParserProvider capable of parsing the requested data");
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
