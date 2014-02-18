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

   String getName();

   String getQualifiedName();

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
