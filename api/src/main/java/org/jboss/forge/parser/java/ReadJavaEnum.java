/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadEnumConstant.EnumConstant;

/**
 * Represents a Java {@link Enum} source file as an in-memory modifiable element. See {@link JavaParser} for various
 * options in generating {@link ReadJavaEnum} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ReadJavaEnum<O extends ReadJavaEnum<O>> extends ReadJavaSource<O>, ReadInterfaceCapable,
         ReadFieldHolder<O>,
         ReadMethodHolder<O>
{
   /**
    * Return the {@link ReadEnumConstant} with the given name, or return null if no such constant exists.
    * 
    * @param name
    * @return
    */
   ReadEnumConstant<O> getEnumConstant(String name);

   /**
    * Return all declared {@link ReadEnumConstant} types for this {@link ReadJavaEnum}
    */
   List<? extends ReadEnumConstant<O>> getEnumConstants();

   public interface JavaEnum extends ReadJavaEnum<JavaEnum>, JavaSource<JavaEnum>, InterfaceCapable<JavaEnum>,
            FieldHolder<JavaEnum>,
            MethodHolder<JavaEnum>
   {
      /**
       * Return the {@link ReadEnumConstant} with the given name, or return null if no such constant exists.
       * 
       * @param name
       * @return
       */
      EnumConstant getEnumConstant(String name);

      /**
       * Return all declared {@link ReadEnumConstant} types for this {@link ReadJavaEnum}
       */
      List<EnumConstant> getEnumConstants();

      /**
       * Add a new {@link ReadEnumConstant}
       */
      EnumConstant addEnumConstant();

      /**
       * Add a new {@link ReadEnumConstant} using the given declaration.
       */
      EnumConstant addEnumConstant(String declaration);

   }
}