/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * Represents a {@link JavaType} that can extend other types (Java inheritance and interfaces).
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Extendable<O extends JavaType<O>>
{
   /**
    * Get this type's super class.
    * 
    * @see #setSuperType(String)
    */
   String getSuperType();
}