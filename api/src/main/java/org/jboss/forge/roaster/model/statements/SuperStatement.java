/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;

/**
 * Represents an super constructor invocation statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface SuperStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementSource<O,P,SuperStatement<O,P>>
{

   /**
    * Returns the current list of arguments
    * @return  An immutable list containing the arguments
    */
   public List<Argument<O,SuperStatement<O,P>,?>> getArguments();

   /**
    * Adds an argument to the super constructor invocation
    * @param argument  The argument to be added
    * @return  The <code>SuperStatement</code> itself
    */
   public SuperStatement<O,P> addArgument(Argument<?,?,?> argument);

}
