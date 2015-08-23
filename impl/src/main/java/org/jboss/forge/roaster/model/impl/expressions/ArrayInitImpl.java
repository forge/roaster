/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayInitImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,ArrayInit<O,P>,ArrayInitializer>
      implements ArrayInit<O,P>
{

   private List<ExpressionSource<O,ArrayInit<O,P>,?>> elements = Collections.EMPTY_LIST;

   private ArrayInitializer init;

   @Override
   public ArrayInit<O,P> addElement(ArrayInit<?,?> subRow)
   {
      if (elements.isEmpty())
      {
         elements = new ArrayList();
      }
      elements.add((ExpressionSource<O,ArrayInit<O,P>,?>) subRow);
      return this;
   }

   @Override
   public ArrayInit<O,P> addElement(ExpressionSource<?,?,?> subElement)
   {
      if (elements.isEmpty())
      {
         elements = new ArrayList();
      }
      elements.add((ExpressionSource<O,ArrayInit<O,P>,?>) subElement);
      return this;
   }

   @Override
   public int size()
   {
      return elements.size();
   }

   @Override
   public int getDimension()
   {
      if (elements.isEmpty())
      {
         return 0;
      } else
      {
         if (elements.get(0) instanceof ArrayInit)
         {
            return elements.size();
         } else
         {
            return 1;
         }
      }
   }

   @Override
   public ArrayInitializer materialize(AST ast)
   {
      if (isMaterialized())
      {
         return init;
      }
      this.init = ast.newArrayInitializer();
      for (ExpressionSource<O,ArrayInit<O,P>,?> src : elements)
      {
         this.init.expressions().add(wireAndGetExpression(src, this, ast));
      }
      return init;
   }

   @Override
   public ArrayInitializer getInternal()
   {
      return init;
   }

   @Override
   public void setInternal(ArrayInitializer jdtNode)
   {
      super.setInternal(jdtNode);
      this.init = jdtNode;
   }

   @Override
   public List<ExpressionSource<O,ArrayInit<O,P>,?>> getElements()
   {
      return Collections.unmodifiableList(elements);
   }
}
