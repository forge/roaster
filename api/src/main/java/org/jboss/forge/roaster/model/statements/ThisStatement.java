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
 * Represents a constructor call statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ThisStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementSource<O,P,ThisStatement<O,P>>
{

   /**
    * Returns the current list of arguments
    * @return  An immutable list containing the arguments
    */
   public List<Argument<O,ThisStatement<O,P>,?>> getArguments();

   /**
    * Adds an argument to the constructor invocation
    * @param argument  The argument to be added
    * @return  The <code>ThisStatement</code> itself
    */
   public ThisStatement<O,P> addArgument(Argument<?,?,?> argument);

}
