/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.Internal;
import org.jboss.forge.parser.Origin;

public interface EnumConstant extends Internal, Origin<JavaEnum>
{
   /**
    * Represents the anonymous subclass "body" of an enum constant.
    */
   public interface Body extends JavaSource<Body>, FieldHolder<Body>, MethodHolder<Body> {
   }

   /**
    * Get the constructor arguments of this enum constant.
    */
   List<String> getConstructorArguments();

   /**
    * Set the constructor arguments for this enum constant.
    */
   EnumConstant<O> setConstructorArguments(String... literalArguments);

   /**
    * Get the {@link Body} of this enum constant.
    */
   Body getBody();

   /**
    * Remove the {@link Body} of this enum constant.
    */
   EnumConstant setName(String name);
}
