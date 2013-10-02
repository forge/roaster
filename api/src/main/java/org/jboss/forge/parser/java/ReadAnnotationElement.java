/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.Origin;
import org.jboss.forge.parser.java.ReadAnnotation.Annotation;
import org.jboss.forge.parser.java.ReadJavaAnnotation.JavaAnnotation;

/**
 * Represents an element definition of a {@link ReadJavaAnnotation}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Matt Benson
 */
public interface ReadAnnotationElement<O extends ReadJavaAnnotation<O>> extends ReadAnnotationTarget<O>,
         Origin<O>, ReadNamed
{
   /**
    * Represents the default value of a given annotation element and provides mechanisms to set that value.
    */
   public interface ReadDefaultValue<O extends ReadJavaAnnotation<O>>
   {
      String getString();

      String getLiteral();

      <T extends Enum<T>> T getEnum(Class<T> type);

      <T extends Enum<T>> T[] getEnumArray(Class<T> type);

      ReadAnnotation<O> getAnnotation();

      Class<?> getSingleClass();

      Class<?>[] getClassArray();
   }

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
   Type<O> getTypeInspector();

   /**
    * Attempt to determine if this annotation element is of the same type as the given type.
    */
   boolean isType(Class<?> type);

   /**
    * Attempt to determine if this annotation element is of the same type as the given type.
    */
   boolean isType(String type);

   ReadDefaultValue<O> getDefaultValue();

   public interface AnnotationElement extends ReadAnnotationElement<JavaAnnotation>,
            AnnotationTarget<JavaAnnotation, AnnotationElement>, Named<AnnotationElement>
   {
      /**
       * Represents an element definition of a {@link JavaAnnotation}.
       * 
       * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
       * @author Matt Benson
       */
      public interface DefaultValue extends ReadDefaultValue<JavaAnnotation>
      {
         Annotation<JavaAnnotation> getAnnotation();

         DefaultValue setLiteral(String value);

         DefaultValue setString(String value);

         <T extends Enum<T>> DefaultValue setEnum(T value);

         <T extends Enum<T>> DefaultValue setEnumArray(T... values);

         Annotation<JavaAnnotation> setAnnotation();

         DefaultValue setSingleClass(Class<?> value);

         DefaultValue setClassArray(Class<?>... values);

      }

      /**
       * Set the type of this {@link ReadAnnotationElement} to the given {@link Class} type. Attempt to add an import
       * statement to this annotation element's base {@link O} if required.
       */
      AnnotationElement setType(Class<?> clazz);

      /**
       * Set the type of this {@link ReadAnnotationElement} to the given type. Attempt to add an import statement to
       * this annotation element's base {@link O} if required. (Note that the given className must be fully-qualified in
       * order to properly import required classes)
       */
      AnnotationElement setType(String type);

      /**
       * Set the type of this {@link ReadAnnotationElement} to the given {@link JavaSource<?>} type. Attempt to add an
       * import statement to this field's base {@link O} if required.
       */
      AnnotationElement setType(ReadJavaSource<?> entity);

      DefaultValue getDefaultValue();
   }
}
