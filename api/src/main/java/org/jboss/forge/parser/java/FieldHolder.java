/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@SuppressWarnings("rawtypes")
public interface FieldHolder<O extends JavaSource<O>> extends MemberHolder<O, Member>
{
   /**
    * Add a new Java {@link Field} to this {@link O} instance. This field will be a stub until further modified.
    */
   public Field<O> addField();

   /**
    * Add a new {@link Field} declaration to this {@link O} instance, using the given {@link String} as the declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Field f = javaClass.addField("private String newField;");</code>
    */
   public Field<O> addField(final String declaration);

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
    * Get a list of all {@link Field}s declared by this {@link O}, or return an empty list if no {@link Field}s are
    * declared.
    */
   public List<Field<O>> getFields();

   /**
    * Remove the given {@link Field} from this {@link O} instance, if it exists; otherwise, do nothing.
    */
   public O removeField(final Field<O> field);
}
