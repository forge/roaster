/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.Origin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public interface Type<O extends JavaType<O>> extends Origin<O>
{
   public abstract List<Type<O>> getTypeArguments();

   public abstract String getName();

   public abstract String getQualifiedName();

   public abstract Type<O> getParentType();

   public abstract boolean isArray();

   public abstract int getArrayDimensions();

   public abstract boolean isParameterized();

   public abstract boolean isPrimitive();

   public abstract boolean isQualified();

   public abstract boolean isWildcard();

}
