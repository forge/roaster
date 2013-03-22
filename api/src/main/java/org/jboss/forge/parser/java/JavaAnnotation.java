/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.lang.annotation.Annotation;
import java.util.List;

import org.jboss.forge.parser.JavaParser;

/**
 * Represents a Java {@link Annotation} source file as an in-memory modifiable
 * element. See {@link JavaParser} for various options in generating
 * {@link JavaAnnotation} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaAnnotation extends JavaSource<JavaAnnotation>
{

   /**
    * Add a new Java {@link AnnotationElement} to this {@link JavaAnnotation} instance. This will be a stub until
    * further modified.
    */
   public AnnotationElement addAnnotationElement();

   /**
    * Add a new {@link AnnotationElement} declaration to this {@link JavaAnnotation} instance, using the given
    * {@link String} as the declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>AnnotationElement e = javaClass.addAnnotationElement("String newAnnotationElement();");</code>
    */
   public AnnotationElement addAnnotationElement(final String declaration);

   /**
    * Return whether or not this {@link JavaAnnotation} declares an {@link AnnotationElement} with the given name.
    */
   public boolean hasAnnotationElement(String name);

   /**
    * Return whether or not this {@link JavaAnnotation} declares the given {@link AnnotationElement} instance.
    */
   public boolean hasAnnotationElement(AnnotationElement annotationElement);

   /**
    * Get the {@link AnnotationElement} with the given name and return it, otherwise, return null.
    */
   public AnnotationElement getAnnotationElement(String name);

   /**
    * Get a list of all {@link AnnotationElement}s declared by this {@link JavaAnnotation}, or return an empty list if
    * no {@link AnnotationElement}s are declared.
    */
   public List<AnnotationElement> getAnnotationElements();

   /**
    * Remove the given {@link AnnotationElement} from this {@link JavaAnnotation} instance, if it exists; otherwise, do
    * nothing.
    */
   public JavaAnnotation removeAnnotationElement(final AnnotationElement annotationElement);
}