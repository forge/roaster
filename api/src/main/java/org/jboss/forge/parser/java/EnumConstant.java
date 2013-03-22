/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import org.jboss.forge.parser.Internal;
import org.jboss.forge.parser.Origin;

public interface EnumConstant<O extends JavaSource<O>> extends Internal, Origin<O>
{
   /**
    * Get this enum constant name.
    */
   String getName();
   
   /**
    * Set this enum constant name.
    */
   EnumConstant<O> setName(String name);
}
