/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;
import org.jboss.forge.parser.java.ReadMethod.Method;

/**
 * Represents a {@link ReadJavaSource} that may declare methods.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadMethodHolder<O extends ReadJavaSource<O>> extends ReadMemberHolder<O>
{
   /**
    * Return true if this {@link O} has a method with the given name and zero parameters; otherwise return false.
    */
   public boolean hasMethod(final ReadMethod<O, ?> name);

   /**
    * Return true if this {@link O} has a method with signature matching the given method's signature.
    */
   public boolean hasMethodSignature(final ReadMethod<?, ?> method);

   /**
    * Return true if this {@link O} has a method with the given name and zero parameters; otherwise return false.
    */
   public boolean hasMethodSignature(final String name);

   /**
    * Return true if this {@link O} has a method with the given name and signature types; otherwise return false.
    */
   public boolean hasMethodSignature(final String name, String... paramTypes);

   /**
    * Return true if this {@link O} has a method with the given name and signature types; otherwise return false.
    */
   public boolean hasMethodSignature(final String name, Class<?>... paramTypes);

   /**
    * Return the {@link ReadMethod} with the given name and zero parameters; otherwise return null.
    */
   public ReadMethod<O, ?> getMethod(final String name);

   /**
    * Return the {@link ReadMethod} with the given name and signature types; otherwise return null.
    */
   public ReadMethod<O, ?> getMethod(final String name, String... paramTypes);

   /**
    * Return the {@link ReadMethod} with the given name and signature types; otherwise return null.
    */
   public ReadMethod<O, ?> getMethod(final String name, Class<?>... paramTypes);

   /**
    * Get a {@link List} of all {@link ReadMethod}s declared by this {@link O} instance, if any; otherwise, return an
    * empty {@link List}
    */
   public List<? extends ReadMethod<O, ?>> getMethods();

   /**
    * Represents a {@link JavaSource} that may declare methods.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface MethodHolder<O extends JavaSource<O>> extends ReadMethodHolder<O>, MemberHolder<O>
   {

      /**
       * Return the {@link Method} with the given name and zero parameters; otherwise return null.
       */
      public Method<O> getMethod(final String name);

      /**
       * Return the {@link Method} with the given name and signature types; otherwise return null.
       */
      public Method<O> getMethod(final String name, String... paramTypes);

      /**
       * Return the {@link Method} with the given name and signature types; otherwise return null.
       */
      public Method<O> getMethod(final String name, Class<?>... paramTypes);

      /**
       * Get a {@link List} of all {@link Method}s declared by this {@link O} instance, if any; otherwise, return an
       * empty {@link List}
       */
      public List<Method<O>> getMethods();

      /**
       * Add an uninitialized {@link Method} declaration to this {@link O} instance. This {@link Method} will be a stub
       * until further modified.
       */
      public Method<O> addMethod();

      /**
       * Add a new {@link Method} declaration to this {@link O} instance, using the given {@link String} as the
       * method declaration.
       * <p/>
       * <strong>For example:</strong><br>
       * <code>Method m = javaClass.addMethod("public String method() {return \"hello!\";}")</code>
       */
      public Method<O> addMethod(final String method);

      /**
       * Remove the given {@link Method} declaration from this {@link O} instance, if it exists; otherwise, do
       * nothing.
       */
      public O removeMethod(final ReadMethod<O, ?> method);

   }
}