/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Property;

/**
 * Source variant of {@link Property} interface.
 * 
 * @author mbenson
 *
 * @param <O>
 */
public interface PropertySource<O extends JavaSource<O>> extends Property<O>,
         NamedSource<PropertySource<O>>
{
   /**
    * Set the type of this {@link Property} to the given {@link Class} type. Attempt to add an import statement to this
    * Property's base {@link O} if required.
    */
   PropertySource<O> setType(Class<?> clazz);

   /**
    * Set the type of this {@link Property} to the given type. Attempt to add an import statement to this Property's
    * base {@link O} if required. (Note that the given className must be fully-qualified in order to properly import
    * required classes)
    */
   PropertySource<O> setType(String type);

   /**
    * Set the type of this {@link Property} to the given {@link JavaType<?>} type. Attempt to add an import statement to
    * this field's base {@link O} if required.
    */
   PropertySource<O> setType(JavaType<?> entity);

   /**
    * Create the accessor method.
    * 
    * @throws IllegalStateException if property name unset or method already exists
    */
   MethodSource<O> createAccessor();

   /**
    * Override.
    */
   @Override
   MethodSource<O> getAccessor();

   /**
    * Remove the accessor method.
    */
   PropertySource<O> removeAccessor();

   /**
    * Create the mutator method.
    * 
    * @throws IllegalStateException if property name unset or method already exists
    */
   MethodSource<O> createMutator();

   /**
    * Override.
    */
   @Override
   MethodSource<O> getMutator();

   /**
    * Remove the mutator method.
    */
   PropertySource<O> removeMutator();

   /**
    * Set whether this property is accessible.
    */
   PropertySource<O> setAccessible(boolean accessible);

   /**
    * Set whether this property is mutable.
    */
   PropertySource<O> setMutable(boolean mutable);

   /**
    * Create the storing field.
    *
    * @throws IllegalStateException if property name unset or method already exists
    */
   FieldSource<O> createField();

   /**
    * Override.
    */
   @Override
   FieldSource<O> getField();

   /**
    * Remove the storing field.
    */
   PropertySource<O> removeField();

}
