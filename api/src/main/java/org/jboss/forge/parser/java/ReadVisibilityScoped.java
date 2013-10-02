/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

/**
 * Represents a Java element that has a certain visibility scope.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadVisibilityScoped
{
   boolean isPackagePrivate();

   boolean isPublic();

   boolean isPrivate();

   boolean isProtected();

   Visibility getVisibility();

   /**
    * Represents a Java source element that has a certain visibility scope.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface VisibilityScoped<T> extends ReadVisibilityScoped
   {
      T setPackagePrivate();

      T setPublic();

      T setPrivate();

      T setProtected();

      T setVisibility(Visibility scope);
   }
}