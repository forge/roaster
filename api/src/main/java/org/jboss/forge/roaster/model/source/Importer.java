/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;

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
    * Return whether or not this {@link O} would require an import to reference the given {@link Class} type.
    */
   boolean requiresImport(Class<?> type);

   /**
    * Return whether or not this {@link O} would require an import to reference the given fully-qualified class name.
    */
   boolean requiresImport(String type);

   /**
    * Return whether or not this {@link O} has an import for the given {@link T} type.
    */
   public <T extends JavaType<T>> boolean hasImport(T type);

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
   public <T extends JavaType<?>> Import getImport(T type);

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
    * Add an import for the given {@link JavaType} type.
    */
   public <T extends JavaType<?>> Import addImport(T type);

   /**
    * Ensures the type passed as argument is included in the list of imports for this java source. The method will also
    * recursively import parameter types. This method is idempotent: if a type has already been imported, no further
    * action will be required. The method returns the name that can be used inside the code to reference the type. The
    * name will be simple if no ambiguity exists with other types having the same (local) name, or fully qualified
    * otherwise.
    * 
    * @param type The {@link org.jboss.forge.roaster.model.Type} to be imported.
    * @return The name (simple or fully qualified) that should be used to reference the imported type in the code
    * @seeAlso org.jboss.forge.roaster.model.Type
    */
   public Import addImport(Type<?> type);

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
   public <T extends JavaType<?>> O removeImport(T type);

   /**
    * Remove the given {@link Import} from this {@link O} instance, if it exists; otherwise, do nothing;
    */
   public O removeImport(Import imprt);

}