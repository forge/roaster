/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@link JavaType} that may declare methods.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MethodHolder<O extends JavaType<O>> extends MemberHolder<O>
{
   /**
    * Return true if this {@link O} has a method with the given name and zero parameters; otherwise return false.
    */
   default boolean hasMethod(final Method<O, ?> name) {
      return getMethods().contains(name);
   }

   /**
    * Return true if this {@link O} has a method with signature matching the given method's signature.
    */
   default boolean hasMethodSignature(final Method<?, ?> method) {
      for (var local : getMethods())
      {
         if (local.getName().equals(method.getName()))
         {
            var localParams = local.getParameters().iterator();
            for (Parameter<? extends JavaType<?>> methodParam : method.getParameters())
            {
               if (localParams.hasNext()
                        && Objects.equals(localParams.next().getType().getName(), methodParam.getType().getName()))
               {
                  continue;
               }
               return false;
            }
            return !localParams.hasNext();
         }
      }
      return false;
   }

   /**
    * Return true if this {@link O} has a method with the given name and zero parameters; otherwise return false.
    */
   default boolean hasMethodSignature(final String name) {
      return hasMethodSignature(name, new String[] {});
   }

   /**
    * Return true if this {@link O} has a method with the given name and signature types; otherwise return false.
    */
   default boolean hasMethodSignature(final String name, String... paramTypes) {
      return getMethod(name, paramTypes) != null;
   }

   /**
    * Return true if this {@link O} has a method with the given name and signature types; otherwise return false.
    */
   default boolean hasMethodSignature(final String name, Class<?>... paramTypes) {
      final String[] types = paramTypes == null ? new String[0] :
               Arrays.stream(paramTypes).map(Class::getName).toArray(String[]::new);
      return hasMethodSignature(name, types);
   }

   /**
    * Return the {@link Method} with the given name and zero parameters; otherwise return null.
    */
   default Method<O, ?> getMethod(final String name) {
      for (var method : getMethods())
      {
         if (method.getName().equals(name) && (method.getParameters().size() == 0))
         {
            return method;
         }
      }
      return null;
   }

   /**
    * Return the {@link Method} with the given name and signature types; otherwise return null.
    */
   Method<O, ?> getMethod(final String name, String... paramTypes);

   /**
    * Return the {@link Method} with the given name and signature types; otherwise return null.
    */
   Method<O, ?> getMethod(final String name, Class<?>... paramTypes);

   /**
    * Get a {@link List} of all {@link Method}s declared by this {@link O} instance, if any; otherwise, return an empty
    * {@link List}
    */
   List<? extends Method<O, ?>> getMethods();
}