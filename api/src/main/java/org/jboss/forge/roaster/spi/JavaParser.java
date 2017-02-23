/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.spi;

import java.util.List;

import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Problem;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Roaster SPI for parser implementations
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
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
    * Read the given {@link String} and parse the data into a new {@link JavaUnit} instance.
    * 
    * @param data to parse
    * @return {@link JavaUnit}, {@code null} if the data format is not recognized by this {@link JavaParser}.
    */
   JavaUnit parseUnit(final String data);

   /**
    * Checks if the code is valid
    * 
    * @param code
    * @throws ParserException if it's not
    */
   List<Problem> validateSnippet(String code) throws ParserException;
}