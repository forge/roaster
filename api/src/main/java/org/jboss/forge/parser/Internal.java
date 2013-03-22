/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser;

/**
 * Represents an object that stores implementation-specific data. This data must be accessible to other objects sharing
 * the implementation, but should not be referenced by end-users.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Internal
{
   /**
    * Returns the implementation-specific Object representing <code>this</code>. <b>Do not call this method</b> unless
    * you are willing to risk breaking backwards compatibility if future versions do not use the same internal object
    * implementations.
    */
   Object getInternal();
}
