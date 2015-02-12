/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * Represents a Java element that may support the <b>static</b> keyword
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface StaticCapable
{
   /**
    * @return if the element is static
    */
   boolean isStatic();
}
