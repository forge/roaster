/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.Origin;

/**
 * Represents a Java element that may carry annotations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationTarget<O extends JavaType<O>> extends Internal, Origin<O>
{

   /**
    * Returns a {@link List} of {@link Annotation} elements bound to this {@link AnnotationTarget} instance
    */
   List<? extends Annotation<O>> getAnnotations();

   /**
    * Check if annotation bound of the given type in this {@link AnnotationTarget} exists
    * 
    * @param type The {@link java.lang.annotation.Annotation} type
    * 
    */
   boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Check if annotation bound of the given type in this {@link AnnotationTarget} exists
    * 
    * @param type The FQN of the annotation
    * 
    */
   boolean hasAnnotation(final String type);

   /**
    * Returns the annotation bound of the given type in this {@link AnnotationTarget} or null if it doesn't exist
    * 
    * @param type The {@link java.lang.annotation.Annotation} type
    * 
    */
   Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Returns the annotation bound of the given type in this {@link AnnotationTarget} or null if it doesn't exist
    * 
    * @param type The FQN of the annotation
    * 
    */
   Annotation<O> getAnnotation(final String type);
}