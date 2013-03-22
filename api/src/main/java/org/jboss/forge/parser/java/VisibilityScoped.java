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
public interface VisibilityScoped<T>
{
   boolean isPackagePrivate();

   T setPackagePrivate();

   boolean isPublic();

   T setPublic();

   boolean isPrivate();

   T setPrivate();

   boolean isProtected();

   T setProtected();

   Visibility getVisibility();

   T setVisibility(Visibility scope);
}
