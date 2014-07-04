/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.grammar.java;

import org.jboss.forge.test.grammar.java.common.MockAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation.MockNestedAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation;

import static org.jboss.forge.test.grammar.java.common.MockEnum.FOO;

public class MockAnnotatedField
{
   @Deprecated
   @SuppressWarnings("deprecation")
   @SuppressWarnings(value = "unchecked")
   @MockAnnotation(FOO)
   @MockNestingAnnotation(@MockNestedAnnotation)
   private String field;
}
