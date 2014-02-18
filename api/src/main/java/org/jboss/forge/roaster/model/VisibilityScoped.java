/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * Represents a Java element that has a certain visibility scope.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface VisibilityScoped
{
   boolean isPackagePrivate();

   boolean isPublic();

   boolean isPrivate();

   boolean isProtected();

   Visibility getVisibility();
}