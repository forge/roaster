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
   public static final String WILDCARD = "*";

   /**
    * Returns the package part of a import.
    * 
    * @return the package of this import
    */
   String getPackage();

   /**
    * Returns the simple name of a import.
    * 
    * @return the simple class name of a import or {@link #WILDCARD}, if this is a wildcard import.
    * @see Class#getSimpleName()
    */
   String getSimpleName();

   /**
    * Returns the qualified name, so it's the same as '{@code getPackage() + "." + getSimpleName()}'. In the case this
    * is a wildcard import, the package part is returned.
    * 
    * @return the qualified name or the packge if this is a wildcar import
    */
   String getQualifiedName();

   /**
    * Checks if this import is a wildcard ({@code *}) import.
    * 
    * @return true if this is a wildcard import, false otherwise
    */
   boolean isWildcard();

   /**
    * Sets the data of this import object. The data is the part of a {@code import} statement which is between
    * {@code import} and {@code ;}.
    * 
    * <p>
    * This method is <b>not</b> intended to be called from externally.
    * </p>
    * 
    * @param name the actual data of the import
    * @return {@code this}
    */
   Import setName(final String name);
}