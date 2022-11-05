/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents a {@link JavaType} that may implement one or more interfaces.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface InterfaceCapable
{
   List<String> getInterfaces();

   boolean hasInterface(String type);

   default boolean hasInterface(Class<?> type) {
      return hasInterface(type.getCanonicalName());
   }

   default boolean hasInterface(JavaInterface<?> type) {
      return hasInterface(type.getQualifiedName());
   }
}