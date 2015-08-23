/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.Map;

/**
 * Represents a try catch statement in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface TryCatchStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends ControlFlowStatement<O,P,TryCatchStatement<O,P>>,
      BodyHolder<O,P,TryCatchStatement<O,P>>
{

   /**
    * Returns a map that associates each catch-ed throwable declaration to its handling block
    * @return an immutable map associating the declarations of the throwables to their handling blocks
    */
   public Map<DeclareExpression<O,TryCatchStatement<O,P>>,BlockStatement<O,TryCatchStatement<O,P>>> getCatches();

   /**
    * Adds a catch block to the statement, associating it to a throwable <code>declaration</code>.
    * @param declaration The declaration of the <code>Throwable</code> to catch
    * @param block       The catch block
    * @return this <code>TryCatchStatement</code> itself
    */
   public TryCatchStatement<O,P> addCatch(DeclareExpression<?,?> declaration,
                                          BlockStatement<?,?> block);

   /**
    * Adds a catch block to the statement, associating it to a throwable <code>declaration</code>.
    * @param declaration The declaration of the <code>Throwable</code> to catch
    * @param block       A single statement which will become the catch block
    * @return this <code>TryCatchStatement</code> itself
    */
   public TryCatchStatement<O,P> addCatch(DeclareExpression<?,?> declaration,
                                          StatementSource<?,?,?> block);


   /**
    * Returns the body of the finally part
    * @return the body of the finally part
    */
   public BlockStatement<O,TryCatchStatement<O,P>> getFinally();

   /**
    * Sets the body of the finally part from a whole block
    * @param block The block containing the body statements
    * @return this <code>TryCatchStatement</code> itself
    */
   public TryCatchStatement<O,P> setFinally(BlockStatement<?,?> block);

   /**
    * Sets the body of the finally part from a single statement, wrapping it inside a block
    * @param block The statement that will become the body
    * @return this <code>TryCatchStatement</code> itself
    */
   public TryCatchStatement<O,P> setFinally(StatementSource<?,?,?> block);

}
