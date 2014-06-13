/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.source.BlockHolder;

/**
 * Represent a block, a sequence of statements possibly including other blocks
 */
public interface Block<O extends JavaType<O>,
                       P extends BlockHolder<O>>
        extends Origin<P>,
                BlockHolder<O> {

}
