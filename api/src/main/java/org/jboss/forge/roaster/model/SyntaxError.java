/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model;

/**
 * Describes a syntax problem in a {@link JavaClass}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface SyntaxError
{
   /**
    * Get a textual description of the type of problem encountered.
    */
   public String getDescription();

   /**
    * Gets the 1 based line number for the syntax error whenever it's possible to calculate it, or < 0 when the line is
    * unknown.
    */
   public int getLine();

   /**
    * Get the 0 based column number for the error start whenever it's possible to calculate it, or < 0 when the column
    * is unknown.
    */
   public int getColumn();

   /**
    * True if the syntax error is an error, false in any other case. (see isWarning())
    * 
    * @return
    */
   public boolean isError();

   /**
    * True if the syntax error is a warning, false in any other case. (see isError())
    * 
    * @return
    */
   public boolean isWarning();

}
