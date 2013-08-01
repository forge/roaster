/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import org.jboss.forge.parser.Internal;
import org.jboss.forge.parser.Origin;

//TODO implement MethodHolder
public interface EnumConstant extends Internal, Origin<JavaEnum>
{
   /**
    * Get this enum constant name.
    */
   String getName();
   
   /**
    * Set this enum constant name.
    */
   EnumConstant setName(String name);
}
