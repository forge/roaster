/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Internal;

/**
 * Represents a {@link JavaDoc} tag, like the author tag below
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaDocTag extends Internal
{
   /**
    * The tag name
    */
   String getName();

   /**
    * The tag value
    */
   String getValue();
}
