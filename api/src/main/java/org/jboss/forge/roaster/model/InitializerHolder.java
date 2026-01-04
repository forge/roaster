package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents a {@link JavaType} that may define initializers.
 */
public interface InitializerHolder<O extends JavaType<O>> {

    /**
     * Get a {@link List} of all {@link Initializer}s declared by this {@link O} instance, if any; otherwise return an empty
     * {@link List}
     */
    List<? extends Initializer<O, ?>> getInitializers();
    
}
