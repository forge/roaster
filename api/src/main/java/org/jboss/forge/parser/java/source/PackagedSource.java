package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Packaged;

/**
 * Represents a {@link JavaSource} that may be declared as belonging to a particular Java {@code package}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface PackagedSource<T> extends Packaged<T>
{

   /**
    * Set this {@link T}' package.
    */
   public T setPackage(String name);

   /**
    * Set this {@link T} to be in the default package (removes any current package declaration.)
    */
   public T setDefaultPackage();

}