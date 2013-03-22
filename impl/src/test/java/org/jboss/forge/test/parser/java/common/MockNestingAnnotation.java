/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.parser.java.common;

/**
 * 
 */
public @interface MockNestingAnnotation
{
   public @interface MockNestedAnnotation {
   }

   MockNestedAnnotation value();
}
