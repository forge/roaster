/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.JavaParser;

/**
 * Represents a Java {@link Enum} source file as an in-memory modifiable element. See {@link JavaParser} for various
 * options in generating {@link JavaEnum} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaEnum extends JavaSource<JavaEnum>, InterfaceCapable<JavaEnum>, FieldHolder<JavaEnum>,
         MethodHolder<JavaEnum>
{
   /**
    * Add a new {@link EnumConstant}
    */
   EnumConstant addEnumConstant();
   
   /**
    * Add a new {@link EnumConstant} using the given declaration.
    */
   EnumConstant addEnumConstant(String declaration);
   
   /**
    * Return the {@link EnumConstant} with the given name, or return null if no such constant exists.
    * @param name
    * @return
    */
   EnumConstant getEnumConstant(String name);

   /**
    * Return all declared {@link EnumConstant} types for this {@link JavaEnum}
    */
   List<EnumConstant> getEnumConstants();
}