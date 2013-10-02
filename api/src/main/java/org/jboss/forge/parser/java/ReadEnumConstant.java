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
import org.jboss.forge.parser.java.ReadFieldHolder.FieldHolder;
import org.jboss.forge.parser.java.ReadJavaEnum.JavaEnum;
import org.jboss.forge.parser.java.ReadJavaSource.JavaSource;
import org.jboss.forge.parser.java.ReadMethodHolder.MethodHolder;

public interface ReadEnumConstant<O extends ReadJavaEnum<O>> extends Internal, Origin<O>,
         ReadAnnotationTarget<O>, ReadNamed
{
   /**
    * Represents the anonymous subclass "body" of an enum constant.
    */
   public interface ReadBody<O extends ReadBody<O>> extends ReadJavaSource<O>, ReadFieldHolder<O>, ReadMethodHolder<O>
   {
   }

   /**
    * Get the constructor arguments of this enum constant.
    */
   List<String> getConstructorArguments();

   /**
    * Get the {@link Body} of this enum constant.
    */
   ReadBody<?> getBody();

   public interface EnumConstant extends ReadEnumConstant<JavaEnum>, AnnotationTarget<JavaEnum, EnumConstant>,
            Named<EnumConstant>
   {
      public interface Body extends ReadBody<Body>, JavaSource<Body>, FieldHolder<Body>, MethodHolder<Body>
      {
      }
      
      /**
       * Set the constructor arguments for this enum constant.
       */
      EnumConstant setConstructorArguments(String... literalArguments);

      Body getBody();

      /**
       * Remove the {@link Body} of this enum constant.
       */
      EnumConstant removeBody();
   }
}
