/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.JavaParser;

/**
 * Represents a Java {@code class} type. See {@link JavaParser} for various options in generating {@link JavaClass}
 * instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaClass<O extends JavaClass<O>> extends
         JavaType<O>,
         InterfaceCapable,
         FieldHolder<O>,
         MethodHolder<O>,
         GenericCapable<O>,
         Extendable<O>,
         Abstractable<O>
{
}