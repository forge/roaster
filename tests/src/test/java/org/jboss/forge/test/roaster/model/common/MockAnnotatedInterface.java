
/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.common;

public interface MockAnnotatedInterface
{
   @MockAnnotation
   MockSuperType lookup(@MockAnnotation String name, @MockAnnotation boolean create);
}