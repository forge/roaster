/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.FieldHolder;

/**
 * Represents a {@link JavaSource} that may contain field definitions.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface FieldHolderSource<O extends JavaSource<O>> extends FieldHolder<O>, MemberHolderSource<O>
{

   /**
    * Add a new Java {@link Field} to this {@link O} instance. This field will be a stub until further modified.
    */
   FieldSource<O> addField();

   /**
    * Add a new {@link Field} declaration to this {@link O} instance, using the given {@link String} as the declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Field f = javaClass.addField("private String newField;");</code>
    */
   FieldSource<O> addField(final String declaration);

   /**
    * Remove the given {@link Field} from this {@link O} instance, if it exists; otherwise, do nothing.
    */
   O removeField(final Field<O> field);

   /**
    * Get the {@link Field} with the given name and return it, otherwise, return null.
    */
   @Override
   FieldSource<O> getField(String name);

   /**
    * Get a list of all {@link Field}s declared by this {@link O}, or return an empty list if no {@link Field}s are
    * declared.
    */
   @Override
   List<FieldSource<O>> getFields();
}