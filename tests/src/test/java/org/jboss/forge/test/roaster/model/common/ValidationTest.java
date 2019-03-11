/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.jboss.forge.roaster.Problem;
import org.jboss.forge.roaster.Roaster;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ValidationTest
{
   @Test
   public void testSnippetIsValid()
   {
      String snippet = "System.out.println();";
      List<Problem> problems = Roaster.validateSnippet(snippet);
      assertThat(problems).isEmpty();
   }

   @Test
   public void testSnippetIsInvalid()
   {
      String snippet = "System.out.println()";
      List<Problem> problems = Roaster.validateSnippet(snippet);
      assertThat(problems).hasSize(1)
               .contains(new Problem("Syntax error, insert \";\" to complete BlockStatements", 19, 19, 1));
   }

   @Test
   public void testJavaClassIsValid()
   {
      String snippet = "public class MyClass {}";
      List<Problem> problems = Roaster.validateSnippet(snippet);
      assertThat(problems).isEmpty();
   }

   @Test
   public void testJavaClassIsInvalid()
   {
      String snippet = "public class MyClass {";
      List<Problem> problems = Roaster.validateSnippet(snippet);
      assertThat(problems).hasSize(1)
               .contains(new Problem("Syntax error, insert \"}\" to complete ClassBody", 21, 21, 1));
   }

   @Test
   public void testJavaClassHasInvalidAttribute()
   {
      String snippet = "public class MyClass { public String abstract;}";
      List<Problem> problems = Roaster.validateSnippet(snippet);
      assertThat(problems).hasSize(1)
               .contains(new Problem("Syntax error on token \"abstract\", invalid VariableDeclarator", 37, 44, 1));
   }

   @Test
   public void testJavaClassHasInvalidAttributeOnMultipleLines()
   {
      String snippet = "public class MyClass {\n public String abstract;}";
      List<Problem> problems = Roaster.validateSnippet(snippet);
      assertThat(problems).hasSize(1)
               .contains(new Problem("Syntax error on token \"abstract\", invalid VariableDeclarator", 38, 45, 2));
   }

}
