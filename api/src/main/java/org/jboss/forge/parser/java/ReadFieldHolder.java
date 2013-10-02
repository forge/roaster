/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.java.ReadField.Field;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * Represents a {@link ReadJavaSource} that may contain field definitions.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadFieldHolder<O extends ReadJavaSource<O>> extends ReadMemberHolder<O>
{
   /**
    * Return whether or not this {@link O} declares a {@link ReadField} with the given name.
    */
   public boolean hasField(String name);

   /**
    * Return whether or not this {@link O} declares the given {@link ReadField} instance.
    */
   public boolean hasField(ReadField<O> field);

   /**
    * Get the {@link ReadField} with the given name and return it, otherwise, return null.
    */
   public ReadField<O> getField(String name);

   /**
    * Get a list of all {@link ReadField}s declared by this {@link O}, or return an empty list if no {@link ReadField}s
    * are declared.
    */
   public List<? extends ReadField<O>> getFields();

   /**
    * Represents a {@link JavaSource} that may contain field definitions.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface FieldHolder<O extends JavaSource<O>> extends ReadFieldHolder<O>, MemberHolder<O>
   {

      /**
       * Add a new Java {@link ReadField} to this {@link O} instance. This field will be a stub until further modified.
       */
      public Field<O> addField();

      /**
       * Add a new {@link ReadField} declaration to this {@link O} instance, using the given {@link String} as the
       * declaration.
       * <p/>
       * <strong>For example:</strong><br>
       * <code>Field f = javaClass.addField("private String newField;");</code>
       */
      public Field<O> addField(final String declaration);

      /**
       * Remove the given {@link ReadField} from this {@link O} instance, if it exists; otherwise, do nothing.
       */
      public O removeField(final ReadField<O> field);
      /**
       * Get the {@link ReadField} with the given name and return it, otherwise, return null.
       */
      public Field<O> getField(String name);
      
      /**
       * Get a list of all {@link ReadField}s declared by this {@link O}, or return an empty list if no {@link ReadField}s
       * are declared.
       */
      public List<Field<O>> getFields();
   }
}

