/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Extendable;
import org.jboss.forge.roaster.model.JavaType;

/**
 * Represents a {@link JavaSource} that can extend other types (Java inheritance and interfaces).
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ExtendableSource<O extends JavaType<O>> extends Extendable<O>
{
   /**
    * Set this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public O setSuperType(JavaType<?> type);

   /**
    * Set this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public O setSuperType(Class<?> type);

   /**
    * Set this type's super class and import their methods.
    * 
    * @see #setSuperType(String)
    */
   public O setSuperType(Class<?> type, boolean importAbstractMethods);

   /**
    * Set this type's super class.
    * <p>
    * <strong>For example:</strong><br/>
    * In the case of " <code>public class Foo extends Bar {}</code>" - <code>Foo</code> is the base type, and
    * <code>Bar</code> is the super class.)
    * <p>
    * Attempt to add an import statement to this object's {@link O} if required. (Note that the given className must be
    * fully-qualified in order to properly import required classes)
    */
   public O setSuperType(String type);
}