/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.SyntaxError;

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

   @Override
   public int getLine()
   {
      return problem.getSourceLineNumber();
   }

   @Override
   public int getColumn()
   {
      int position = problem.getSourceStart();
      if (position >= 0 && parent != null && (parent.getInternal() instanceof CompilationUnit))
      {
         return ((CompilationUnit) parent.getInternal()).getColumnNumber(position);
      }
      return -1;
   }

   @Override
   public boolean isError()
   {
      return problem.isError();
   }

   @Override
   public boolean isWarning()
   {
      return problem.isWarning();
   }
}
