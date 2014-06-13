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
 * Represents a continue statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */

public interface ContinueStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BranchingStatement<O,P,ContinueStatement<O,P>>
{

   /**
    * Returns the label to jump to in case of continue
    * @return  the label to jump to in case of continue
    */
   public String getContinueToLabel();

   /**
    * Sets the label to jump to in case of continue
    * @param label   The label
    * @return  this <code>ContinueStatement</code> itself
    */
   public ContinueStatement<O,P> setContinueToLabel(String label);

}
