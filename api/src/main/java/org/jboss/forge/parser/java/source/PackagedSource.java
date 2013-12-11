/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Packaged;

/**
 * Represents a {@link JavaSource} that may be declared as belonging to a particular Java {@code package}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface PackagedSource<T> extends Packaged<T>
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