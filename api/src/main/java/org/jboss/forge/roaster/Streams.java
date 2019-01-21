/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Various stream utilities.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
abstract class Streams
{
   private Streams()
   {
      // Util class
   }

   /**
    * Return a {@link String} containing the contents of the given {@link InputStream}. The caller is responsible to
    * close the stream.
    * 
    * @param stream the stream to read from
    * @return the content of the stream
    * @throws RuntimeException if a {@link IOException} occurs
    */
   public static String toString(final InputStream stream)
   {
      StringBuilder out = new StringBuilder();

      try
      {
         final char[] buffer = new char[0x10000];
         Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
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
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      return out.toString();
   }
}