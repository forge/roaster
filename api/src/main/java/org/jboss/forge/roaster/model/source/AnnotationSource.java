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

   AnnotationSource<O> setAnnotationValue();

   AnnotationSource<O> setAnnotationValue(String name);

   AnnotationSource<O> setClassValue(String name, Class<?> value);

   AnnotationSource<O> setClassValue(Class<?> value);

   AnnotationSource<O> setClassArrayValue(String name, Class<?>... values);

   AnnotationSource<O> setClassArrayValue(Class<?>... values);
}