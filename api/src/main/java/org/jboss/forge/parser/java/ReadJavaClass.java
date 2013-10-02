/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.JavaParser;

/**
 * Represents a Java {@link Class} source file as an in-memory modifiable element. See {@link JavaParser}
 * for various options in generating {@link ReadJavaClass} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ReadJavaClass<O extends ReadJavaClass<O>> extends
         ReadJavaSource<O>,
         ReadInterfaceCapable,
         ReadFieldHolder<O>,
         ReadMethodHolder<O>,
         ReadGenericCapable,
         ReadExtendable<O>,
         ReadAbstractable<O>
{
   public interface JavaClass extends ReadJavaClass<JavaClass>,
            JavaSource<JavaClass>,
            InterfaceCapable<JavaClass>,
            FieldHolder<JavaClass>,
            MethodHolder<JavaClass>,
            GenericCapable<JavaClass>,
            Extendable<JavaClass>,
            Abstractable<JavaClass>
   {
   }
}