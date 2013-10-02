/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;


/**
 * Represents a field of a {@link JavaClass}, {@link JavaInterface}, or {@link JavaEnum}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Field<O extends JavaType<O>> extends Member<O>
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
}
