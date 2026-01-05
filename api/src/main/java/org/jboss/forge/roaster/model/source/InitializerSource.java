package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Initializer;

/**
 * Represents a Java initializer in source form.
 */
public interface InitializerSource<O extends JavaSource<O>> extends Initializer<O, InitializerSource<O>>, 
    JavaDocCapableSource<InitializerSource<O>>, StaticCapableSource<InitializerSource<O>>, LocationCapable {

    /**
     * Set the inner body of this {@link Initializer}
     */
    InitializerSource<O> setBody(String body);

}
