/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ArrayImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,ArrayConstructorExpression<O,P>,ArrayCreation>
      implements ArrayConstructorExpression<O,P>
{

   private ArrayCreation array;

   private String type;
   private List<ExpressionSource<O,ArrayConstructorExpression<O,P>,?>> dims = Collections.EMPTY_LIST;
   private ArrayInit<O,ArrayConstructorExpression<O,P>> init;

   public ArrayImpl(String type)
   {
      this.type = type;
   }

   @Override
   public ArrayCreation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return array;
      }
      array = ast.newArrayCreation();
      array.setType((ArrayType) JDTHelper.getType(type + new String(new char[getDimension()]).replace("\0", "[]"), ast));
      for (ExpressionSource<O,ArrayConstructorExpression<O,P>,?> dim : dims)
      {
         array.dimensions().add(wireAndGetExpression(dim, this, ast));
      }
      if (init != null)
      {
         array.setInitializer((ArrayInitializer) wireAndGetExpression(init, this, ast));
      }
      return array;
   }

   @Override
   public ArrayCreation getInternal()
   {
      return array;
   }

   @Override
   public void setInternal(ArrayCreation jdtNode)
   {
      super.setInternal(jdtNode);
      this.array = jdtNode;
   }

   @Override
   public ArrayConstructorExpression<O,P> addDimension(ExpressionSource<?,?,?> dim)
   {
      if (dims.isEmpty())
      {
         dims = new ArrayList<ExpressionSource<O,ArrayConstructorExpression<O,P>,?>>();
      }
      dims.add((ExpressionSource<O,ArrayConstructorExpression<O,P>,?>) dim);
      return this;
   }

   @Override
   public ArrayConstructorExpression<O,P> init(ArrayInit<?,?> array)
   {
      this.init = (ArrayInit<O,ArrayConstructorExpression<O,P>>) array;
      return this;
   }

   public int getDimension()
   {
      if (!dims.isEmpty())
      {
         return dims.size();
      } else if (init != null)
      {
         return init.getDimension();
      }
      return 0;
   }

   @Override
   public ArrayInit<O,ArrayConstructorExpression<O,P>> getInit()
   {
      return init;
   }

   @Override
   public List<ExpressionSource<O,ArrayConstructorExpression<O,P>,?>> getDimensions()
   {
      return Collections.unmodifiableList(dims);
   }

}

