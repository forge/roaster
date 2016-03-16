/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.grammar.java;

import org.jboss.forge.test.roaster.model.common.MockEnumType;

public @interface MockJavaAnnotationType
{
   public @interface MockNestedJavaAnnotationType
   {
      int value();

      Class<? extends CharSequence> charSequenceType() default String.class;

      @Deprecated
      MockEnumType metasyntacticVariable() default FOO;

      Class<? extends Number>[] numberTypes() default {};

      MockEnumType[] metasyntacticVariables() default {};
   }

   MockNestedJavaAnnotationType value() default @MockNestedJavaAnnotationType(-1);
}