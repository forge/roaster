/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * A {@link Property} is a convenience construct depicting a simple Java bean property.
 *
 * @param <O>
 */
public interface Property<O extends JavaType<O>> extends Named, AnnotationTarget<O>
{
   /**
    * Get this property's {@link Type}.
    */
   Type<O> getType();

   /**
    * Learn whether this property is backed by a {@link Field}.
    */
   boolean hasField();

   /**
    * Get the field that stores the value of the property.
    */
   Field<O> getField();

   /**
    * Learn whether this property is accessible (i.e. has an accessor method).
    */
   boolean isAccessible();

   /**
    * Learn whether this property is mutable (i.e. has a mutator method).
    */
   boolean isMutable();

   /**
    * Get this property's accessor method.
    */
   Method<O, ?> getAccessor();

   /**
    * Get this property's mutator method.
    */
   Method<O, ?> getMutator();

}
