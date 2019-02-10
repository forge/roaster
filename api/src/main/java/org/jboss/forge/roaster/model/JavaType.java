/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

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
         JavaDocCapable<T>
{
   /**
    * Return the canonical name of this {@link T} instance. This is equivalent to calling
    * {@link Class#getCanonicalName()}
    */
   String getCanonicalName();

   /**
    * Get the qualified-name of this {@link T} instance, where the qualified-name contains both the Java package and
    * simple class name of the type represented by this {@link T} instance.
    * <p/>
    * <strong>For example</strong>, calling: {@link #getQualifiedName()} is equivalent to calling "{@link #getPackage()}
    * + "." + {@link #getName()}", which in turn is equivalent to calling: {@link Class#getName()}
    */
   String getQualifiedName();

   /**
    * Get a list of all {@link SyntaxError}s detected in the current {@link T}. Note that when errors are present, the
    * class may still be modified, but changes may not be completely accurate.
    */
   List<SyntaxError> getSyntaxErrors();

   /**
    * Return whether or not this {@link T} currently has any {@link SyntaxError} s.
    */
   boolean hasSyntaxErrors();

   /**
    * Return <code>true</code> if this {@link JavaType} represents a {@link JavaClass}
    */
   boolean isClass();

   /**
    * Return <code>true</code> if this {@link JavaType} represents a {@link JavaEnum}
    */
   boolean isEnum();

   /**
    * Return <code>true</code> if this {@link JavaType} represents a {@link JavaClass} interface.
    */
   boolean isInterface();

   /**
    * Return <code>true</code> if this {@link JavaType} represents a {@link JavaAnnotation}
    */
   boolean isAnnotation();

   /**
    * Return the enclosing {@link JavaType} type in which this class is defined. If this class is a top-level type, and
    * is not a nested type, this object will return itself.
    */
   JavaType<?> getEnclosingType();

   /**
    * Return the generated code without any formatting.
    */
   String toUnformattedString();

}