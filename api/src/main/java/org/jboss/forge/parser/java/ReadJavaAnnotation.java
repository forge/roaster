/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadAnnotationElement.AnnotationElement;

/**
 * Represents a Java {@code @interface} annotation type. See {@link JavaParser} for various options in generating
 * {@link ReadJavaAnnotation} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ReadJavaAnnotation<O extends ReadJavaAnnotation<O>> extends ReadJavaSource<O>
{

   /**
    * Return whether or not this {@link ReadJavaAnnotation} declares an {@link ReadAnnotationElement} with the given
    * name.
    */
   public boolean hasAnnotationElement(String name);

   /**
    * Return whether or not this {@link ReadJavaAnnotation} declares the given {@link ReadAnnotationElement} instance.
    */
   public boolean hasAnnotationElement(ReadAnnotationElement<?> annotationElement);

   /**
    * Get the {@link ReadAnnotationElement} with the given name and return it, otherwise, return null.
    */
   public ReadAnnotationElement<O> getAnnotationElement(String name);

   /**
    * Get a list of all {@link ReadAnnotationElement}s declared by this {@link ReadJavaAnnotation}, or return an empty
    * list if no {@link ReadAnnotationElement}s are declared.
    */
   public List<? extends ReadAnnotationElement<O>> getAnnotationElements();

   /**
    * Represents a Java {@code @interface} annotation source file as an in-memory modifiable element. See
    * {@link JavaParser} for various options in generating {@link JavaAnnotation} instances.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    */
   public interface JavaAnnotation extends ReadJavaAnnotation<JavaAnnotation>, JavaSource<JavaAnnotation>
   {
      /**
       * Get the {@link AnnotationElement} with the given name and return it, otherwise, return null.
       */
      public AnnotationElement getAnnotationElement(String name);

      /**
       * Get a list of all {@link AnnotationElement}s declared by this {@link ReadJavaAnnotation}, or return an empty
       * list if no {@link AnnotationElement}s are declared.
       */
      public List<AnnotationElement> getAnnotationElements();

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
       * Remove the given {@link ReadAnnotationElement} from this {@link JavaAnnotation} instance, if it exists;
       * otherwise, do nothing.
       */
      public JavaAnnotation removeAnnotationElement(final ReadAnnotationElement<?> annotationElement);
   }
}