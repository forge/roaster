/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import java.util.List;

public interface GenericCapable<T>
{
   /**
    * Adds a generic type
    *
    * @param genericType should never be null
    * @return
    */
   T addGenericType(String genericType);

   /**
    * Removes a generic type
    *
    * @param genericType should never be null
    * @return
    */
   T removeGenericType(String genericType);

   /**
    * Returns all the generic types associated with this object
    */
   List<String> getGenericTypes();
}
