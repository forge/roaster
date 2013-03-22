/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.Origin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Matt Benson
 */
public interface AnnotationElement extends AnnotationTarget<JavaAnnotation, AnnotationElement>, Origin<JavaAnnotation>
{
   public interface DefaultValue
   {
      String getString();

      String getLiteral();

      <T extends Enum<T>> T getEnum(Class<T> type);

      <T extends Enum<T>> T[] getEnumArray(Class<T> type);

      Annotation<JavaAnnotation> getAnnotation();

      Class<?> getSingleClass();

      Class<?>[] getClassArray();

      DefaultValue setLiteral(String value);

      DefaultValue setString(String value);

      <T extends Enum<T>> DefaultValue setEnum(T value);

      <T extends Enum<T>> DefaultValue setEnumArray(T... values);

      Annotation<JavaAnnotation> setAnnotation();

      DefaultValue setSingleClass(Class<?> value);

      DefaultValue setClassArray(Class<?>... values);

   }

   String getName();

   AnnotationElement setName(String name);

   /**
    * Get this annotation element's type.
    */
   String getType();

   /**
    * Get this annotation element's fully qualified type.
    */
   String getQualifiedType();

   /**
    * Get this annotation element's {@link Type}
    */
   Type<JavaAnnotation> getTypeInspector();

   /**
    * Attempt to determine if this annotation element is of the same type as the given type.
    */
   boolean isType(Class<?> type);

   /**
    * Attempt to determine if this annotation element is of the same type as the given type.
    */
   boolean isType(String type);

   /**
    * Set the type of this {@link AnnotationElement} to the given {@link Class} type. Attempt to add an import statement
    * to this annotation element's base {@link O} if required.
    */
   AnnotationElement setType(Class<?> clazz);

   /**
    * Set the type of this {@link AnnotationElement} to the given type. Attempt to add an import statement to this
    * annotation element's base {@link O} if required. (Note that the given className must be fully-qualified in order
    * to properly import required classes)
    */
   AnnotationElement setType(String type);

   /**
    * Set the type of this {@link AnnotationElement} to the given {@link JavaSource<?>} type. Attempt to add an import
    * statement to this field's base {@link O} if required.
    */
   AnnotationElement setType(JavaSource<?> entity);

   DefaultValue getDefaultValue();
}
