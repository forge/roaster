/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.GenericCapable;
import org.jboss.forge.roaster.model.TypeVariable;

/**
 * Represents a Java source element that may define type variables.
 * 
 * @author mbenson
 * 
 */
public interface GenericCapableSource<O extends JavaSource<O>, T> extends
         GenericCapable<O>
{
   @Override
   List<TypeVariableSource<O>> getTypeVariables();

   @Override
   TypeVariableSource<O> getTypeVariable(String name);

   /**
    * Adds a type variable.
    * 
    * @return {@link TypeVariableSource}
    */
   TypeVariableSource<O> addTypeVariable();

   /**
    * Removes a type variable.
    * 
    * @param name should never be null
    * @return this
    */
   T removeTypeVariable(String name);

   /**
    * Removes a type variable.
    * 
    * @param typeVariable should never be null
    * @return this
    */
   T removeTypeVariable(TypeVariable<?> typeVariable);
}