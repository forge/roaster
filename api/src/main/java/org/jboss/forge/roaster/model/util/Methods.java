/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.roaster.model.source.MethodHolderSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 * Utility methods for {@link MethodSource} objects
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Methods
{

   /**
    * This will add the inherited abstract methods present in a {@link Class} to the specified
    * {@link MethodHolderSource}
    * 
    * @param source the {@link Class} where the methods will be imported
    * @param target the {@link MethodHolderSource} where the methods will be exported
    */

   public static List<MethodSource<?>> addInheritedAbstractMethods(final Class<?> source,
            final MethodHolderSource<?> target)
   {
      Class<?> currentType = source;
      List<MethodSource<?>> methods = new ArrayList<MethodSource<?>>();
      for (Method m : currentType.getMethods())
      {
         if (m.getDeclaringClass() == Object.class)
            continue;
         if (Modifier.isAbstract(m.getModifiers())
                  && target.getMethod(m.getName(), m.getParameterTypes()) == null)
         {
            MethodSource<?> newMethod = target.addMethod(m);
            methods.add(newMethod);
         }
      }
      return methods;
   }

   /**
    * Generate the parameter names of a given {@link Method}
    */
   public static String[] generateParameterNames(Class<?>[] parameterTypes)
   {
      List<String> parameterNames = new ArrayList<String>();
      for (Class<?> paramType : parameterTypes)
      {
         // Check if we haven't already used it.
         String name = toParamName(paramType.getSimpleName());
         String paramName = name;
         int idx = 1;
         while (parameterNames.contains(paramName))
         {
            paramName = name + idx++;
         }
         parameterNames.add(paramName);
      }
      return parameterNames.toArray(new String[parameterNames.size()]);
   }

   static String toParamName(String type)
   {
      // Special case for java.lang types
      if (Types.isJavaLang(type))
      {
         return String.valueOf(type.charAt(0)).toLowerCase();
      }
      StringBuilder name = new StringBuilder(type);
      int i;
      for (i = 0; i < name.length(); i++)
      {
         if (!Character.isUpperCase(name.charAt(i)))
         {
            // Go back one index
            i--;
            break;
         }
      }
      if (i == 0)
         name.setCharAt(0, Character.toLowerCase(name.charAt(0)));
      else if (i > 0)
         name.replace(0, i, name.substring(0, i).toLowerCase());
      return name.toString();
   }
}
