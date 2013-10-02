/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadPackaged<T>
{
   /**
    * Get the package of this {@link T}, or return null if it is in the default package.
    */
   public String getPackage();

   /**
    * Return whether or not this {@link T} is in the default package.
    */
   public boolean isDefaultPackage();

   public interface Packaged<T> extends ReadPackaged<T>
   {

      /**
       * Set this {@link T}' package.
       */
      public T setPackage(String name);

      /**
       * Set this {@link T} to be in the default package (removes any current package declaration.)
       */
      public T setDefaultPackage();

   }
}
