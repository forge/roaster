/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ArrayAccessImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends SimpleAccessorImpl<O,P,ArrayIndexer<O,P>,ArrayAccess>
      implements ArrayIndexer<O,P>,
      ExpressionChainLink<O,ArrayIndexer<O,P>>
{

   private ExpressionSource<O,ArrayIndexer<O,P>,?> index;
   private ExpressionSource<O,ArrayIndexer<O,P>,?> target;
   private ArrayAccess axx;

   public ArrayAccessImpl(ExpressionSource<?,?,?> index)
   {
      ExpressionSource<O,ArrayIndexer<O,P>,?> cast = (ExpressionSource<O,ArrayIndexer<O,P>,?>) index;
      this.index = cast;
   }

   @Override
   public ArrayAccess materialize(AST ast)
   {
      if (isMaterialized())
      {
         return axx;
      }
      axx = ast.newArrayAccess();
      axx.setIndex(wireAndGetExpression(index, this, ast));
      if (target != null)
      {
         axx.setArray(wireAndGetExpression(target, this, ast));
      }
      return axx;
   }

   @Override
   public ArrayAccess getInternal()
   {
      return axx;
   }

   @Override
   public void setInternal(ArrayAccess jdtNode)
   {
      super.setInternal(jdtNode);
      this.axx = jdtNode;
   }

   @Override
   public ExpressionSource<O,ArrayIndexer<O,P>,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      ExpressionSource<O,ArrayIndexer<O,P>,?> cast = (ExpressionSource<O,ArrayIndexer<O,P>,?>) child;
      this.target = cast;
      return cast;
   }

   @Override
   public ExpressionSource<O,ArrayIndexer<O,P>,?> getIndex()
   {
      return index;
   }

   @Override
   public ArrayIndexer<O,P> setIndex(ExpressionSource<?,?,?> index)
   {
      this.index = (ExpressionSource<O,ArrayIndexer<O,P>,?>) index;
      return this;
   }

   @Override
   public ExpressionSource<O,ArrayIndexer<O,P>,?> getInvocationTarget()
   {
      return target;
   }

   @Override
   public ArrayIndexer<O,P> setInvocationTarget(ExpressionSource<?,?,?> target)
   {
      this.target = (ExpressionSource<O,ArrayIndexer<O,P>,?>) target;
      return this;
   }
}
