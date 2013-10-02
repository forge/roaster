/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadField<O extends ReadJavaSource<O>> extends ReadMember<O>
{
   /**
    * Get this field's type.
    */
   String getType();

   /**
    * Get this field's fully qualified type.
    */
   String getQualifiedType();

   /**
    * Get this field's {@link Type}
    */
   Type<O> getTypeInspector();

   /**
    * Attempt to determine if this field is of the same type as the given type.
    */
   boolean isType(Class<?> type);

   /**
    * Attempt to determine if this field is of the same type as the given type.
    */
   boolean isType(String type);

   String getStringInitializer();

   String getLiteralInitializer();

   /**
    * 
    * @return True if the type of the field is a primitive type
    */
   boolean isPrimitive();

   /**
    * 
    * @return True if the field is transient
    */
   boolean isTransient();

   /**
    * 
    * @return True if the field is volatile
    */
   boolean isVolatile();

   public interface Field<O extends JavaSource<O>> extends ReadField<O>, Member<O, Field<O>>
   {

      /**
       * Set the type of this {@link ReadField} to the given {@link Class} type. Attempt to add an import statement to
       * this field's base {@link O} if required.
       */
      Field<O> setType(Class<?> clazz);

      /**
       * Set the type of this {@link ReadField} to the given type. Attempt to add an import statement to this field's
       * base {@link O} if required. (Note that the given className must be fully-qualified in order to properly import
       * required classes)
       */
      Field<O> setType(String type);

      /**
       * Set the type of this {@link ReadField} to the given {@link JavaSource<?>} type. Attempt to add an import
       * statement to this field's base {@link O} if required.
       */
      Field<O> setType(ReadJavaSource<?> entity);

      Field<O> setLiteralInitializer(String value);

      Field<O> setStringInitializer(String value);
   }
}
