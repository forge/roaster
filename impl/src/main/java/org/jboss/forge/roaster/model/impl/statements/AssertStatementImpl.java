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
import org.jboss.forge.roaster.model.statements.AssertStatement;

public class AssertStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,AssertStatement<O,P>,org.eclipse.jdt.core.dom.AssertStatement>
      implements AssertStatement<O,P>
{

   private org.eclipse.jdt.core.dom.AssertStatement assrt;

   private ExpressionSource<O,AssertStatement<O,P>,?> assertion;
   private ExpressionSource<O,AssertStatement<O,P>,?> message;


   @Override
   public org.eclipse.jdt.core.dom.AssertStatement materialize(AST ast)
   {
      if (assrt != null)
      {
         return assrt;
      }
      assrt = ast.newAssertStatement();
      if (assertion != null)
      {
         assrt.setExpression(wireAndGetExpression(assertion, this, ast));
      }
      if (message != null)
      {
         assrt.setMessage(wireAndGetExpression(message, this, ast));
      }
      return assrt;
   }

   @Override
   public ExpressionSource<O,AssertStatement<O,P>,?> getAssertion()
   {
      return assertion;
   }

   @Override
   public AssertStatement<O,P> setAssertion(ExpressionSource<?,?,?> expression)
   {
      ExpressionSource<O,AssertStatement<O,P>,?> cast = (ExpressionSource<O,AssertStatement<O,P>,?>) expression;
      this.assertion = cast;
      cast.setOrigin(this);
      return this;
   }

   @Override
   public ExpressionSource<O,AssertStatement<O,P>,?> getMessage()
   {
      return message;
   }

   @Override
   public AssertStatement<O,P> setMessage(ExpressionSource<?,?,?> msg)
   {
      ExpressionSource<O,AssertStatement<O,P>,?> cast = (ExpressionSource<O,AssertStatement<O,P>,?>) msg;
      this.message = cast;
      cast.setOrigin(this);
      return this;
   }

   @Override
   public org.eclipse.jdt.core.dom.AssertStatement getInternal()
   {
      return assrt;
   }

   @Override
   public void setInternal(org.eclipse.jdt.core.dom.AssertStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.assrt = jdtNode;
   }
}
