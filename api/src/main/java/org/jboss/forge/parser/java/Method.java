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
public interface Method<O extends JavaType<O>, T extends Method<O, T>> extends Abstractable<T>, Member<O>,
         GenericCapable,
         Origin<O>
{
   /**
    * Get the inner body of this {@link Method}
    */
   public String getBody();

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
    * Return true if this {@link Method} has a return type of 'void'
    */
   public boolean isReturnTypeVoid();

   /**
    * Get a list of this {@link Method}'s parameters.
    */
   public List<? extends Parameter<O>> getParameters();

   /**
    * Convert this {@link Method} into a string representing its unique signature.
    */
   public String toSignature();

   /**
    * Get a list of qualified (if possible) {@link Exception} class names thrown by this method.
    */
   public List<String> getThrownExceptions();
}