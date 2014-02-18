/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Roaster;

/**
 * Represents a Java {@code @interface} annotation type. See {@link Roaster} for various options in generating
 * {@link JavaAnnotation} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaAnnotation<O extends JavaAnnotation<O>> extends JavaType<O>
{

   /**
    * Return whether or not this {@link JavaAnnotation} declares an {@link AnnotationElement} with the given
    * name.
    */
   public boolean hasAnnotationElement(String name);

   /**
    * Return whether or not this {@link JavaAnnotation} declares the given {@link AnnotationElement} instance.
    */
   public boolean hasAnnotationElement(AnnotationElement<?> annotationElement);

   /**
    * Get the {@link AnnotationElement} with the given name and return it, otherwise, return null.
    */
   public AnnotationElement<O> getAnnotationElement(String name);

   /**
    * Get a list of all {@link AnnotationElement}s declared by this {@link JavaAnnotation}, or return an empty
    * list if no {@link AnnotationElement}s are declared.
    */
   public List<? extends AnnotationElement<O>> getAnnotationElements();
}