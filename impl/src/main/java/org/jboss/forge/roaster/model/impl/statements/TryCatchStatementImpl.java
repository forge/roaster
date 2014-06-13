/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.BlockSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;
import org.jboss.forge.roaster.model.statements.TryCatchStatement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class TryCatchStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends BodiedStatementImpl<O,P,TryCatchStatement<O,P>,TryStatement>
      implements TryCatchStatement<O,P>
{

   private TryStatement tryCatch;

   private Map<DeclareExpression<O,TryCatchStatement<O,P>>,BlockStatement<O,TryCatchStatement<O,P>>> catches = Collections.EMPTY_MAP;
   private BlockStatement<O,TryCatchStatement<O,P>> body;
   private BlockStatement<O,TryCatchStatement<O,P>> fine;

   public TryCatchStatementImpl()
   {
   }

   @Override
   public TryStatement materialize(AST ast)
   {
      if (tryCatch != null)
      {
         return tryCatch;
      }
      tryCatch = ast.newTryStatement();

      for (DeclareExpression<O,TryCatchStatement<O,P>> declare : catches.keySet())
      {
         CatchClause clause = ast.newCatchClause();
         clause.setException(varToSvd((VariableDeclarationExpression) wireAndGetExpression(declare, this, ast), ast));
         clause.setBody((org.eclipse.jdt.core.dom.Block) wireAndGetStatement(catches.get(declare), this, ast));
         tryCatch.catchClauses().add(clause);
      }

      if (fine != null)
      {
         tryCatch.setFinally((org.eclipse.jdt.core.dom.Block) wireAndGetStatement(fine, this, ast));
      }
      if (body != null)
      {
         tryCatch.setBody((org.eclipse.jdt.core.dom.Block) wireAndGetStatement(body, this, ast));
      }
      return tryCatch;
   }

   @Override
   public TryStatement getInternal()
   {
      return tryCatch;
   }

   @Override
   public void setInternal(TryStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.tryCatch = jdtNode;
   }

   private SingleVariableDeclaration varToSvd(VariableDeclarationExpression var, AST ast)
   {
      SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
      VariableDeclarationFragment frag = (VariableDeclarationFragment) var.fragments().get(0);
      svd.setName(ast.newSimpleName(frag.getName().getIdentifier()));
      svd.setType(JDTHelper.getType(var.getType().toString(), ast));
      return svd;
   }

   @Override
   public Map<DeclareExpression<O,TryCatchStatement<O,P>>,BlockStatement<O,TryCatchStatement<O,P>>> getCatches()
   {
      return Collections.unmodifiableMap(catches);
   }

   @Override
   public TryCatchStatement<O,P> addCatch(DeclareExpression<?,?> declaration,
                                          BlockStatement<?,?> block)
   {
      if (catches.isEmpty())
      {
         catches = new LinkedHashMap<DeclareExpression<O,TryCatchStatement<O,P>>,BlockStatement<O,TryCatchStatement<O,P>>>();
      }
      DeclareExpression<O,TryCatchStatement<O,P>> castKey = (DeclareExpression<O,TryCatchStatement<O,P>>) declaration;
      BlockStatement<O,TryCatchStatement<O,P>> castValue = (BlockStatement<O,TryCatchStatement<O,P>>) block;
      catches.put(castKey, castValue);
      return this;
   }

   @Override
   public BlockStatement<O,TryCatchStatement<O,P>> getFinally()
   {
      return fine;
   }

   @Override
   public TryCatchStatement<O,P> addCatch(DeclareExpression<?,?> declaration,
                                          StatementSource<?,?,?> block)
   {
      return addCatch(declaration, wrap(block));
   }

   @Override
   public BlockStatement<O,TryCatchStatement<O,P>> getBody()
   {
      return body;
   }

   @Override
   public TryCatchStatement<O,P> setBody(BlockSource<?,?,?> body)
   {
      BlockStatement<O,TryCatchStatement<O,P>> cast = (BlockStatement<O,TryCatchStatement<O,P>>) body;
      this.body = cast;
      return this;
   }

   @Override
   public TryCatchStatement<O,P> setBody(StatementSource<?,?,?> body)
   {
      this.body = wrap(body);
      return this;
   }

   @Override
   public TryCatchStatement<O,P> setFinally(BlockStatement<?,?> block)
   {
      BlockStatement<O,TryCatchStatement<O,P>> cast = (BlockStatement<O,TryCatchStatement<O,P>>) block;
      this.fine = cast;
      return this;
   }

   @Override
   public TryCatchStatement<O,P> setFinally(StatementSource<?,?,?> block)
   {
      this.fine = wrap(block);
      return this;
   }
}
