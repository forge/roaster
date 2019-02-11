/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents a Java Method.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Method<O extends JavaType<O>, T extends Method<O, T>> extends Abstractable,
         Member<O>
{
   /**
    * @return <code>true</code> if this method is synchronized
    */
   boolean isSynchronized();

   /**
    * @return <code>true</code> if this method is native
    */
   boolean isNative();

   /**
    * Get the inner body of this {@link Method}
    */
   String getBody();

   /**
    * Return <code>true</code> if this {@link Method} is a constructor for the class in which it is defined.
    */
   boolean isConstructor();

   /**
    * Get the return {@link Type} of this {@link Method}.
    */
   Type<O> getReturnType();

   /**
    * Convenience method to learn whether the {@link Method} has a primitive {@code void} return type.
    */
   boolean isReturnTypeVoid();

   /**
    * Get a list of this {@link Method}'s parameters.
    */
   List<? extends Parameter<O>> getParameters();

   /**
    * Convert this {@link Method} into a string representing its unique signature.
    */
   String toSignature();

   /**
    * Get a list of qualified (if possible) {@link Exception} class names thrown by this method.
    */
   List<String> getThrownExceptions();

   /**
    * @return <code>true</code> if this method is declared as default (JDK 8)
    */
   boolean isDefault();
}