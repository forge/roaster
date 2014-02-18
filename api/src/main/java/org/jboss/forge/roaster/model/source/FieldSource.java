/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.JavaType;


/**
 * Represents a field of a {@link JavaClassSource}, {@link JavaInterfaceSource}, or {@link JavaEnumSource}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface FieldSource<O extends JavaSource<O>> extends Field<O>, MemberSource<O, FieldSource<O>>
{

   /**
    * Set the type of this {@link Field} to the given {@link Class} type. Attempt to add an import statement to
    * this field's base {@link O} if required.
    */
   FieldSource<O> setType(Class<?> clazz);

   /**
    * Set the type of this {@link Field} to the given type. Attempt to add an import statement to this field's
    * base {@link O} if required. (Note that the given className must be fully-qualified in order to properly import
    * required classes)
    */
   FieldSource<O> setType(String type);

   /**
    * Set the type of this {@link Field} to the given {@link JavaSource<?>} type. Attempt to add an import
    * statement to this field's base {@link O} if required.
    */
   FieldSource<O> setType(JavaType<?> entity);

   FieldSource<O> setLiteralInitializer(String value);

   FieldSource<O> setStringInitializer(String value);
}