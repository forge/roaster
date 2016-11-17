/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.lang.reflect.Method;

/**
 * Represents a named Java element.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Named
{
   /**
    * Get the simple name of this {@link T} instance. (E.g: For a Java type, this is be equivalent to calling,
    * <code>{@link Class#getSimpleName()}.</code> For a Java method, this would be equivalent to calling
    * {@link Method#getName()} ... and so on.
    */
   String getName();
}
