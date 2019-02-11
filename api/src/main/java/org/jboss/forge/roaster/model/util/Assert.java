/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.util;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public class Assert
{
   private Assert()
   {
      // Util class
   }

   public static void isTrue(boolean condition, String message)
   {
      if (!condition)
      {
         throw new IllegalStateException(message);
      }
   }

   public static void isFalse(boolean condition, String message)
   {
      if (condition)
      {
         throw new IllegalStateException(message);
      }
   }

   public static void notNull(Object object, String message)
   {
      if (object == null)
      {
         throw new NullPointerException(message);
      }
   }

   public static <T> T notNull(T object)
   {
      if (object == null)
      {
         throw new NullPointerException("Unexpected null");
      }
      return object;
   }
}