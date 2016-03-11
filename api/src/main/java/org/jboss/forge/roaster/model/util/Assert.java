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
   private Assert() {}

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

   public static void notNull(Object object, String message) throws IllegalStateException
   {
      if (object == null)
      {
         throw new IllegalStateException(message);
      }
   }
}
