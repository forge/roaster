/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.Internal;
import org.jboss.forge.parser.Origin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationTarget<O extends JavaSource<O>, T> extends Internal, Origin<O>
{
   /**
    * Add a new annotation instance to this {@link T}. (Note that an import statement must be added manually if
    * required.)
    */
   public abstract Annotation<O> addAnnotation();

   /**
    * Add a new annotation instance to this {@link T}, using the given {@link Class} as the annotation type. Attempt to
    * add an import statement to this object's {@link O} if required.
    */
   public abstract Annotation<O> addAnnotation(Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Add a new annotation instance to this {@link T}, using the given {@link String} className as the annotation type.
    * Attempt to add an import statement to this object's {@link O} if required. (Note that the given className must be
    * fully-qualified in order to properly import required classes)
    */
   public abstract Annotation<O> addAnnotation(final String className);

   public abstract List<Annotation<O>> getAnnotations();

   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   public boolean hasAnnotation(final String type);

   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   public Annotation<O> getAnnotation(final String type);

   public abstract T removeAnnotation(Annotation<O> annotation);
}
