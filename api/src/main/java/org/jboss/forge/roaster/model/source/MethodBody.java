/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

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
        extends BlockHolder<O>,
                Origin<T> {

}
