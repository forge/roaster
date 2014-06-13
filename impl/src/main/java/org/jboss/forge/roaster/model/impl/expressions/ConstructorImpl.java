/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ConstructorImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AccessorImpl<O,P,ConstructorExpression<O,P>,ClassInstanceCreation>
      implements ConstructorExpression<O,P>,
      ExpressionChainLink<O,P>
{

   private ClassInstanceCreation constr;

   private String type;
   private List<Argument<O,ConstructorExpression<O,P>,?>> arguments = Collections.EMPTY_LIST;

   public ConstructorImpl(String type)
   {
      this.type = type;
   }

   @Override
   public ConstructorExpression<O,P> addArgument(Argument<?,?,?> arg)
   {
      if (arguments.isEmpty())
      {
         arguments = new LinkedList<Argument<O,ConstructorExpression<O,P>,?>>();
      }
      arguments.add((Argument<O,ConstructorExpression<O,P>,?>) arg);
      return this;
   }

   @Override
   public List<Argument<O,ConstructorExpression<O,P>,?>> getArguments()
   {
      return arguments;
   }

   @Override
   public ClassInstanceCreation materialize(AST ast)
   {
      if (isMaterialized())
      {
         return constr;
      }
      constr = ast.newClassInstanceCreation();
      constr.setType(JDTHelper.getType(type, ast));

      for (Argument<O,ConstructorExpression<O,P>,?> arg : arguments)
      {
         constr.arguments().add(wireAndGetExpression(arg, this, ast));
      }
      return constr;
   }

   @Override
   public ClassInstanceCreation getInternal()
   {
      return constr;
   }

   @Override
   public void setInternal(ClassInstanceCreation jdtNode)
   {
      super.setInternal(jdtNode);
      this.constr = jdtNode;
   }

   @Override
   public ExpressionSource<O,P,?> chainExpression(ExpressionSource<O,?,?> child)
   {
      throw new IllegalStateException("This method should not be invoked [constructors start chains]");
   }
}

