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
import org.jboss.forge.roaster.model.util.Types;

/**
 * Represents an annotation on a Java element.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @param <O> the java type which can be annotated
 */
public interface Annotation<O extends JavaType<O>> extends Internal, Origin<O>
{
   boolean isSingleValue();

   /**
    * Checks if this annotation is a marker annotation. A marker annotation is defined as {@literal @}{@code TypeName},
    * e.g. {@literal @}{@code Overwrite}. The annotation has no value.
    * 
    * @return {@code true}, if this annotation is a marker annotation, {@code false} otherwise.
    */
   boolean isMarker();

   boolean isNormal();

   /**
    * Get the simple name of this annotation.
    * <p>
    * <b>NOTE:</b> This method returns always the simple name indifferent if the type was imported or not.
    * <p>
    * 
    * @return the simple name
    * @see Types#toSimpleName(String)
    */
   String getName();

   /**
    * Get the qualified name of this annotation.
    * <p>
    * <b>NOTE:</b> This method returns always the qualified name indifferent if the type was imported or not.
    * <p>
    * 
    * @return the qualified name
    */
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

   boolean isTypeElementDefined(String name);
}
