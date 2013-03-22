/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.spi.JavaParserProvider;

/**
 * Responsible for parsing data into new {@link JavaClass} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public final class JavaParser
{
   public static ServiceLoader<JavaParserProvider> loader = ServiceLoader.load(JavaParserProvider.class);
   private static List<JavaParserProvider> parsers;

   private static JavaParserProvider getParser()
   {
      if (parsers == null)
      {
         parsers = new ArrayList<JavaParserProvider>();
         for (JavaParserProvider p : loader)
         {
            parsers.add(p);
         }
      }
      if (parsers.size() == 0)
      {
         throw new IllegalStateException("No instances of [" + JavaParserProvider.class.getName()
                  + "] were found on the classpath.");
      }
      return parsers.get(0);
   }

   /**
    * Open the given {@link File}, parsing its contents into a new {@link JavaClass} instance.
    */
   public static JavaSource<?> parse(final File file) throws FileNotFoundException
   {
      return getParser().parse(file);
   }

   /**
    * Read the given {@link InputStream} and parse the data into a new {@link JavaClass} instance.
    */
   public static JavaSource<?> parse(final InputStream data)
   {
      return getParser().parse(data);
   }

   /**
    * Parse the given character array into a new {@link JavaClass} instance.
    */
   public static JavaSource<?> parse(final char[] data)
   {
      return getParser().parse(data);
   }

   /**
    * Parse the given String data into a new {@link JavaClass} instance.
    */
   public static JavaSource<?> parse(final String data)
   {
      return getParser().parse(data);
   }

   /**
    * Create a new empty {@link JavaClass} instance.
    */
   public static <T extends JavaSource<?>> T create(final Class<T> type)
   {
      return getParser().create(type);
   }

   /**
    * Read the given {@link File} and parse its data into a new {@link JavaSource} instance of the given type.
    * 
    * @throws FileNotFoundException
    */
   public static <T extends JavaSource<?>> T parse(final Class<T> type, final File file) throws FileNotFoundException
   {
      return getParser().parse(type, file);
   }

   /**
    * Read the given {@link InputStream} and parse its data into a new {@link JavaSource} instance of the given type.
    */
   public static <T extends JavaSource<?>> T parse(final Class<T> type, final InputStream data)
   {
      return getParser().parse(type, data);
   }

   /**
    * Read the given character array and parse its data into a new {@link JavaSource} instance of the given type.
    */
   public static <T extends JavaSource<?>> T parse(final Class<T> type, final char[] data)
   {
      return getParser().parse(type, data);
   }

   /**
    * Read the given string and parse its data into a new {@link JavaSource} instance of the given type.
    */
   public static <T extends JavaSource<?>> T parse(final Class<T> type, final String data)
   {
      return getParser().parse(type, data);
   }
}
