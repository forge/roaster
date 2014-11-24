/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;

/**
 * Represents an annotation on some Java source element.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface AnnotationSource<O extends JavaType<O>> extends Annotation<O>
{
   @Override
   AnnotationSource<O> getAnnotationValue();

   @Override
   AnnotationSource<O> getAnnotationValue(String name);

   @Override
   AnnotationSource<O>[] getAnnotationArrayValue();

   @Override
   AnnotationSource<O>[] getAnnotationArrayValue(String name);

   AnnotationSource<O> removeValue(String name);

   AnnotationSource<O> removeAllValues();

   AnnotationSource<O> setName(String className);

   AnnotationSource<O> setEnumValue(String name, Enum<?> value);

   AnnotationSource<O> setEnumValue(Enum<?>... value);

   AnnotationSource<O> setEnumArrayValue(String name, Enum<?>... values);

   AnnotationSource<O> setEnumArrayValue(Enum<?>... values);

   AnnotationSource<O> setLiteralValue(String value);

   AnnotationSource<O> setLiteralValue(String name, String value);

   AnnotationSource<O> setStringValue(String value);

   AnnotationSource<O> setStringValue(String name, String value);

   /**
    * Set an annotation value.
    * 
    * @return the nested {@link AnnotationSource}
    */
   AnnotationSource<O> setAnnotationValue();

   /**
    * Set a named annotation value.
    * 
    * @param name
    * @return the nested {@link AnnotationSource}
    */
   AnnotationSource<O> setAnnotationValue(String name);

   /**
    * Add an annotation value.
    * 
    * @return the nested {@link AnnotationSource}
    * @see #addAnnotationValue(String)
    */
   AnnotationSource<O> addAnnotationValue();

   /**
    * Add a named annotation value. When there is no existing annotation or annotation array value for {@code name}, a
    * single unwrapped annotation value will be created (as with {@link #setAnnotationValue(String)}); otherwise an
    * unwrapped annotation value will be promoted to an array and a new element will be added.
    * 
    * @param name
    * @return the nested {@link AnnotationSource}
    */
   AnnotationSource<O> addAnnotationValue(String name);

   /**
    * Add an annotation value.
    * 
    * @return the nested {@link AnnotationSource}
    * @see #addAnnotationValue(String)
    */
   AnnotationSource<O> addAnnotationValue(Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Add an annotation value.
    * 
    * @param name
    * @return the nested {@link AnnotationSource}
    * @see #addAnnotationValue(String)
    */
   AnnotationSource<O> addAnnotationValue(String name, Class<? extends java.lang.annotation.Annotation> type);

   /**
    * Remove {@code element} from the array of values associated with the {@code "value"} annotation element.
    * 
    * @param element
    * @return this, fluently
    */
   AnnotationSource<O> removeAnnotationValue(Annotation<O> element);

   /**
    * Remove {@code element} from the array of values associated with the specified annotation element.
    * 
    * @param name
    * @param element
    * @return this, fluently
    */
   AnnotationSource<O> removeAnnotationValue(String name, Annotation<O> element);

   AnnotationSource<O> setClassValue(String name, Class<?> value);

   AnnotationSource<O> setClassValue(Class<?> value);

   AnnotationSource<O> setClassArrayValue(String name, Class<?>... values);

   AnnotationSource<O> setClassArrayValue(Class<?>... values);

   AnnotationSource<O> setStringArrayValue(String name, String[] values);

   AnnotationSource<O> setStringArrayValue(String[] values);
}