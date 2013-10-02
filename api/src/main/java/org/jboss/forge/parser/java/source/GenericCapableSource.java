package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.GenericCapable;

/**
 * Represents a Java source element that may define type variables.
 * 
 * @author mbenson
 *
 */
public interface GenericCapableSource<T> extends GenericCapable
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