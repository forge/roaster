/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Member;


/**
 * Represents a {@link JavaSource} member. (I.e.: a {@link FieldSource} or {@link MethodSource})
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MemberSource<O extends JavaSource<O>, T> extends Member<O>, AnnotationTargetSource<O, T>,
         VisibilityScopedSource<T>, NamedSource<T>, JavaDocCapableSource<T>
{

   public T setFinal(boolean finl);

   public T setStatic(boolean statc);
}