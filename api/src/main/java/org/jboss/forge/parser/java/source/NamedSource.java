package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Named;

/**
 * Represents a named Java source element.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface NamedSource<T> extends Named
{

   /**
    * Set the simple-name of this {@link T} instance.
    * 
    * @see #getName()
    */
   public T setName(String name);
}