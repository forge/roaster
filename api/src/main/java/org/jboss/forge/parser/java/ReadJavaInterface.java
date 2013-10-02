/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import org.jboss.forge.parser.JavaParser;

/**
 * Represents a Java {@code interface} type. See {@link JavaParser} for various options in generating
 * {@link ReadJavaInterface} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ReadJavaInterface<O extends ReadJavaInterface<O>> extends
         ReadJavaSource<O>,
         ReadInterfaceCapable,
         ReadFieldHolder<O>,
         ReadMethodHolder<O>,
         ReadGenericCapable
{
   /**
    * Represents a Java {@code interface} source file as an in-memory modifiable element. See {@link JavaParser} for
    * various options in generating {@link JavaInterface} instances.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    */
   public interface JavaInterface extends ReadJavaInterface<JavaInterface>,
            JavaSource<JavaInterface>,
            FieldHolder<JavaInterface>,
            MethodHolder<JavaInterface>,
            GenericCapable<JavaInterface>,
            InterfaceCapable<JavaInterface>
   {
   }

}
