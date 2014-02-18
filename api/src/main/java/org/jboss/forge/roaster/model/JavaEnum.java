/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Roaster;

/**
 * Represents a Java {@code enum} type. See {@link Roaster} for various options in generating {@link JavaEnum}
 * instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaEnum<O extends JavaEnum<O>> extends JavaType<O>, PropertyHolder<O>
{
   /**
    * Return the {@link EnumConstant} with the given name, or return null if no such constant exists.
    * 
    * @param name
    * @return
    */
   EnumConstant<O> getEnumConstant(String name);

   /**
    * Return all declared {@link EnumConstant} types for this {@link JavaEnum}
    */
   List<? extends EnumConstant<O>> getEnumConstants();
}