/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public interface Field<O extends JavaSource<O>> extends Member<O, Field<O>>
{
   Field<O> setName(String name);

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

   /**
    * Set the type of this {@link Field} to the given {@link Class} type. Attempt to add an import statement to this
    * field's base {@link O} if required.
    */
   Field<O> setType(Class<?> clazz);

   /**
    * Set the type of this {@link Field} to the given type. Attempt to add an import statement to this field's base
    * {@link O} if required. (Note that the given className must be fully-qualified in order to properly import required
    * classes)
    */
   Field<O> setType(String type);

   /**
    * Set the type of this {@link Field} to the given {@link JavaSource<?>} type. Attempt to add an import statement to
    * this field's base {@link O} if required.
    */
   Field<O> setType(JavaSource<?> entity);

   String getStringInitializer();

   String getLiteralInitializer();

   Field<O> setLiteralInitializer(String value);

   Field<O> setStringInitializer(String value);

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
   
   /**
    * 
    * @return True if the field is an array 
    */
   boolean isArray();
}
