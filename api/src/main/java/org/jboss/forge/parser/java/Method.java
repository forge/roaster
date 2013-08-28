/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.Origin;

/**
 * Represents a Java Method.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Method<O extends JavaSource<O>> extends Abstractable<Method<O>>, Member<O, Method<O>>, Origin<O>
{
   /**
    * Get the inner body of this {@link Method}
    */
   public String getBody();

   /**
    * Set the inner body of this {@link Method}
    */
   public Method<O> setBody(final String body);

   /**
    * Toggle this method as a constructor. If true, and the name of the {@link Method} is not the same as the name of
    * its parent {@link JavaClass} , update the name of the to match.
    */
   public Method<O> setConstructor(final boolean constructor);

   /**
    * Return true if this {@link Method} is a constructor for the class in which it is defined.
    */
   public boolean isConstructor();

   /**
    * Get the return type of this {@link Method} or return null if the return type is void.
    */
   public String getReturnType();

   /**
    * Get the fully qualified return type of this {@link Method} or return null if the return type is void.
    */
   public String getQualifiedReturnType();

   /**
    * Get the return {@link Type} of this {@link Method} or return null if the return type is void.
    */
   public Type<O> getReturnTypeInspector();

   /**
    * Set this {@link Method} to return the given type.
    */
   public Method<O> setReturnType(final Class<?> type);

   /**
    * Set this {@link Method} to return the given type.
    */
   public Method<O> setReturnType(final String type);

   /**
    * Set this {@link Method} to return the given {@link JavaSource} type.
    */
   public Method<O> setReturnType(JavaSource<?> type);

   /**
    * Return true if this {@link Method} has a return type of 'void'
    */
   public boolean isReturnTypeVoid();

   /**
    * Set this {@link Method} to return 'void'
    */
   public Method<O> setReturnTypeVoid();

   /**
    * Set this {@link Method}'s parameters.
    */
   public Method<O> setParameters(String string);

   /**
    * Get a list of this {@link Method}'s parameters.
    */
   public List<Parameter<O>> getParameters();

   /**
    * Convert this {@link Method} into a string representing its unique signature.
    */
   public String toSignature();

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   public Method<O> addThrows(String type);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   public Method<O> addThrows(Class<? extends Exception> type);

   /**
    * Get a list of qualified (if possible) {@link Exception} class names thrown by this method.
    */
   public List<String> getThrownExceptions();

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   public Method<O> removeThrows(String type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   public Method<O> removeThrows(Class<? extends Exception> type);

}