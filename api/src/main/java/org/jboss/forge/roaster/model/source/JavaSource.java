/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

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
         AnnotationTargetSource<T, T>,
         JavaDocCapableSource<T>,
         LocationCapable
{
   /**
    * Returns the {@link JavaSource} enclosing this type
    */
   @Override
   JavaSource<?> getEnclosingType();
}