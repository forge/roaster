package org.jboss.forge.grammar.java;

import org.jboss.forge.test.grammar.java.common.MockAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation.MockNestedAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation;

import static org.jboss.forge.test.grammar.java.common.MockEnum.FOO;

public class MockAnnotatedMethod
{
   @Deprecated
   @SuppressWarnings("deprecation")
   @SuppressWarnings(value = "unchecked")
   @MockAnnotation(FOO)
   @MockNestingAnnotation(@MockNestedAnnotation)
   @MockContainerAnnotation({
      @MockContainedAnnotation(0)
   })
   public MockAnnotatedMethod()
   {
   }

   public void testme()
   {
   }
}
