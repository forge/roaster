/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.EnumConstant;

/**
 * Represents one of the constant members of a {@link JavaEnumSource}.
 */
public interface EnumConstantSource extends EnumConstant<JavaEnumSource>, AnnotationTargetSource<JavaEnumSource, EnumConstantSource>,
         NamedSource<EnumConstantSource>
{
   /**
    * Represents the anonymous subclass "body" of an {@link EnumConstantSource}.
    */
   public interface Body extends EnumConstant.ReadBody<Body>, JavaSource<Body>, FieldHolderSource<Body>,
            MethodHolderSource<Body>
   {
   }

   /**
    * Set the constructor arguments for this enum constant.
    */
   EnumConstantSource setConstructorArguments(String... literalArguments);

   @Override
   EnumConstantSource.Body getBody();

   /**
    * Remove the {@link Body} of this enum constant.
    */
   EnumConstantSource removeBody();
}