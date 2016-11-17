/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Property;
import org.jboss.forge.roaster.model.PropertyHolder;

/**
 * Represents a {@link JavaSource} that may contain {@link PropertySource} definitions.
 *
 * @param <O> owning {@link JavaSource} type
 */
public interface PropertyHolderSource<O extends JavaSource<O>> extends
         PropertyHolder<O>, MethodHolderSource<O>, FieldHolderSource<O>
{
   /**
    * Add a new {@link Property} declaration to this {@link O} instance.
    */
   PropertySource<O> addProperty(String type, String name);

   /**
    * Add a new {@link Property} declaration to this {@link O} instance.
    */
   PropertySource<O> addProperty(Class<?> type, String name);

   /**
    * Add a new {@link Property} declaration to this {@link O} instance.
    */
   PropertySource<O> addProperty(JavaType<?> type, String name);

   /**
    * Remove the given {@link Property} from this {@link O} instance, if it exists; otherwise, do nothing.
    */
   PropertyHolderSource<O> removeProperty(Property<O> property);

   @Override
   List<PropertySource<O>> getProperties();

   @Override
   List<PropertySource<O>> getProperties(Class<?> type);

   @Override
   PropertySource<O> getProperty(String name);
}
