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
 * Represents an assert statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface AssertStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementSource<O,P,AssertStatement<O,P>>
{

   /**
    * Returns the assertion expression
    * @return the assertion expression
    */
   public ExpressionSource<O,AssertStatement<O,P>,?> getAssertion();

   /**
    * Sets the assertion expression
    * @param expression the assertion expression expected to hold true
    * @return this <code>AssertStatement</code> itself
    */
   public AssertStatement<O,P> setAssertion(ExpressionSource<?,?,?> expression);


   /**
    * Returns the message expression
    * @return the message expression
    */
   public ExpressionSource<O,AssertStatement<O,P>,?> getMessage();

   /**
    * Sets the expression that evaluates to the message to be displayed in case of failed assertion
    * @param msg  The message expression
    * @return this <code>AssertStatement</code> itself
    */
   public AssertStatement<O,P> setMessage(ExpressionSource<?,?,?> msg);

}
