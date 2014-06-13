/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.ForEachStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;

public class ForEachStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,ForEachStatement<O,P>,EnhancedForStatement>
      implements ForEachStatement<O,P>
{

   private EnhancedForStatement iter;

   private String name;
   private String type;
   private ExpressionSource<O,ForEachStatement<O,P>,?> source;
   private BlockStatement<O,ForEachStatement<O,P>> body;

   public ForEachStatementImpl()
   {
   }

   @Override
   public String getIteratorName()
   {
      return name;
   }

   @Override
   public String getIteratorType()
   {
      return type;
   }

   @Override
   public ForEachStatement<O,P> setIterator(String klass, String name)
   {
      this.name = name;
      this.type = klass;
      return this;
   }

   @Override
   public ForEachStatement<O,P> setIterator(Class klass, String name)
   {
      return setIterator(klass.getName(), name);
   }

   @Override
   public ExpressionSource<O,ForEachStatement<O,P>,?> getSource()
   {
      return source;
   }

   @Override
   public ForEachStatement<O,P> setSource(ExpressionSource<?,?,?> expr)
   {
      ExpressionSource<O,ForEachStatement<O,P>,?> cast = (ExpressionSource<O,ForEachStatement<O,P>,?>) expr;
      this.source = cast;
      return this;
   }

   @Override
   public EnhancedForStatement materialize(AST ast)
   {
      if (iter != null)
      {
         return iter;
      }
      iter = ast.newEnhancedForStatement();

      iter.getParameter().setName(iter.getAST().newSimpleName(name));
      iter.getParameter().setType(JDTHelper.getType(type, iter.getAST()));

      if (source != null)
      {
         iter.setExpression(wireAndGetExpression(source, this, ast));
      }
      if (body != null)
      {
         iter.setBody(wireAndGetStatement(body, this, ast));
      }
      return iter;
   }

   @Override
   public EnhancedForStatement getInternal()
   {
      return iter;
   }

   @Override
   public void setInternal(EnhancedForStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.iter = jdtNode;
   }

   @Override
   public BlockStatement<O,ForEachStatement<O,P>> getBody()
   {
      return body;
   }

   @Override
   public ForEachStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      BlockStatement<O,ForEachStatement<O,P>> cast = (BlockStatement<O,ForEachStatement<O,P>>) body;
      this.body = cast;
      return this;
   }

   @Override
   public ForEachStatement<O,P> setBody(StatementSource<?,?,?> statement)
   {
      this.body = wrap(statement);
      return this;
   }

}
