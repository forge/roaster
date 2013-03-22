/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.Origin;

/**
 * Represents a {@link JavaClass} member. (E.g.: a {@link Field} or {@link Method})
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Member<O extends JavaSource<O>, T> extends VisibilityScoped<T>, AnnotationTarget<O, T>, Origin<O>
{
   public String getName();

   public boolean isFinal();

   public T setFinal(boolean finl);

   public boolean isStatic();

   public T setStatic(boolean statc);
}
