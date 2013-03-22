/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface InterfaceCapable<T extends JavaSource<T>>
{
   List<String> getInterfaces();

   T addInterface(String type);

   T addInterface(Class<?> type);

   T addInterface(JavaInterface type);

   boolean hasInterface(String type);

   boolean hasInterface(Class<?> type);

   boolean hasInterface(JavaInterface type);

   T removeInterface(String type);

   T removeInterface(Class<?> type);

   T removeInterface(JavaInterface type);
}
