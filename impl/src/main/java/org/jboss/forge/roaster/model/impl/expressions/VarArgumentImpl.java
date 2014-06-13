/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Name;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.source.JavaSource;

public class VarArgumentImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends SimpleAccessorImpl<O,P,Variable<O,P>,Name>
      implements Variable<O,P>,
      ExpressionChainLink<O,Variable<O,P>>
{

   private Name var;

   private String name;

   private ExpressionSource<O,Variable<O,P>,?> expression;

   public VarArgumentImpl(String name)
   {
      this.name = name;
   }

   @Override
   public Name materialize(AST ast)
   {
      if (isMaterialized())
      {
         return var;
      }
      var = ast.newName(name);
      return var;
   }

   @Override
   public Name getInternal()
   {
      return var;
   }

   @Override
   public void setInternal(Name jdtNode)
   {
      super.setInternal(jdtNode);
      this.var = jdtNode;
   }

   @Override
   public String getName()
   {
      return name;
   }


   @Override
   public ExpressionSource<O,Variable<O,P>,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      ExpressionSource<O,Variable<O,P>,?> cast = (ExpressionSource<O,Variable<O,P>,?>) child;
      this.expression = cast;
      return cast;
   }
}
