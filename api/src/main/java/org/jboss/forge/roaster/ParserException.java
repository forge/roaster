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
 * {@code ParserException} is the exception which is thrown if a parsing process fails. The problems occurred
 * during the parsing can be accessed with {@link #getProblems()}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ParserException extends RuntimeException
{
   private static final long serialVersionUID = 642493448571856848L;

   private final List<Problem> problems;

   /**
    * Constructs a new {@code ParserException} with no problems, a {@code null} error message and a
    * {@code null} cause.
    */
   public ParserException()
   {
      this.problems = Collections.emptyList();
   }

   /**
    * Constructs a new {@code ParserException} with no problems, the given error message and a {@code null}
    * cause.
    * 
    * @param message the error message of this exception
    */
   public ParserException(final String message)
   {
      super(message);
      this.problems = Collections.emptyList();
   }

   /**
    * Constructs a new {@code ParserException} with no problems, a {@code null} error message and the given
    * cause.
    * 
    * @param cause the cause of this exception
    */
   public ParserException(final Throwable cause)
   {
      super(cause);
      this.problems = Collections.emptyList();
   }

   /**
    * Constructs a new {@code ParserException} with no problems, the given error message and the given cause.
    * 
    * @param message the error message of this exception
    * @param cause the cause of this exception
    */
   public ParserException(final String message, final Throwable cause)
   {
      super(message, cause);
      this.problems = Collections.emptyList();
   }

   /**
    * Constructs a new {@code ParserException} with the given problems, the given error message and a
    * {@code null} cause.
    * <p>
    * Note that the problems list instance is <b>not</b> copied.
    * </p>
    * 
    * @param message the error message of this exception
    * @param problems the problems of this exception
    */
   public ParserException(String message, List<Problem> problems)
   {
      super(message);
      this.problems = problems;
   }

   /**
    * Constructs a new {@code ParserException} with the given problems, a error message constructed from the
    * problems list and a {@code null} cause.
    * <p>
    * Note that the problems list instance is <b>not</b> copied.
    * </p>
    * 
    * @param problems the problems of this exception
    */
   public ParserException(List<Problem> problems)
   {
      super(getProblemsMessage(problems));
      this.problems = problems;
   }

   /**
    * Get the problems occurred during a parsing process. The returned list is maybe empty but never {@code null}.
    * 
    * @return a unmodifiable list of the problems occurred during a parsing process
    * @see Collections#unmodifiableList(List)
    */
   public List<Problem> getProblems()
   {
      return Collections.unmodifiableList(problems);
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