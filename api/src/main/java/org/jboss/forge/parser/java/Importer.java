/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * Defines the aspect of {@link JavaSource} that handles type imports.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Importer<O extends JavaSource<O>>
{
   /**
    * Return whether or not this {@link O} has an import for the given {@link Class} type.
    */
   boolean hasImport(Class<?> type);

   /**
    * Return whether or not this {@link O} has an import for the given fully-qualified class name.
    */
   boolean hasImport(String type);

   /**
    * Return whether or not this {@link O} could accept an import for the given {@link Class} type.
    */
   boolean requiresImport(Class<?> type);

   /**
    * Return whether or not this {@link O} could accept an import for the given fully-qualified class name.
    */
   boolean requiresImport(String type);

   /**
    * Return whether or not this {@link O} has an import for the given {@link T} type.
    */
   public <T extends ReadJavaSource<T>> boolean hasImport(T type);

   /**
    * Return whether or not this {@link O} has the given {@link Import} type.
    */
   public boolean hasImport(Import imprt);

   /**
    * Get the {@link Import} for the given fully-qualified class name, if it exists; otherwise, return null;
    */
   public Import getImport(String literalValue);

   /**
    * Get the {@link Import} for the given {@link Class} type, if it exists; otherwise, return null;
    */
   public Import getImport(Class<?> type);

   /**
    * Get the {@link Import} for the given {@link T} type, if it exists; otherwise, return null;
    */
   public <T extends ReadJavaSource<?>> Import getImport(T type);

   /**
    * Get the {@link Import} of the given {@link Import} type, if it exists; otherwise, return null;
    */
   public Import getImport(Import imprt);

   /**
    * Get an immutable list of all {@link Import}s currently imported by this {@link O}
    */
   public List<Import> getImports();

   /**
    * Given a simple or qualified type, resolve that type against the available imports and return the referenced type.
    * If the type cannot be resolved, return the given type unchanged.
    */
   public String resolveType(String type);

   /**
    * Add an import by qualified class name. (E.g: "com.example.Imported") unless it is in the provided 'java.lang.*'
    * package.
    */
   public Import addImport(final String className);

   /**
    * Add an import for the given {@link Class} type.
    */
   public Import addImport(final Class<?> type);

   /**
    * Add an import for the given {@link Import} type.
    */
   public Import addImport(Import imprt);

   /**
    * Add an import for the given {@link ReadJavaSource} type.
    */
   public <T extends ReadJavaSource<?>> Import addImport(T type);

   /**
    * Remove any {@link Import} for the given fully-qualified class name, if it exists; otherwise, do nothing;
    */
   public O removeImport(String name);

   /**
    * Remove any {@link Import} for the given {@link Class} type, if it exists; otherwise, do nothing;
    */
   public O removeImport(Class<?> type);

   /**
    * Remove any {@link Import} for the given {@link T} type, if it exists; otherwise, do nothing;
    */
   public <T extends ReadJavaSource<?>> O removeImport(T type);

   /**
    * Remove the given {@link Import} from this {@link O} instance, if it exists; otherwise, do nothing;
    */
   public O removeImport(Import imprt);

}