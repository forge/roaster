/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster;

/**
 * Represents an object that has a root ancestor that should be made accessible to its clients.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @param <T> the concrete type of the origin
 * 
 */
public interface Origin<T>
{
   /**
    * Returns the root ancestor of this object.
    * 
    * @return the instance of the root ancestor
    */
   T getOrigin();
}