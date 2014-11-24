/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.spi;

import java.util.Properties;

import org.jboss.forge.roaster.Roaster;

/**
 * Allows attaching a custom source formatter.
 * 
 * This interface is meant to be consumed by {@link Roaster} only
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface FormatterProvider
{
   /**
    * Format the provided source code
    * 
    * @param source a Java source file content
    * @return the formatted version of the source parameter
    */
   String format(String source);

   /**
    * Format the source code, given the required {@link Properties}
    * 
    * @param properties implementation specific formatting options
    * @param source a Java source file content
    * @return the formatted version of the source parameter
    */
   String format(Properties properties, String source);
}
