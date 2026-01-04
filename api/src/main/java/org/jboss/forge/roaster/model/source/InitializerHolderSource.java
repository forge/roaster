package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.Initializer;
import org.jboss.forge.roaster.model.InitializerHolder;

/**
 * Represents a {@link JavaSource} that may declare initializers.
 */
public interface InitializerHolderSource<O extends JavaSource<O>> extends InitializerHolder<O>, MemberHolderSource<O> {

    /**
     * Get a {@link List} of all {@link InitializerSource}s declared by this {@link O} instance, if any; otherwise return an 
     * empty {@link List}
     */
    @Override
    List<InitializerSource<O>> getInitializers();
    
    /**
     * Add an uninitialized {@link InitializerSource} declaration to this {@link O} instance. This {@link InitializerSource} will
     * be a stub until further modified.
     */
    InitializerSource<O> addInitializer();

    /**
     * Add a new {@link InitializerSource} declaration to this {@link O} instance, using the given {@link String} as the
     * initializer declaration.
     * <p/>
     * <strong>For example:</strong><br>
     * <code>Initializer initializer = javaClass.addInitializer("static { System.out.println(\"Hello\") }")</code>
     */
    InitializerSource<O> addInitializer(final String body);
    
    /**
     * Remove the given {@link InitializerSource} declaration from this {@link O} instance, if it exists; otherwise, do
     * nothing.
     */
    O removeInitializer(final Initializer<O, ?> initializer);
    
}
