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
public interface ReadVisibilityScoped
{
   boolean isPackagePrivate();

   boolean isPublic();

   boolean isPrivate();

   boolean isProtected();

   Visibility getVisibility();

   public interface VisibilityScoped<T> extends ReadVisibilityScoped
   {
      T setPackagePrivate();

      T setPublic();

      T setPrivate();

      T setProtected();

      T setVisibility(Visibility scope);
   }
}