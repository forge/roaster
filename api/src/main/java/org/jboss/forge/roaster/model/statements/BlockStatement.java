/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a code block statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */

public interface BlockStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BlockSource<O,P,BlockStatement<O,P>>,
      StatementSource<O,P,BlockStatement<O,P>>
{
}
