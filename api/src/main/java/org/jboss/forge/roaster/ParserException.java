/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ParserException extends RuntimeException
{
   private static final long serialVersionUID = 642493448571856848L;

   public ParserException()
   {
   }

   public ParserException(final String message)
   {
      super(message);
   }

   public ParserException(final Throwable e)
   {
      super(e);
   }

   public ParserException(final String message, final Throwable e)
   {
      super(message, e);
   }

}
