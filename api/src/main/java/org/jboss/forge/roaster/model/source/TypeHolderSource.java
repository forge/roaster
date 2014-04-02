/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.TypeHolder;

/**
 * Represents a {@link JavaSource} that may declare types.
 *
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface TypeHolderSource<T extends JavaSource<T>> extends TypeHolder<T>
{
   /**
    * Return a list containing {@link JavaSource} instances for each nested {@link Class} declaration found within
    * <code>this</code>. Any modification of returned {@link JavaSource} instances will result in modification of the
    * contents contained by <code>this</code> the parent instance.
    *
    */
   List<JavaSource<?>> getNestedClasses();

   @Override
   JavaSource<?> getNestedType(String name);

   /**
    * Creates a nested type in this {@link JavaSource}. Any modification of returned {@link JavaSource} instances will
    * result in modification of the contents contained by <code>this</code> the parent instance.
    */
   <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(Class<NESTED_TYPE> type);

   /**
    * Add a new type declaration to this instance, using the given {@link String} as the declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>JavaClassSource source = javaClass.addNested("public class InnerClass{}");</code>
    */
   <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(final String declaration);

   /**
    * Adds a type as a nested type in this {@link JavaSource}. Any modification of returned {@link JavaSource} instances
    * will result in modification of the contents contained by <code>this</code> the parent instance.
    */
   <NESTED_TYPE extends JavaSource<?>> NESTED_TYPE addNestedType(NESTED_TYPE type);

   /**
    * Remove the nested type instance, if it exists; otherwise, do nothing.
    */
   T removeNestedType(JavaSource<?> type);
}