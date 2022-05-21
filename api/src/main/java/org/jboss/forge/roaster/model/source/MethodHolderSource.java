/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.Arrays;
import java.util.List;

import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.MethodHolder;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.roaster.model.util.Types;

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
   default MethodSource<O> getMethod(final String name) {
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
    * Return the {@link MethodSource} with the given name and signature types; otherwise return null.
    */
   @Override
   default MethodSource<O> getMethod(final String name, String... paramTypes) {
      for (MethodSource<O> local : getMethods())
      {
         if (local.getName().equals(name))
         {
            List<ParameterSource<O>> localParams = local.getParameters();
            if (paramTypes != null)
            {
               if (localParams.size() == paramTypes.length)
               {
                  boolean matches = true;
                  for (int i = 0; i < localParams.size(); i++)
                  {
                     ParameterSource<O> localParam = localParams.get(i);
                     String type = paramTypes[i];
                     if (!Types.areEquivalent(localParam.getType().getName(), type))
                     {
                        matches = false;
                     }
                  }
                  if (matches)
                     return local;
               }
            }
         }
      }
      return null;
   }

   /**
    * Return the {@link MethodSource} with the given name and signature types; otherwise return null.
    */
   @Override
   default MethodSource<O> getMethod(final String name, Class<?>... paramTypes) {
      final String[] types = paramTypes == null ? new String[0] :
               Arrays.stream(paramTypes).map(Class::getName).toArray(String[]::new);
      return getMethod(name, types);
   }

   /**
    * Get a {@link List} of all {@link MethodSource}s declared by this {@link O} instance, if any; otherwise, return an
    * empty {@link List}
    */
   @Override
   List<MethodSource<O>> getMethods();

   /**
    * Add an uninitialized {@link MethodSource} declaration to this {@link O} instance. This {@link MethodSource} will
    * be a stub until further modified.
    */
   MethodSource<O> addMethod();

   /**
    * Add a new {@link MethodSource} declaration to this {@link O} instance, using the given {@link String} as the
    * method declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Method m = javaClass.addMethod("public String method() {return \"hello!\";}")</code>
    */
   MethodSource<O> addMethod(final String method);

   /**
    * Add a new {@link MethodSource} declaration to this {@link O} instance, using the given
    * {@link java.lang.reflect.Method} as the method declaration.
    * 
    * Abstract {@link java.lang.reflect.Method} objects are not implemented in this method. See
    * {@link Methods#implementMethod(MethodSource)} for more information
    * 
    * @param method The {@link java.lang.reflect.Method} to be added
    * @return a {@link MethodSource} declaration based on the {@link java.lang.reflect.Method} parameter.
    * @see Methods#implementMethod(MethodSource)
    */
   MethodSource<O> addMethod(final java.lang.reflect.Method method);

   /**
    * Add a new {@link MethodSource} declaration to this {@link O} instance, using the given {@link Method} as the
    * method declaration.
    * 
    * Abstract {@link Method} objects are not implemented in this method. See
    * {@link Methods#implementMethod(MethodSource)} for more information
    * 
    * @param method The {@link Method} to be added
    * @return a {@link MethodSource} declaration based on the {@link Method} parameter.
    * @see Methods#implementMethod(MethodSource)
    */
   MethodSource<O> addMethod(final Method<?, ?> method);

   /**
    * Remove the given {@link MethodSource} declaration from this {@link O} instance, if it exists; otherwise, do
    * nothing.
    */
   O removeMethod(final Method<O, ?> method);

}