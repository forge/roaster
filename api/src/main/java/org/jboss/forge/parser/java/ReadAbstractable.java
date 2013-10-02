/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

/**
 * Represents a Java element that may be declared {@code abstract}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ReadAbstractable<T>
{
   public abstract boolean isAbstract();

   /**
    * Represents a Java source element that may be declared {@code abstract}.
    * 
    * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
    * 
    */
   public interface Abstractable<T> extends ReadAbstractable<T>
   {
      public abstract T setAbstract(boolean abstrct);
   }
}
