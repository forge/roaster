/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.SuperStatement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SuperStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,SuperStatement<O,P>,SuperConstructorInvocation>
      implements SuperStatement<O,P>
{

   private SuperConstructorInvocation invoke;

   private List<Argument<O,SuperStatement<O,P>,?>> argumentList = Collections.EMPTY_LIST;

   public SuperStatementImpl()
   {
   }

   @Override
   public List<Argument<O,SuperStatement<O,P>,?>> getArguments()
   {
      return Collections.unmodifiableList(argumentList);
   }

   @Override
   public SuperStatement<O,P> addArgument(Argument<?,?,?> argument)
   {
      Argument<O,SuperStatement<O,P>,?> cast = (Argument<O,SuperStatement<O,P>,?>) argument;
      if (argumentList.isEmpty())
      {
         argumentList = new LinkedList<Argument<O,SuperStatement<O,P>,?>>();
      }
      argumentList.add(cast);
      return this;
   }

   @Override
   public SuperConstructorInvocation materialize(AST ast)
   {
      if (invoke != null)
      {
         return invoke;
      }
      invoke = ast.newSuperConstructorInvocation();

      for (Argument argument : argumentList)
      {
         invoke.arguments().add(wireAndGetExpression(argument, this, ast));
      }
      return invoke;
   }

   @Override
   public SuperConstructorInvocation getInternal()
   {
      return invoke;
   }

   @Override
   public void setInternal(SuperConstructorInvocation jdtNode)
   {
      super.setInternal(jdtNode);
      this.invoke = jdtNode;
   }
}
