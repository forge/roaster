/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.InterfaceCapable;
import org.jboss.forge.roaster.model.JavaInterface;

/**
 * Represents a {@link JavaSource} that may implement one or more interfaces.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface InterfaceCapableSource<T extends JavaSource<T>> extends InterfaceCapable
{
   T addInterface(String type);

   T addInterface(Class<?> type);

   /**
    * Adds the interface and imports the methods
    */
   T addInterface(Class<?> type, boolean importAbstractMethods);

   T addInterface(JavaInterface<?> type);

   T removeInterface(String type);

   T removeInterface(Class<?> type);

   T removeInterface(JavaInterface<?> type);
}