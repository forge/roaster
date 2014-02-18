/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Stream utilities.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
abstract class Streams
{
   private Streams()
   {
   }

   /**
    * Return a {@link String} containing the contents of the given {@link InputStream}
    */
   public static String toString(final InputStream stream)
   {
      StringBuilder out = new StringBuilder();
      try
      {
         final char[] buffer = new char[0x10000];
         Reader in = new InputStreamReader(stream, "UTF-8");
         int read;
         do
         {
            read = in.read(buffer, 0, buffer.length);
            if (read > 0)
            {
               out.append(buffer, 0, read);
            }
         }
         while (read >= 0);
      }
      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException(e);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      return out.toString();
   }

   public static void write(final InputStream source, final OutputStream destination)
   {
      try
      {
         final byte[] buffer = new byte[0x10000];
         int read;
         do
         {
            read = source.read(buffer, 0, buffer.length);
            if (read > 0)
            {
               destination.write(buffer, 0, read);
            }
         }
         while (read >= 0);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   public static InputStream fromString(final String data)
   {
      return new ByteArrayInputStream(data.getBytes());
   }

   /**
    * Closes the resource without throwing any exception
    *
    * @param source the resource to be closed. May be null
    */
   public static void closeQuietly(final Closeable source)
   {
      if (source != null)
      {
         try
         {
            source.close();
         }
         catch (IOException ignore)
         {

         }
      }
   }
}
