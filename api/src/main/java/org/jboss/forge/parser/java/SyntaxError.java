/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java;

/**
 * Describes a syntax problem in a {@link ReadJavaClass}.
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
}
