/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ParserException extends RuntimeException
{
   private static final long serialVersionUID = 642493448571856848L;

   private final List<Problem> problems;

   public ParserException()
   {
      this.problems = Collections.emptyList();
   }

   public ParserException(final String message)
   {
      super(message);
      this.problems = Collections.emptyList();
   }

   public ParserException(final Throwable e)
   {
      super(e);
      this.problems = Collections.emptyList();
   }

   public ParserException(final String message, final Throwable e)
   {
      super(message, e);
      this.problems = Collections.emptyList();
   }

   public ParserException(String message, List<Problem> problems)
   {
      super(message);
      this.problems = problems;
   }

   public ParserException(List<Problem> problems)
   {
      super(getProblemsMessage(problems));
      this.problems = problems;
   }

   /**
    * @return the problems
    */
   public List<Problem> getProblems()
   {
      return problems;
   }

   private static String getProblemsMessage(List<Problem> problems)
   {
      StringBuilder sb = new StringBuilder();
      if (!problems.isEmpty())
      {
         for (Problem problem : problems)
         {
            sb.append("- ").append(problem.getMessage()).append('\n');
         }
         return sb.toString();
      }
      return sb.toString();
   }

}
