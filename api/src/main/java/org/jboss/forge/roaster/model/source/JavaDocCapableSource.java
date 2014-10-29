/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.JavaDoc;
import org.jboss.forge.roaster.model.JavaDocCapable;

/**
 * Represents a {@link JavaSource} element that can hold {@link JavaDoc}
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface JavaDocCapableSource<O> extends JavaDocCapable<O>
{
   @Override
   JavaDocSource<O> getJavaDoc();

   /**
    * Remove the associated {@link JavaDoc}
    */
   O removeJavaDoc();
}
