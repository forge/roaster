package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.Origin;

/**
 * Represents a initializer of a {@link JavaClass}, {@link JavaRecord} or {@link JavaEnum}.
 */
public interface Initializer<O extends JavaType<O>, T extends Initializer<O, T>> extends StaticCapable, Internal, Origin<O> {

    /**
     * Get the inner body of this {@link Initializer}
     */
    String getBody();

}
