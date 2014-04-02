/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents a {@link JavaType} that may declare types.
 *
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface TypeHolder<O extends JavaType<O>>
{
   /**
    * Return a list containing {@link JavaType} instances for each nested {@link Class} declaration found within
    * <code>this</code>. Any modification of returned {@link JavaType} instances will result in modification of the
    * contents contained by <code>this</code> the parent instance.
    *
    * TODO: Should be renamed to getNestedTypes()
    */
   List<? extends JavaType<?>> getNestedClasses();

   /**
    * Return whether or not this {@link O} declares a type with the given name.
    */
   boolean hasNestedType(String name);

   /**
    * Return whether or not this {@link O} declares the given {@link JavaType} instance.
    */
   boolean hasNestedType(JavaType<?> type);

   /**
    * Return whether or not this {@link O} declares the given {@link Class} instance.
    */
   boolean hasNestedType(Class<?> type);

   /**
    * Get the {@link JavaType} with the given name and return it, otherwise, return null.
    */
   JavaType<?> getNestedType(String name);
}