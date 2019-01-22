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
 * Main interface for Roaster parser implementations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaParser
{
   /**
    * Create a new empty {@link JavaSource} instance.
    * @param <T> the java type
    * @param type desired source type for the java source
    * @return either a instance of {@code T} or {@code null} if the {@link JavaSource} type is not supported by this
    *         {@link JavaParser}.
    */
   <T extends JavaSource<?>> T create(final Class<T> type);

   /**
    * Read the given source and parse the data into a new {@link JavaUnit} instance.
    * 
    * @param data the source to parse
    * @return a {@link JavaUnit} or {@code null} if the data format is not recognised by this {@link JavaParser}
    * @throws ParserException if the source coudn't be parsed
    */
   JavaUnit parseUnit(final String data);

   /**
    * Checks which problems occur while parsing the provided source code.
    * 
    * @param code the code to parse
    * @return a list of problems which occurred (maybe empty but not {@code null})
    */
   List<Problem> validateSnippet(String code);
}