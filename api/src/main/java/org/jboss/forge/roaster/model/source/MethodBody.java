package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.Block;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 * The body of a method
 */
public interface MethodBody<O extends JavaSource<O>, T extends MethodSource<O>>
        extends BlockHolder<O,T>,
                Origin<T> {

}
