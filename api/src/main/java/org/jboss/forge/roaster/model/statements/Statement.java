/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;


import org.jboss.forge.roaster.Origin;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Abstract interface that represents a java statement
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface Statement<O extends JavaType<O>,
      P extends BlockHolder<O>,
      S extends Statement<O,P,S>>
      extends Origin<P>,
      ExpressionHolder<O>
{

   public void setOrigin(P origin);

   /**
    * Assigns a label to the statement
    * @param label   the label
    * @return  This <code>Statement</code> itself
    */
   public S setLabel(String label);

   /**
    * Returns the label for this statement, if any
    * @return the label associated to the statement
    */
   public String getLabel();

   /**
    * Checks whether a label has been associated to the statement
    * @return true if the statement has a label, false otherwise
    */
   public boolean hasLabel();
}
