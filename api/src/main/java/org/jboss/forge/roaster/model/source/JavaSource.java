/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;

/**
 * Represents a Java type in source form.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaSource<T extends JavaSource<T>> extends JavaType<T>,
         PackagedSource<T>,
         Importer<T>,
         NamedSource<T>,
         VisibilityScopedSource<T>,
         AnnotationTargetSource<T, T>,
         JavaDocCapableSource<T>
{
   @Override
   /**
    * Returnes the {@link JavaSource} enclosing this type
    */
   public JavaSource<?> getEnclosingType();

   /**
    * Return a list containing {@link JavaSource} instances for each nested {@link Class} declaration found within
    * <code>this</code>. Any modification of returned {@link JavaSource} instances will result in modification of the
    * contents contained by <code>this</code> the parent instance.
    */
   @Override
   @Deprecated
   public List<JavaSource<?>> getNestedClasses();

    /**
     * Ensures the type passed as argument is included in the list of imports for this java source.
     * The method will also recursively import parameter types. This method is idempotent: if a type has
     * already been imported, no further action will be required.
     * The method returns the name that can be used inside the code to reference the type. The name will be simple
     * if no ambiguity exists with other types having the same (local) name, or fully qualified otherwise.
     * @param type  The {@link Type} to be imported.
     * @return      The name (simple or fully qualified) that should be used to reference the imported type in the code
     * @seeAlso     org.jboss.forge.roaster.model.Type
     */
   public String ensureImports(Type<?> type);

}