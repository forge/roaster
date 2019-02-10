/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Roaster;

/**
 * Represents a Java {@code class} type. See {@link Roaster} for various options in generating {@link JavaClass}
 * instances.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaClass<O extends JavaClass<O>> extends
         JavaType<O>,
         PropertyHolder<O>,
         GenericCapable<O>,
         Extendable<O>,
         Abstractable,
         TypeHolder<O>,
         FinalCapable,
         StaticCapable
{
   /**
    * Returns <code>true</code> if this {@link JavaClass} represents a local class.
    * 
    * @see JSL 14.3. Local Class Declarations
    * @return <code>true</code> if this {@link JavaClass} represents a local class.
    */
   boolean isLocalClass();

}