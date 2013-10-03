package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.java.GenericCapable;
import org.jboss.forge.parser.java.TypeVariable;

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