/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster;

import java.util.Objects;

/**
 * During the parsing of code, problems can occur which can be wrapped inside a object with this class.
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class Problem
{
   private final String message;
   private final int sourceStart;
   private final int sourceEnd;
   private final int sourceLineNumber;

   /**
    * Constructs a new <code>Problem</code> with the given attributes.
    * 
    * @param message the message of this problem in a human readable form
    * @param sourceStart the start position of the problem
    * @param sourceEnd the end position of the problem
    * @param sourceLineNumber the source line number of the start of the problem
    */
   public Problem(String message, int sourceStart, int sourceEnd, int sourceLineNumber)
   {
      this.message = message;
      this.sourceStart = sourceStart;
      this.sourceEnd = sourceEnd;
      this.sourceLineNumber = sourceLineNumber;
   }

   /**
    * Returns the message of this problem.
    * 
    * @return the message
    */
   public String getMessage()
   {
      return message;
   }

   /**
    * Returns the start position of this problem or -1 if unknown.
    * 
    * @return the start position
    */
   public int getSourceStart()
   {
      return sourceStart;
   }

   /**
    * Returns the end position of this problem or -1 if unknown.
    * 
    * @return the end position 
    */
   public int getSourceEnd()
   {
      return sourceEnd;
   }

   /**
    * Returns the source line number of the start of the problem.
    * 
    * @return the source line number
    */
   public int getSourceLineNumber()
   {
      return sourceLineNumber;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(message, sourceEnd, sourceLineNumber, sourceStart);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Problem other = (Problem) obj;
      if (message == null)
      {
         if (other.message != null)
            return false;
      }
      else if (!message.equals(other.message))
         return false;
      if (sourceEnd != other.sourceEnd)
         return false;
      if (sourceLineNumber != other.sourceLineNumber)
         return false;
      if (sourceStart != other.sourceStart)
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "Problem: " + message;
   }
}