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

   public static MethodSource<?>[] addInheritedAbstractMethods(final Class<?> source,
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
      return methods.toArray(new MethodSource<?>[methods.size()]);
   }

}
