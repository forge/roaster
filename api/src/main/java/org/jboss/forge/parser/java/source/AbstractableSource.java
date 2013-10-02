package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.Abstractable;

/**
 * Represents a Java source element that may be declared {@code abstract}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface AbstractableSource<T> extends Abstractable<T>
{
   public abstract T setAbstract(boolean abstrct);
}