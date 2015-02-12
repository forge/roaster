/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.Internal;

/**
 * Represents an imported element in a {@link JavaSource}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Import extends Internal, StaticCapableSource<Import>
{
   String getPackage();

   String getSimpleName();

   String getQualifiedName();

   boolean isWildcard();

   Import setName(final String name);
}