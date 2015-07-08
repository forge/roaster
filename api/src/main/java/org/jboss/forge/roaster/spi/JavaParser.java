/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.spi;

import java.io.InputStream;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface JavaParser
{
   /**
    * Create a new empty {@link JavaSource} instance.
    * 
    * @param type desired source type
    * @return instance of {@code T}, {@code null} if the {@link JavaSource} type is not supported by this
    *         {@link JavaParser}.
    */
   <T extends JavaSource<?>> T create(final Class<T> type);

   /**
    * Read the given {@link InputStream} and parse the data into a new {@link JavaUnit} instance.
    * 
    * @param data to parse
    * @return {@link JavaUnit}, {@code null} if the data format is not recognized by this {@link JavaParser}.
    */
   JavaUnit parseUnit(final InputStream data);

   /**
    * Read the given {@link InputStream} and parse the data into a new {@link JavaType} instance.
    * 
    * @param data to parse
    * @return {@link JavaType}, {@code null} if the data format is not recognized by this {@link JavaParser}.
    * @deprecated {@link #parseUnit(InputStream)} should be used instead, as the JLS allows several types to be defined
    *             in a single file
    */
   @Deprecated
   JavaType<?> parse(final InputStream data);
}