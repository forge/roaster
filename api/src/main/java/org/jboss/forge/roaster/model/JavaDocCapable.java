/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * Represents a {@link JavaType} that may support {@link JavaDoc}
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaDocCapable<O>
{
   /**
    * Returns the {@link JavaDoc} for this element. Never null.
    */
   JavaDoc<O> getJavaDoc();

   /**
    * Returns if this {@link JavaType} already has a {@link JavaDoc}
    */
   boolean hasJavaDoc();
}
