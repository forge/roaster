/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.Origin;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;
import org.jboss.forge.parser.java.ReadParameter.Parameter;

/**
 * Represents a Java Method.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadMethod<O extends ReadJavaSource<O>, T extends ReadMethod<O, T>> extends ReadAbstractable<T>, ReadMember<O>,
         Origin<O>
{
   /**
    * Get the inner body of this {@link ReadMethod}
    */
   public String getBody();

   /**
    * Return true if this {@link ReadMethod} is a constructor for the class in which it is defined.
    */
   public boolean isConstructor();

   /**
    * Get the return type of this {@link ReadMethod} or return null if the return type is void.
    */
   public String getReturnType();

   /**
    * Get the fully qualified return type of this {@link ReadMethod} or return null if the return type is void.
    */
   public String getQualifiedReturnType();

   /**
    * Get the return {@link Type} of this {@link ReadMethod} or return null if the return type is void.
    */
   public Type<O> getReturnTypeInspector();

   /**
    * Return true if this {@link ReadMethod} has a return type of 'void'
    */
   public boolean isReturnTypeVoid();

   /**
    * Get a list of this {@link ReadMethod}'s parameters.
    */
   public List<? extends ReadParameter<O>> getParameters();

   /**
    * Convert this {@link ReadMethod} into a string representing its unique signature.
    */
   public String toSignature();

   /**
    * Get a list of qualified (if possible) {@link Exception} class names thrown by this method.
    */
   public List<String> getThrownExceptions();

   /**
    * Represents a Java Method in source form.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface Method<O extends JavaSource<O>> extends ReadMethod<O, Method<O>>, Abstractable<Method<O>>, Member<O, Method<O>>
   {
      /**
       * Set this {@link ReadMethod} to return the given type.
       */
      public Method<O> setReturnType(final Class<?> type);

      /**
       * Set the inner body of this {@link ReadMethod}
       */
      public Method<O> setBody(final String body);

      /**
       * Toggle this method as a constructor. If true, and the name of the {@link ReadMethod} is not the same as the
       * name of its parent {@link ReadJavaClass} , update the name of the to match.
       */
      public Method<O> setConstructor(final boolean constructor);

      /**
       * Set this {@link ReadMethod} to return the given type.
       */
      public Method<O> setReturnType(final String type);

      /**
       * Set this {@link ReadMethod} to return the given {@link ReadJavaSource} type.
       */
      public Method<O> setReturnType(ReadJavaSource<?> type);

      /**
       * Set this {@link ReadMethod} to return 'void'
       */
      public Method<O> setReturnTypeVoid();

      /**
       * Set this {@link ReadMethod}'s parameters.
       */
      public Method<O> setParameters(String string);

      /**
       * Add a thrown {@link Exception} to this method's signature.
       */
      public Method<O> addThrows(String type);

      /**
       * Add a thrown {@link Exception} to this method's signature.
       */
      public Method<O> addThrows(Class<? extends Exception> type);

      /**
       * Remove a thrown {@link Exception} to this method's signature.
       */
      public Method<O> removeThrows(String type);

      /**
       * Remove a thrown {@link Exception} to this method's signature.
       */
      public Method<O> removeThrows(Class<? extends Exception> type);

      /**
       * Get a list of this {@link ReadMethod}'s parameters.
       */
      public List<Parameter<O>> getParameters();

   }
}