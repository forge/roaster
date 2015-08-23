/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a case statement in source format, to be used with a switch statement
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface SwitchCaseStatement<O extends JavaSource<O>,
      P extends SwitchStatement<O,? extends BlockHolder<O>>>
      extends StatementSource<O,P,SwitchCaseStatement<O,P>>
{

   /**
    * Returns the expression evaluated in this case
    * @return the expression evaluated in this case
    */
   public ExpressionSource<O,SwitchCaseStatement<O,P>,?> getCaseExpression();

   /**
    * Sets the expression for this case
    * @param src the case expression
    * @return this <code>SwitchCaseStatement</code> itself
    */
   public SwitchCaseStatement<O,P> setCaseExpression(ExpressionSource<?,?,?> src);

}
