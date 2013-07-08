/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.spi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface JavaParserProvider
{
   /**
    * Open the given {@link File}, parsing its contents into a new {@link JavaSource} instance.
    * 
    * @throws FileNotFoundException
    */
   public JavaSource<?> parse(final File file) throws FileNotFoundException;

   /**
    * Open the given {@link URL}, parsing its contents into a new {@link JavaSource} instance.
    * 
    * @throws FileNotFoundException
    */
   public JavaSource<?> parse(final URL url) throws IOException;

   /**
    * Read the given {@link InputStream} and parse the data into a new {@link JavaSource} instance.
    */
   public JavaSource<?> parse(final InputStream data);

   /**
    * Parse the given character array into a new {@link JavaSource} instance.
    */
   public JavaSource<?> parse(final char[] data);

   /**
    * Parse the given String data into a new {@link JavaSource} instance.
    */
   public JavaSource<?> parse(final String data);

   /**
    * Create a new empty {@link JavaClass} instance.
    */
   public <T extends JavaSource<?>> T create(final Class<T> type);

   /**
    * Read the given {@link File} and parse its data into a new {@link JavaSource} instance of the given type.
    * 
    * @throws FileNotFoundException
    */
   public <T extends JavaSource<?>> T parse(final Class<T> type, final File file) throws FileNotFoundException;

   /**
    * Read the given {@link URL} and parse its data into a new {@link JavaSource} instance of the given type.
    * 
    * @throws FileNotFoundException
    */
   public <T extends JavaSource<?>> T parse(final Class<T> type, final URL url) throws IOException;

   /**
    * Read the given {@link InputStream} and parse its data into a new {@link JavaSource} instance of the given type.
    */
   public <T extends JavaSource<?>> T parse(final Class<T> type, final InputStream data);

   /**
    * Read the given character array and parse its data into a new {@link JavaSource} instance of the given type.
    */
   public <T extends JavaSource<?>> T parse(final Class<T> type, final char[] data);

   /**
    * Read the given string and parse its data into a new {@link JavaSource} instance of the given type.
    */
   public <T extends JavaSource<?>> T parse(final Class<T> type, final String data);
}