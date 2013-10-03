package org.jboss.forge.parser.java.source;

import java.util.List;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaType;
import org.jboss.forge.parser.java.Method;

/**
 * Represents a Java Method in source form.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MethodSource<O extends JavaSource<O>> extends Method<O, MethodSource<O>>, AbstractableSource<MethodSource<O>>,
         MemberSource<O, MethodSource<O>>, GenericCapableSource<O, MethodSource<O>>
{
   /**
    * Set this {@link Method} to return the given type.
    */
   public MethodSource<O> setReturnType(final Class<?> type);

   /**
    * Set the inner body of this {@link Method}
    */
   public MethodSource<O> setBody(final String body);

   /**
    * Toggle this method as a constructor. If true, and the name of the {@link Method} is not the same as the name
    * of its parent {@link JavaClass} , update the name of the to match.
    */
   public MethodSource<O> setConstructor(final boolean constructor);

   /**
    * Set this {@link Method} to return the given type.
    */
   public MethodSource<O> setReturnType(final String type);

   /**
    * Set this {@link Method} to return the given {@link JavaType} type.
    */
   public MethodSource<O> setReturnType(JavaType<?> type);

   /**
    * Set this {@link Method} to return 'void'
    */
   public MethodSource<O> setReturnTypeVoid();

   /**
    * Set this {@link Method}'s parameters.
    */
   public MethodSource<O> setParameters(String string);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   public MethodSource<O> addThrows(String type);

   /**
    * Add a thrown {@link Exception} to this method's signature.
    */
   public MethodSource<O> addThrows(Class<? extends Exception> type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   public MethodSource<O> removeThrows(String type);

   /**
    * Remove a thrown {@link Exception} to this method's signature.
    */
   public MethodSource<O> removeThrows(Class<? extends Exception> type);

   /**
    * Get a list of this {@link Method}'s parameters.
    */
   @Override
   public List<ParameterSource<O>> getParameters();

}