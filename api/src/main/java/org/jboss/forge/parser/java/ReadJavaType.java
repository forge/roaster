/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadJavaType<T extends ReadJavaSource<T>> extends ReadJavaSource<T>,
         ReadInterfaceCapable,
         ReadFieldHolder<T>,
         ReadMethodHolder<T>,
         ReadGenericCapable
{
   public interface JavaType<T extends JavaSource<T>> extends JavaSource<T>,
            InterfaceCapable<T>,
            FieldHolder<T>,
            MethodHolder<T>,
            GenericCapable<T>
   {
   }
}