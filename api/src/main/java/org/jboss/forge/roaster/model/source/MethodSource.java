/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Method;

/**
 * Represents a Java Method in source form.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MethodSource<O extends JavaSource<O>> extends Method<O, MethodSource<O>>, AbstractableSource<MethodSource<O>>,
         MemberSource<O, MethodSource<O>>, GenericCapableSource<O, MethodSource<O>>
{
   /**
    * Set this {@link Method} to return the given type.
    */
   MethodSource<O> setReturnType(final Class<?> type);

   /**
    * Set the inner body of this {@link Method}
    */
   MethodSource<O> setBody(final String body);

   /**
    * Toggle this method as a constructor. If true, and the name of the {@link Method} is not the same as the name
    * of its parent {@link JavaClass} , update the name of the to match.
    */
   MethodSource<O> setConstructor(final boolean constructor);

   /**
    * Set this {@link Method} to return the given type.
    */
   MethodSource<O> setReturnType(final String type);

   /**
    * Set this {@link Method} to return the given {@link JavaType} type.
    */
   MethodSource<O> setReturnType(JavaType<?> type);

   /**
    * Set this {@link Method} to return 'void'
    */
   MethodSource<O> setReturnTypeVoid();

   /**
    * Set this {@link Method}'s parameters.
    */
   MethodSource<O> setParameters(String string);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> addThrows(String type);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> addThrows(Class<? extends Exception> type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> removeThrows(String type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> removeThrows(Class<? extends Exception> type);

   /**
    * Get a list of this {@link Method}'s parameters.
    */
   @Override
   List<ParameterSource<O>> getParameters();

}