/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.ThisStatement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ThisStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,ThisStatement<O,P>,ConstructorInvocation>
      implements ThisStatement<O,P>
{

   private ConstructorInvocation invoke;

   private List<Argument<O,ThisStatement<O,P>,?>> argumentList = Collections.EMPTY_LIST;

   public ThisStatementImpl()
   {
   }

   @Override
   public List<Argument<O,ThisStatement<O,P>,?>> getArguments()
   {
      return Collections.unmodifiableList(argumentList);
   }

   @Override
   public ThisStatement<O,P> addArgument(Argument<?,?,?> argument)
   {
      Argument<O,ThisStatement<O,P>,?> cast = (Argument<O,ThisStatement<O,P>,?>) argument;
      if (argumentList.isEmpty())
      {
         argumentList = new LinkedList<Argument<O,ThisStatement<O,P>,?>>();
      }
      argumentList.add(cast);
      return this;
   }

   @Override
   public ConstructorInvocation materialize(AST ast)
   {
      if (invoke != null)
      {
         return invoke;
      }
      invoke = ast.newConstructorInvocation();

      for (Argument argument : argumentList)
      {
         invoke.arguments().add(wireAndGetExpression(argument, this, ast));
      }
      return invoke;
   }

   @Override
   public ConstructorInvocation getInternal()
   {
      return invoke;
   }

   @Override
   public void setInternal(ConstructorInvocation jdtNode)
   {
      super.setInternal(jdtNode);
      this.invoke = jdtNode;
   }
}
