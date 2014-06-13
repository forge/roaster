/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ArgumentHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.MethodInvokeExpression;
import org.jboss.forge.roaster.model.expressions.MethodWithArgumentsInvokeExpression;
import org.jboss.forge.roaster.model.impl.expressions.MethodInvokeImpl;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.InvokeStatement;

import java.util.Collections;
import java.util.List;

public class InvokeStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,InvokeStatement<O,P>,ExpressionStatement>
      implements InvokeStatement<O,P>
{

   private ExpressionStatement statement;

   private MethodInvokeExpression<O,InvokeStatement<O,P>,?> inner;

   public InvokeStatementImpl()
   {
      inner = new MethodInvokeImpl<O,InvokeStatement<O,P>>(null);
   }

   public InvokeStatementImpl(MethodInvokeExpression<?,?,?> expr)
   {
      this.inner = (MethodInvokeExpression<O,InvokeStatement<O,P>,?>) expr;
   }

   @Override
   public String getMethodName()
   {
      return inner.getMethodName();
   }

   public InvokeStatement<O,P> setMethodName(String method)
   {
      if (inner instanceof MethodCallExpression)
      {
         ((MethodCallExpression) inner).setMethodName(method);
      }
      return this;
   }

   @Override
   public Accessor<O,MethodInvokeExpression<O,InvokeStatement<O,P>,?>,?> getInvocationTarget()
   {
      return (Accessor<O,MethodInvokeExpression<O,InvokeStatement<O,P>,?>,?>) inner.getInvocationTarget();
   }

   @Override
   public InvokeStatement<O,P> setInvocationTarget(Accessor<?,?,?> target)
   {
      inner.setInvocationTarget(target);
      return this;
   }

   @Override
   public List<Argument<O,MethodWithArgumentsInvokeExpression<O,InvokeStatement<O,P>,?>,?>> getArguments()
   {
      if (inner instanceof MethodWithArgumentsInvokeExpression)
      {
         return Collections.unmodifiableList(MethodWithArgumentsInvokeExpression.class.cast(inner).getArguments());
      } else
      {
         return Collections.emptyList();
      }
   }

   @Override
   public InvokeStatement<O,P> addArgument(Argument<?,?,?> argument)
   {
      if (inner instanceof ArgumentHolder)
      {
         ((ArgumentHolder<O,InvokeStatement<O,P>,?>) inner).addArgument(argument);
      } else
      {
         throw new UnsupportedOperationException("This method does not support arguments");
      }
      return this;
   }

   @Override
   public ExpressionStatement materialize(AST ast)
   {
      if (statement != null)
      {
         return statement;
      }
      statement = ast.newExpressionStatement(wireAndGetExpression(inner, this, ast));
      return statement;
   }

   @Override
   public ExpressionStatement getInternal()
   {
      return statement;
   }

   @Override
   public void setInternal(ExpressionStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.statement = jdtNode;
   }

   @Override
   public ExpressionSource<O,InvokeStatement<O,P>,?> getExpr()
   {
      return inner;
   }
}
