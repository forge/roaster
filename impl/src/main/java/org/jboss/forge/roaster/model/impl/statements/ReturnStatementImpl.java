/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ReturnStatement;

public class ReturnStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,ReturnStatement<O,P>,org.eclipse.jdt.core.dom.ReturnStatement>
      implements ReturnStatement<O,P>
{

   private org.eclipse.jdt.core.dom.ReturnStatement ret;

   private ExpressionSource<O,ReturnStatement<O,P>,?> result;

   public ReturnStatementImpl()
   {
   }

   public ReturnStatement<O,P> setReturn(ExpressionSource<?,?,?> expression)
   {
      ExpressionSource<O,ReturnStatement<O,P>,?> cast = (ExpressionSource<O,ReturnStatement<O,P>,?>) expression;
      result = cast;
      cast.setOrigin(this);
      return this;
   }

   public ExpressionSource<O,ReturnStatement<O,P>,?> getReturn()
   {
      return result;
   }

   @Override
   public org.eclipse.jdt.core.dom.ReturnStatement materialize(AST ast)
   {
      if (isMaterialized())
      {
         return ret;
      }
      ret = ast.newReturnStatement();

      if (result != null)
      {
         ret.setExpression(wireAndGetExpression(result, this, ast));
      }
      return ret;
   }

   @Override
   public org.eclipse.jdt.core.dom.ReturnStatement getInternal()
   {
      return ret;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.ReturnStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.ret = jdtNode;
   }
}
