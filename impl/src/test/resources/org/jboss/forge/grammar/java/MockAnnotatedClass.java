package org.jboss.forge.grammar.java;

import org.jboss.forge.test.grammar.java.common.MockAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation.MockNestedAnnotation;
import org.jboss.forge.test.grammar.java.common.MockNestingAnnotation;

import static org.jboss.forge.test.grammar.java.common.MockEnum.FOO;

@Deprecated
@SuppressWarnings("deprecation")
@SuppressWarnings(value = "unchecked")
@MockAnnotation(FOO)
@MockNestingAnnotation(@MockNestedAnnotation)
public class MockAnnotatedClass
{
   private String field;

   public MockAnnotatedClass()
   {
   }
}
