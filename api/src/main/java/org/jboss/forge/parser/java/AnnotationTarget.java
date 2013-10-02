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
 * Represents a Java element that may carry annotations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationTarget<O extends JavaType<O>> extends Internal, Origin<O>
{
   public List<? extends Annotation<O>> getAnnotations();

   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   public boolean hasAnnotation(final String type);

   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type);

   public Annotation<O> getAnnotation(final String type);
}