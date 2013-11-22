package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.VisibilityScoped;
import org.jboss.forge.parser.java.Visibility;

/**
 * Represents a Java source element that has a certain visibility scope.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface VisibilityScopedSource<T> extends VisibilityScoped
{
   T setPackagePrivate();

   T setPublic();

   T setPrivate();

   T setProtected();

   T setVisibility(Visibility scope);
}