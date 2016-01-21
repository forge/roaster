/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.AnnotationTarget;

/**
 * Represents a Java source element that may carry annotations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationTargetSource<O extends JavaSource<O>, T> extends AnnotationTarget<O>
{
   @Override
   List<AnnotationSource<O>> getAnnotations();

   @Override
   AnnotationSource<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   @Override
   AnnotationSource<O> getAnnotation(final String type);

   /**
    * Add a new annotation instance to this {@link T} (note that an import statement must be added manually if
    * required).
    */
   AnnotationSource<O> addAnnotation();

   /**
    * Add a new annotation instance to this {@link T}, using the given {@link Class} as the annotation type. Attempt to
    * add an import statement to this object's {@link O} if required.
    */
   AnnotationSource<O> addAnnotation(Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Add a new annotation instance to this {@link T}, using the given {@link String} className as the annotation type.
    * Attempt to add an import statement to this object's {@link O} if required (note that the given className must be
    * fully-qualified in order to properly import required classes).
    */
   AnnotationSource<O> addAnnotation(final String className);

   /**
    * Remove an annotation instance from this {@link T}.
    */
   T removeAnnotation(Annotation<O> annotation);
   
   /**
    * Remove all annotations instance from this {@link T}.
    */
   void removeAnnotations();
}