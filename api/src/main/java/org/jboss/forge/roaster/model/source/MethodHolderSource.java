/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.MethodHolder;

/**
 * Represents a {@link JavaSource} that may declare methods.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MethodHolderSource<O extends JavaSource<O>> extends MethodHolder<O>, MemberHolderSource<O>
{

   /**
    * Return the {@link MethodSource} with the given name and zero parameters; otherwise return null.
    */
   @Override
   public MethodSource<O> getMethod(final String name);

   /**
    * Return the {@link MethodSource} with the given name and signature types; otherwise return null.
    */
   @Override
   public MethodSource<O> getMethod(final String name, String... paramTypes);

   /**
    * Return the {@link MethodSource} with the given name and signature types; otherwise return null.
    */
   @Override
   public MethodSource<O> getMethod(final String name, Class<?>... paramTypes);

   /**
    * Get a {@link List} of all {@link MethodSource}s declared by this {@link O} instance, if any; otherwise, return an
    * empty {@link List}
    */
   @Override
   public List<MethodSource<O>> getMethods();

   /**
    * Add an uninitialized {@link MethodSource} declaration to this {@link O} instance. This {@link MethodSource} will be a stub
    * until further modified.
    */
   public MethodSource<O> addMethod();

   /**
    * Add a new {@link MethodSource} declaration to this {@link O} instance, using the given {@link String} as the
    * method declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Method m = javaClass.addMethod("public String method() {return \"hello!\";}")</code>
    */
   public MethodSource<O> addMethod(final String method);

   /**
    * Remove the given {@link MethodSource} declaration from this {@link O} instance, if it exists; otherwise, do
    * nothing.
    */
   public O removeMethod(final Method<O, ?> method);

}