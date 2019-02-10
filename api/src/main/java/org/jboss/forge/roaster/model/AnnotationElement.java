/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * Represents an element definition of a {@link JavaAnnotation}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Matt Benson
 */
public interface AnnotationElement<O extends JavaAnnotation<O>> extends AnnotationTarget<O>,
         Named
{
   /**
    * Represents the default value of a given {@link AnnotationElement}.
    */
   interface ReadDefaultValue<O extends JavaAnnotation<O>>
   {
      String getString();

      String getLiteral();

      <T extends Enum<T>> T getEnum(Class<T> type);

      <T extends Enum<T>> T[] getEnumArray(Class<T> type);

      Annotation<O> getAnnotation();

      Class<?> getSingleClass();

      Class<?>[] getClassArray();
   }

   /**
    * Get this annotation element's {@link Type}.
    */
   Type<O> getType();

   ReadDefaultValue<O> getDefaultValue();
}
