/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a break statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */

public interface BreakStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BranchingStatement<O,P,BreakStatement<O,P>>
{

   /**
    * Returns the label to jump to in case of break
    * @return  the label to jump to in case of break
    */
   public String getBreakToLabel();

   /**
    * Sets the label to jump to in case of break
    * @param label   The label
    * @return  this <code>BreakStatement</code> itself
    */
   public BreakStatement<O,P> setBreakToLabel(String label);

}
