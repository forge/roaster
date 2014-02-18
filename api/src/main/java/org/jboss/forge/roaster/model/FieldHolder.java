/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents a {@link JavaType} that may contain field definitions.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface FieldHolder<O extends JavaType<O>> extends MemberHolder<O>
{
   /**
    * Return whether or not this {@link O} declares a {@link Field} with the given name.
    */
   public boolean hasField(String name);

   /**
    * Return whether or not this {@link O} declares the given {@link Field} instance.
    */
   public boolean hasField(Field<O> field);

   /**
    * Get the {@link Field} with the given name and return it, otherwise, return null.
    */
   public Field<O> getField(String name);

   /**
    * Get a list of all {@link Field}s declared by this {@link O}, or return an empty list if no {@link Field}s
    * are declared.
    */
   public List<? extends Field<O>> getFields();
}

