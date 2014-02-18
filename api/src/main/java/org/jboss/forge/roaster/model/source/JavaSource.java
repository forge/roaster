/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.JavaType;

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
         AnnotationTargetSource<T, T>
{
   @Override
   public JavaSource<?> getEnclosingType();

   /**
    * Return a list containing {@link JavaSource} instances for each nested {@link Class} declaration found within
    * <code>this</code>. Any modification of returned {@link JavaSource} instances will result in modification of the
    * contents contained by <code>this</code> the parent instance.
    */
   @Override
   public List<JavaSource<?>> getNestedClasses();

}