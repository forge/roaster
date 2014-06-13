/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.Origin;

/**
 * Represents an annotation on some Java element.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Annotation<O extends JavaType<O>> extends Internal, Origin<O>
{
   boolean isSingleValue();

   boolean isMarker();

   boolean isNormal();

   String getName();

   String getQualifiedName();

   <T extends Enum<T>> T getEnumValue(Class<T> type);

   <T extends Enum<T>> T getEnumValue(Class<T> type, String name);

   <T extends Enum<T>> T[] getEnumArrayValue(Class<T> type);

   <T extends Enum<T>> T[] getEnumArrayValue(Class<T> type, String name);

   String getLiteralValue();

   String getLiteralValue(String name);

   List<ValuePair> getValues();

   String getStringValue();

   String getStringValue(String name);

   String[] getStringArrayValue();

   String[] getStringArrayValue(String name);

   Annotation<O> getAnnotationValue();

   Annotation<O> getAnnotationValue(String name);

   Annotation<O>[] getAnnotationArrayValue();

   Annotation<O>[] getAnnotationArrayValue(String name);

   Class<?> getClassValue();

   Class<?> getClassValue(String name);

   Class<?>[] getClassArrayValue();

   Class<?>[] getClassArrayValue(String name);

   boolean hasValue( String name );
}
