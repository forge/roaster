/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.AnnotationElement;
import org.jboss.forge.roaster.model.JavaType;

/**
 * Represents an element definition of a {@link JavaAnnotationSource}.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Matt Benson
 */
public interface AnnotationElementSource extends AnnotationElement<JavaAnnotationSource>,
         AnnotationTargetSource<JavaAnnotationSource, AnnotationElementSource>, NamedSource<AnnotationElementSource>
{
   /**
    * Represents the default value of an {@link AnnotationElementSource} and provides mechanisms to set that value.
    */
   interface DefaultValue extends AnnotationElement.ReadDefaultValue<JavaAnnotationSource>
   {
      @Override
      AnnotationSource<JavaAnnotationSource> getAnnotation();

      DefaultValue setLiteral(String value);

      DefaultValue setString(String value);

      <T extends Enum<T>> AnnotationElementSource.DefaultValue setEnum(T value);

      <T extends Enum<T>> AnnotationElementSource.DefaultValue setEnumArray(T... values);

      AnnotationSource<JavaAnnotationSource> setAnnotation();

      DefaultValue setSingleClass(Class<?> value);

      DefaultValue setClassArray(Class<?>... values);

   }

   /**
    * Set the type of this {@link AnnotationElement} to the given {@link Class} type. Attempt to add an import statement
    * to this annotation element's base {@link O} if required.
    */
   AnnotationElementSource setType(Class<?> clazz);

   /**
    * Set the type of this {@link AnnotationElement} to the given type. Attempt to add an import statement to this
    * annotation element's base {@link O} if required. (Note that the given className must be fully-qualified in order
    * to properly import required classes)
    */
   AnnotationElementSource setType(String type);

   /**
    * Set the type of this {@link AnnotationElement} to the given {@link JavaSource<?>} type. Attempt to add an import
    * statement to this field's base {@link O} if required.
    */
   AnnotationElementSource setType(JavaType<?> entity);

   @Override
   AnnotationElementSource.DefaultValue getDefaultValue();
}