/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;

/**
 * Represents a {@link ReadJavaSource} that may implement one or more interfaces.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadInterfaceCapable
{
   List<String> getInterfaces();

   boolean hasInterface(String type);

   boolean hasInterface(Class<?> type);

   boolean hasInterface(ReadJavaInterface<?> type);

   /**
    * Represents a {@link JavaSource} that may implement one or more interfaces.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface InterfaceCapable<T extends JavaSource<T>> extends ReadInterfaceCapable
   {
      T addInterface(String type);

      T addInterface(Class<?> type);

      T addInterface(ReadJavaInterface<?> type);

      T removeInterface(String type);

      T removeInterface(Class<?> type);

      T removeInterface(ReadJavaInterface<?> type);
   }
}