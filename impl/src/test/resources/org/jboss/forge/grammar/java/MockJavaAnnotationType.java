package org.jboss.forge.grammar.java;

import org.jboss.forge.test.parser.java.common.MockEnumType;

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