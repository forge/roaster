/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents an if statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface IfStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ControlFlowStatement<O,P,IfStatement<O,P>>,
      BodyHolder<O,P,IfStatement<O,P>>
{

   /**
    * Returns the conditional expression evaluated by this if statement
    * @return the conditional expression evaluated by this if statement
    */
   public ExpressionSource<O,IfStatement<O,P>,?> getCondition();

   /**
    * Sets the condition expression evaluated by this if statement
    * @param expr The condition expression
    * @return this <code>IfStatement</code> itself
    */
   public IfStatement<O,P> setCondition(ExpressionSource<?,?,?> expr);


   /**
    * Returns the body of the then part
    * @return the body of the then part
    */
   public BlockStatement<O,IfStatement<O,P>> getThen();

   /**
    * Sets the body of the then part from a single statement, wrapping it inside a block
    * @param ifBlock The statement that will become the body
    * @return this <code>IfStatement</code> itself
    */
   public IfStatement<O,P> setThen(StatementSource<?,?,?> ifBlock);

   /**
    * Sets the body of the then part from a whole block
    * @param ifBlock The block containing the body statements
    * @return this <code>IfStatement</code> itself
    */
   public IfStatement<O,P> setThen(BlockSource<?,?,?> ifBlock);

   /**
    * Returns the body of the else part
    * @return the body of the else part
    */
   public BlockStatement<O,IfStatement<O,P>> getElse();

   /**
    * Sets the body of the else part from a single statement, wrapping it inside a block
    * @param elseBlock The statement that will become the body
    * @return this <code>IfStatement</code> itself
    */
   public IfStatement<O,P> setElse(StatementSource<?,?,?> elseBlock);

   /**
    * Sets the body of the else part from a whole block
    * @param elseBlock The block containing the body statements
    * @return this <code>IfStatement</code> itself
    */
   public IfStatement<O,P> setElse(BlockSource<?,?,?> elseBlock);

}
