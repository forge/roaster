/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster;

/**
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
    * @param message
    * @param sourceStart
    * @param sourceEnd
    * @param sourceLineNumber
    */
   public Problem(String message, int sourceStart, int sourceEnd, int sourceLineNumber)
   {
      super();
      this.message = message;
      this.sourceStart = sourceStart;
      this.sourceEnd = sourceEnd;
      this.sourceLineNumber = sourceLineNumber;
   }

   /**
    * @return the message
    */
   public String getMessage()
   {
      return message;
   }

   /**
    * @return the sourceStart
    */
   public int getSourceStart()
   {
      return sourceStart;
   }

   /**
    * @return the sourceEnd
    */
   public int getSourceEnd()
   {
      return sourceEnd;
   }

   /**
    * @return the sourceLineNumber
    */
   public int getSourceLineNumber()
   {
      return sourceLineNumber;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((message == null) ? 0 : message.hashCode());
      result = prime * result + sourceEnd;
      result = prime * result + sourceLineNumber;
      result = prime * result + sourceStart;
      return result;
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
      return "Problem [message=" + message + ", sourceStart=" + sourceStart + ", sourceEnd=" + sourceEnd
               + ", sourceLineNumber=" + sourceLineNumber + "]";
   }
}
