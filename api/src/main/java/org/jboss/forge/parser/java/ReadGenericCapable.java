/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import java.util.List;

/**
 * Represents a Java element that may define type variables.
 * 
 * @author mbenson
 *
 */
//TODO consider for methods; this would probably necessitate splitting constructors from methods
public interface ReadGenericCapable
{
   /**
    * Returns all the generic types associated with this object
    */
   List<String> getGenericTypes();

   /**
    * Represents a Java source element that may define type variables.
    * 
    * @author mbenson
    *
    */
   public interface GenericCapable<T> extends ReadGenericCapable
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
   }
}
