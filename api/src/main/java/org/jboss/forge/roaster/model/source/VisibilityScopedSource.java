/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.VisibilityScoped;

/**
 * Represents a Java source element that has a certain visibility scope.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface VisibilityScopedSource<T> extends VisibilityScoped
{
   T setPackagePrivate();

   T setPublic();

   T setPrivate();

   T setProtected();

   T setVisibility(Visibility scope);
}