/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Origin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Type<O extends JavaType<O>> extends Origin<O>
{
   List<Type<O>> getTypeArguments();

   /**
    * Returns the type's name after erasing any type parameters.
    * Preserves array dimensions
    * @return the type's name without type parameters
    */
   String getName();

   /**
    * Returns the type's name, simplifying qualified names based on imports
    * Preserves generic parameters, simplifying them recursively
    * Preserves array dimensions
    * @return the type's simple name
    */
   String getSimpleName();

   /**
    * Returns the type's qualified name, expanding simple names according to imports
    * @return the type's qualified name
    */
   String getQualifiedName();

   /**
    * Returns the type's qualified name, preserving type parameters (which are also qualified)
    * Preserves array dimensions.
    * @return the type's qualified name, including type parameters
    */
   String getQualifiedNameWithGenerics();

   Type<O> getParentType();

   boolean isArray();

   int getArrayDimensions();

   boolean isParameterized();

   boolean isPrimitive();

   boolean isQualified();

   boolean isWildcard();

   boolean isType(Class<?> type);

   boolean isType(String name);

}
