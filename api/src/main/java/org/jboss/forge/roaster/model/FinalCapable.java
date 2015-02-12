/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

/**
 * Represents a Java element that can be modified
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface FinalCapable
{
   /**
    * @return if this element has the <b>final</b> keyword
    */
   boolean isFinal();
}
