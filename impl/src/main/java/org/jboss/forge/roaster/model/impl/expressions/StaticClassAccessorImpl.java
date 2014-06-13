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
import org.jboss.forge.roaster.model.expressions.ClassLiteral;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class StaticClassAccessorImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AccessorImpl<O,P,ClassLiteral<O,P>,Name>
      implements ExpressionChainLink<O,P>
{

   private Name klass;

   private String name;

   public StaticClassAccessorImpl(String name)
   {
      this.name = name;
   }

   @Override
   public Name materialize(AST ast)
   {
      if (isMaterialized())
      {
         return klass;
      }
      klass = ast.newName(name);
      return klass;
   }

   @Override
   public Name getInternal()
   {
      return klass;
   }

   @Override
   public void setInternal(Name jdtNode)
   {
      super.setInternal(jdtNode);
      this.klass = jdtNode;
   }

   @Override
   public ExpressionSource<O,P,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      throw new UnsupportedOperationException("Should not be invoked on this");
   }
}
