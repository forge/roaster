/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.expressions.Expression;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.ReturnStatement;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.StatementSource;

import java.util.List;

/**
 * Represents a Java Method in source form.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface MethodSource<O extends JavaSource<O>> extends Method<O, MethodSource<O>>,
         AbstractableSource<MethodSource<O>>,
         MemberSource<O, MethodSource<O>>,
         GenericCapableSource<O, MethodSource<O>>,
         BlockHolder<O>
{
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
    * <b>IMPORTANT</b>: Setting it to true will remove the method body.
    */
   MethodSource<O> setNative(boolean value);

   /**
    * Set this {@link Method} to return the given type.
    */
   MethodSource<O> setReturnType(final Class<?> type);

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
    * Set this {@link Method} to return the given type.
    */
   MethodSource<O> setReturnType(final String type);

   /**
    * Set this {@link Method} to return the given {@link JavaType} type.
    */
   MethodSource<O> setReturnType(JavaType<?> type);

   /**
    * Set this {@link Method} to return 'void'
    */
   MethodSource<O> setReturnTypeVoid();

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

   /**
    * @param body
    * @return
    */
   MethodSource<O> setBody( StatementSource<?,?,?>  body );

   /**
    * @param body
    * @return
    */
   MethodSource<O> setBody( BlockSource<?,?,?> body );

   /**
    * Returns the body of this method as a Roaster Block
    * @return
    */
   BlockSource<O,MethodSource<O>,?> getBodyAsBlock();


}