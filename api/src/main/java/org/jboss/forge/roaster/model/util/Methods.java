/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.MethodHolder;
import org.jboss.forge.roaster.model.source.MethodHolderSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;

/**
 * Utility methods for {@link MethodSource} objects
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Methods
{
   private Methods()
   {
      throw new IllegalAccessError("Utility class");
   }

   /**
    * Implement the abstract methods present in a {@link MethodHolder} to the specified {@link MethodHolderSource}.
    * 
    * @param source the {@link MethodHolder} where the methods will be imported from
    * @param target the {@link MethodHolderSource} where the methods will be exported to
    * @return a {@link List} of the implemented methods added to the provided {@link MethodHolderSource}
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static List<MethodSource<?>> implementAbstractMethods(final MethodHolder<?> source,
            final MethodHolderSource<?> target)
   {
      List<MethodSource<?>> methods = new ArrayList<>();
      for (Method method : source.getMethods())
      {
         if (method.isAbstract() && !target.hasMethod(method))
         {
            MethodSource<?> newMethod = target.addMethod(method);
            implementMethod(newMethod);
            removeAllAnnotations(newMethod);
            methods.add(newMethod);
         }
      }
      return methods;
   }

   public static void removeAllAnnotations(final MethodSource<?> source)
   {
      source.removeAllAnnotations();
      for (ParameterSource<?> parameterSource : source.getParameters())
      {
         parameterSource.removeAllAnnotations();
      }
   }

   /**
    * Implement the abstract methods present in a {@link Class} to the specified {@link MethodHolderSource}.
    * 
    * @param source the {@link Class} where the methods will be imported from
    * @param target the {@link MethodHolderSource} where the methods will be exported to
    * @return a {@link List} of the implemented methods added to the provided {@link MethodHolderSource}
    */
   public static List<MethodSource<?>> implementAbstractMethods(final Class<?> source,
            final MethodHolderSource<?> target)
   {
      List<MethodSource<?>> methods = new ArrayList<>();
      for (java.lang.reflect.Method m : source.getMethods())
      {
         if (m.getDeclaringClass() == Object.class)
            continue;
         if (Modifier.isAbstract(m.getModifiers())
                  && target.getMethod(m.getName(), m.getParameterTypes()) == null)
         {
            MethodSource<?> newMethod = target.addMethod(m);
            implementMethod(newMethod);
            removeAllAnnotations(newMethod);
            methods.add(newMethod);
         }
      }
      return methods;
   }

   /**
    * Adds a default method implementation to the given {@link MethodSource}. This method will call
    * {@link MethodSource#setAbstract(boolean)} with {@code false} before setting the body if the origin is not an
    * interface.
    * 
    * @param source the method where the implementation should be added
    */
   public static void implementMethod(MethodSource<?> source)
   {
      if (source.getOrigin().isInterface())
      {
         source.setBody(null);
      }
      else
      {
         if (source.isNative())
         {
            source.setNative(false);
         }
         source.setAbstract(false);
         if (source.isReturnTypeVoid())
         {
            source.setBody("");
         }
         else
         {
            source.setBody("return " + Types.getDefaultValue(source.getReturnType().getName()) + ";");
         }
      }
   }

   /**
    * Generate the parameter names for given parameter types
    * 
    * @param parameterTypes the parameter types to use
    * @return the parameter names of a given {@link Method}
    */
   public static String[] generateParameterNames(Class<?>[] parameterTypes)
   {
      List<String> parameterNames = new ArrayList<>();
      for (Class<?> paramType : parameterTypes)
      {
         // Check if we haven't already used it.
         String name = toParamName(paramType.getSimpleName());
         String paramName = name;
         int index = 1;
         while (parameterNames.contains(paramName))
         {
            paramName = name + index++;
         }
         parameterNames.add(paramName);
      }
      return parameterNames.toArray(new String[parameterNames.size()]);
   }

   private static String toParamName(String type)
   {
      // Special case for java.lang types
      if (Types.isBasicType(type) || Types.isJavaLang(type))
      {
         return String.valueOf(type.charAt(0)).toLowerCase();
      }

      StringBuilder finalName = new StringBuilder(type.length());

      for (int i = 0; i < type.length(); i++)
      {
         char c = type.charAt(i);

         if (isUpperCase(c))
         {
            finalName.append(toLowerCase(c));
         }
         else if (isLowerCase(c))
         {
            if (i > 1)
            {
               finalName.setCharAt(i - 1, toUpperCase(finalName.charAt(i - 1)));
            }
            finalName.append(type.substring(i));
            break;
         }
      }
      return finalName.toString();
   }
}