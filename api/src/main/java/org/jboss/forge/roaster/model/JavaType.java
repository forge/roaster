/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Origin;

/**
 * Represents a Java type.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 */
public interface JavaType<T extends JavaType<T>> extends
         Packaged<T>,
         Named,
         VisibilityScoped,
         AnnotationTarget<T>,
         Origin<T>
{
   /**
    * Return the canonical name of this {@link T} instance. This is equivalent to calling
    * {@link Class#getCanonicalName()}
    */
   public String getCanonicalName();

   /**
    * Get the qualified-name of this {@link T} instance, where the qualified-name contains both the Java package and
    * simple class name of the type represented by this {@link T} instance.
    * <p/>
    * <strong>For example</strong>, calling: {@link #getQualifiedName()} is equivalent to calling "{@link #getPackage()}
    * + "." + {@link #getName()}", which in turn is equivalent to calling: {@link Class#getName()}
    */
   public String getQualifiedName();

   /**
    * Get a list of all {@link SyntaxError}s detected in the current {@link T}. Note that when errors are present, the
    * class may still be modified, but changes may not be completely accurate.
    */
   public List<SyntaxError> getSyntaxErrors();

   /**
    * Return whether or not this {@link T} currently has any {@link SyntaxError} s.
    */
   public boolean hasSyntaxErrors();

   /**
    * Return true if this {@link JavaType} represents a {@link JavaClass}
    */
   public boolean isClass();

   /**
    * Return true if this {@link JavaType} represents a {@link JavaEnum}
    */
   public boolean isEnum();

   /**
    * Return true if this {@link JavaType} represents a {@link JavaClass} interface.
    */
   public boolean isInterface();

   /**
    * Return true if this {@link JavaType} represents a {@link JavaAnnotation}
    */
   public boolean isAnnotation();

   /**
    * Return the enclosing {@link JavaType} type in which this class is defined. If this class is a top-level
    * type, and is not a nested type, this object will return itself.
    */
   public JavaType<?> getEnclosingType();
   
   /**
    * Return a list containing {@link JavaType} instances for each nested {@link Class} declaration found within
    * <code>this</code>. Any modification of returned {@link JavaType} instances will result in modification of
    * the contents contained by <code>this</code> the parent instance.
    */
   @Deprecated
   public List<? extends JavaType<?>> getNestedClasses();

}