/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents a {@link JavaType} that may contain property definitions.
 *
 * @param <O> owning {@link JavaType} type
 */
public interface PropertyHolder<O extends JavaType<O>> extends MethodHolder<O>,
         FieldHolder<O>, InterfaceCapable
{
   /**
    * Return whether or not this {@link O} declares a {@link Property} with the given name.
    */
   boolean hasProperty(String name);

   /**
    * Return whether or not this {@link O} declares the given {@link Property} instance.
    */
   boolean hasProperty(Property<O> property);

   /**
    * Get the {@link Property} with the given name and return it, otherwise, return null.
    */
   Property<O> getProperty(String name);

   /**
    * Get a list of all {@link Property Properties} declared by this {@link O} with the given type,
    * or return an empty list if no matching {@link Property Properties} are declared.
    */
   List<? extends Property<O>> getProperties(Class<?> type);

   /**
    * Get a list of all {@link Property Properties} declared by this {@link O}, or return an empty list if no
    * {@link Property Properties} are declared.
    */
   List<? extends Property<O>> getProperties();
}
