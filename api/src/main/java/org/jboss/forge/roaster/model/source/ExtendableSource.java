/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Extendable;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.util.Methods;

/**
 * Represents a {@link JavaSource} that can extend other types (Java inheritance and interfaces).
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ExtendableSource<O extends JavaType<O>> extends Extendable<O>
{
   /**
    * Set this type's super class. If the provided type is {@code null} the previous super class is removed.
    * 
    * @see #setSuperType(String)
    */
   O setSuperType(JavaType<?> type);

   /**
    * Set this type's super class. If the provided type is {@code null} the previous super class is removed.
    * 
    * @see #setSuperType(String)
    */
   O setSuperType(Class<?> type);

   /**
    * Set this type's super class and import their abstract methods, if any.
    * 
    * @see #setSuperType(String)
    * @see Methods#implementAbstractMethods(Class, MethodHolderSource)
    */
   O extendSuperType(Class<?> type);

   /**
    * Set this type's super class and import their abstract methods, if any.
    * 
    * @see #setSuperType(String)
    * @see Methods#implementAbstractMethods(Class, MethodHolderSource)
    */
   O extendSuperType(JavaClass<?> type);

   /**
    * Set this type's super class. If the provided type is {@code null} the previous super class is removed.
    * <p>
    * <strong>For example:</strong><br/>
    * In the case of " <code>public class Foo extends Bar {}</code>" - <code>Foo</code> is the base type, and
    * <code>Bar</code> is the super class.)
    * <p>
    * Attempt to add an import statement to this object's {@link O} if required. (Note that the given className must be
    * fully-qualified in order to properly import required classes)
    */
   O setSuperType(String type);
}