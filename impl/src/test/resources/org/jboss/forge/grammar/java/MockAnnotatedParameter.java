package org.jboss.forge.grammar.java;

import org.jboss.forge.test.grammar.java.common.MockAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation.MockNestedAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation;

import static org.jboss.forge.test.grammar.java.common.MockEnum.FOO;

public class MockAnnotatedParameter
{
   public void mockAnnotatedMethod(
            @Deprecated
            @SuppressWarnings("deprecation")
            @SuppressWarnings(value = "unchecked")
            @MockAnnotation(FOO)
            @MockNestingAnnotation(@MockNestedAnnotation)
            String param1, int param2)
   {
   }
}
