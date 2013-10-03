/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.JavaType;
import org.jboss.forge.parser.java.TypeVariable;

/**
 * Represents a type variable of a {@link GenericCapableSource} {@link JavaSource}.
 * 
 * @author mbenson
 * 
 */
public interface TypeVariableSource<O extends JavaSource<O>> extends TypeVariable<O>,
         NamedSource<TypeVariableSource<O>>
{
   /**
    * Set the bounds of this type variable.
    * 
    * @param bounds
    * @return this
    */
   TypeVariableSource<O> setBounds(JavaType<?>... bounds);

   /**
    * Set the bounds of this type variable.
    * 
    * @param bounds
    * @return this
    */
   TypeVariableSource<O> setBounds(Class<?>... bounds);

   /**
    * Set the bounds of this type variable.
    * 
    * @param bounds
    * @return this
    */
   TypeVariableSource<O> setBounds(String... bounds);

   /**
    * Remove any bounds declared on this type variable.
    * 
    * @return this
    */
   TypeVariableSource<O> removeBounds();
}
