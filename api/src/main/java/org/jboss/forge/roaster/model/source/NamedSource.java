/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Named;

/**
 * Represents a named Java source element.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface NamedSource<T> extends Named
{

   /**
    * Set the simple-name of this {@link T} instance.
    * 
    * @see #getName()
    */
   public T setName(String name);
}