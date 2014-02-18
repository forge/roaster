/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.Origin;

/**
 * Represents a type variable of a {@link GenericCapable} {@link JavaType}.
 * 
 * @author mbenson
 * 
 */
public interface TypeVariable<O extends JavaType<O>> extends Named, Internal, Origin<O>
{
   /**
    * Get the upper bounds of this type variable.
    * 
    * @return (possibly empty) Type[]
    */
   List<Type<O>> getBounds();
}
