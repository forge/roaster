/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Mutable;

/**
 * Represents a Java element that supports the <b>final</b> keyword.
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface MutableSource<T> extends Mutable
{
   /**
    * Sets the <b>final</b> keyword in this element.
    * 
    * @param finl if this element should be set to final
    * @return the generic element this interface is bound to
    */
   T setFinal(boolean finl);
}
