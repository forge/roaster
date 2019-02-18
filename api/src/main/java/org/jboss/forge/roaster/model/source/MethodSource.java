/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.Type;

/**
 * Represents a Java Method in source form.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface MethodSource<O extends JavaSource<O>> extends Method<O, MethodSource<O>>,
         AbstractableSource<MethodSource<O>>,
         MemberSource<O, MethodSource<O>>, GenericCapableSource<O, MethodSource<O>>
{
   /**
    * Sets the <b>default</b> keyword in this method
    * 
    * @param value if this method should have the default keywork set
    * @return the generic element this interface is bound to
    */
   MethodSource<O> setDefault(boolean value);

   /**
    * Sets the <b>synchronized</b> keyword in this element.
    * 
    * @param value if this element should be set to synchronized
    * @return the generic element this interface is bound to
    */
   MethodSource<O> setSynchronized(boolean value);

   /**
    * Sets this method to be <b>native</b>
    * 
    * <b>IMPORTANT</b>: Setting it to <code>true</code> will remove the method body.
    */
   MethodSource<O> setNative(boolean value);

   /**
    * Set this {@link Method} to return the given type. The type is properly imported if possible.
    * 
    * @param type the return type
    * @return this
    * @see Importer#addImport(String)
    */
   MethodSource<O> setReturnType(final Class<?> type);

   /**
    * Set this {@link Method} to return the given type. The type is properly imported if possible.
    * 
    * @param type the return type
    * @return this
    * @see Importer#addImport(String)
    */
   MethodSource<O> setReturnType(final String type);

   /**
    * Set this {@link Method} to return the given type. The type is properly imported if possible.
    * 
    * @param type the return type
    * @return this
    * @see Importer#addImport(String)
    */
   MethodSource<O> setReturnType(JavaType<?> type);

   /**
    * Set this {@link Method} to return the given type. The type is properly imported if possible.
    * 
    * @param type the return type
    * @return this
    * @see Importer#addImport(String)
    */
   MethodSource<O> setReturnType(Type<?> type);

   /**
    * Set this {@link Method} to return 'void'
    */
   MethodSource<O> setReturnTypeVoid();

   /**
    * Set the inner body of this {@link Method}
    */
   MethodSource<O> setBody(final String body);

   /**
    * Toggle this method as a constructor. If true, and the name of the {@link Method} is not the same as the name of
    * its parent {@link JavaClass} , update the name of the method to match.
    */
   MethodSource<O> setConstructor(final boolean constructor);

   /**
    * Set this {@link Method}'s parameters.
    */
   MethodSource<O> setParameters(String string);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> addThrows(String type);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> addThrows(Class<? extends Exception> type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> removeThrows(String type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   MethodSource<O> removeThrows(Class<? extends Exception> type);

   /**
    * Get a list of this {@link Method}'s parameters.
    */
   @Override
   List<ParameterSource<O>> getParameters();

   /**
    * Add a parameter with the specified {@link Class} type and name to this method
    */
   ParameterSource<O> addParameter(Class<?> type, String name);

   /**
    * Add a parameter with the specified type and name to this method
    */
   ParameterSource<O> addParameter(String type, String name);

   /**
    * Add a parameter with the specified {@link JavaType} type and name to this method
    */
   ParameterSource<O> addParameter(JavaType<?> type, String name);

   /**
    * Remove a parameter from this method
    */
   MethodSource<O> removeParameter(ParameterSource<O> parameter);

   /**
    * Remove a parameter with the specified {@link Class} type and name from this method
    */
   MethodSource<O> removeParameter(Class<?> type, String name);

   /**
    * Remove a parameter with the specified type and name from this method
    */
   MethodSource<O> removeParameter(String type, String name);

   /**
    * Remove a parameter with the specified {@link JavaType} type and name to this method
    */
   MethodSource<O> removeParameter(JavaType<?> type, String name);

}