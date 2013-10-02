/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.impl;

import org.eclipse.jdt.core.compiler.IProblem;
import org.jboss.forge.parser.Internal;
import org.jboss.forge.parser.java.JavaType;
import org.jboss.forge.parser.java.SyntaxError;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class SyntaxErrorImpl implements SyntaxError, Internal
{
   private final JavaType<?> parent;
   private final IProblem problem;

   public SyntaxErrorImpl(final JavaType<?> parent, final Object internal)
   {
      this.parent = parent;
      this.problem = (IProblem) internal;
   }

   @Override
   public String getDescription()
   {
      int line = problem.getSourceLineNumber();
      int begin = problem.getSourceStart();
      int end = problem.getSourceEnd();

      String snippit = parent.toString().substring(begin, end);

      String message = "Line " + line + " near <" + begin + "," + end + ">: \"" + snippit + "\" - "
               + problem.getMessage();

      return message;
   }

   @Override
   public Object getInternal()
   {
      return problem;
   }

   @Override
   public String toString()
   {
      return getDescription();
   }

}
