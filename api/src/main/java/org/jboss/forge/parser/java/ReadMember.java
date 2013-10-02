/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.Origin;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * Represents a {@link ReadJavaSource} member. (I.e.: a {@link ReadField} or {@link ReadMethod})
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadMember<O extends ReadJavaSource<O>> extends ReadVisibilityScoped, ReadAnnotationTarget<O>,
         Origin<O>, ReadNamed
{
   public boolean isFinal();

   public boolean isStatic();

   /**
    * Represents a {@link JavaSource} member. (I.e.: a {@link Field} or {@link Method})
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface Member<O extends JavaSource<O>, T> extends ReadMember<O>, AnnotationTarget<O, T>,
            VisibilityScoped<T>, Named<T>
   {

      public T setFinal(boolean finl);

      public T setStatic(boolean statc);
   }
}
