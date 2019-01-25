/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.common;

/**
 * 
 */
public @interface MockNestingAnnotation
{
   @interface MockNestedAnnotation
   {
      // empty for testing
   }

   MockNestedAnnotation value();
}