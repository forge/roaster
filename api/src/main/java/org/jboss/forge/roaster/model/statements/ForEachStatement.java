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
 * Represents an enhanced for loop statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ForEachStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends LoopingStatement<O,P,ForEachStatement<O,P>>,
      BodyHolder<O,P,ForEachStatement<O,P>>
{

   /**
    * Returns the name of the iterator variable
    * @return  the name of the iterator variable
    */
   public String getIteratorName();

   /**
    * Returns the name of the type of the iterator variable
    * @return  the name of the type of the iterator variable
    */
   public String getIteratorType();


   /**
    *  Sets the name and type of the iterator variable
    * @param klass   the name of the type of the iterator
    * @param name    the name of the iterator
    * @return  this <code>ForEachStatement</code> itself
    */
   public ForEachStatement<O,P> setIterator(String klass, String name);

   /**
    *  Sets the name and type of the iterator variable
    * @param klass   the type of the iterator
    * @param name    the name of the iterator
    * @return  this <code>ForEachStatement</code> itself
    */
   public ForEachStatement<O,P> setIterator(Class klass, String name);

   /**
    * Returns the expression that evaluates to the collection to be iterated
    * @return the expression that evaluates to the collection to be iterated
    */
   public ExpressionSource<O,ForEachStatement<O,P>,?> getSource();

   /**
    * Sets the expression that evaluates to the collection to be iterated
    * @param expr
    * @return  this <code>ForEachStatement</code> itself
    */
   public ForEachStatement<O,P> setSource(ExpressionSource<?,?,?> expr);

}
