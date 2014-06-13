/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.List;

/**
 * Represents a for loop statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ForStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends LoopingStatement<O,P,ForStatement<O,P>>,
      BodyHolder<O,P,ForStatement<O,P>>
{

   /**
    * Returns the declaration of the iterator variables
    * @return the declaration of the iterator variables
    */
   public DeclareExpression<O,ForStatement<O,P>> getDeclaration();

   /**
    * Sets the declaration of the iterator variables, including their initial values
    * @param declaration   the declaration of the iterator variables
    * @return this <code>ForStatement</code> itself
    */
   public ForStatement<O,P> setDeclaration(DeclareExpression<?,?> declaration);

   /**
    * Returns the conditional expression evaluated by this loop
    * @return the conditional expression evaluated by this loop
    */
   public ExpressionSource<O,ForStatement<O,P>,?> getCondition();

   /**
    * Sets the condition expression evaluated by this loop
    * @param expr The condition expression
    * @return this <code>ForStatement</code> itself
    */
   public ForStatement<O,P> setCondition(ExpressionSource<?,?,?> expr);


   /**
    * Returns the expressions used to update the iterator variables
    * @return An immutable collection holding the expressions
    */
   public List<ExpressionSource<O,ForStatement<O,P>,?>> getUpdates();

   /**
    * Adds an update expression
    * @param expression the expression to be added
    * @return this <code>ForStatement</code> itself
    */
   public ForStatement<O,P> addUpdate(ExpressionSource<?,?,?> expression);

}
