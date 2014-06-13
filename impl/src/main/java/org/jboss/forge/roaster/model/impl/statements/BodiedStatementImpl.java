/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.BodyHolder;
import org.jboss.forge.roaster.model.statements.Statement;
import org.jboss.forge.roaster.model.statements.StatementSource;

public abstract class BodiedStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>,
      S extends Statement<O,P,S>&BodyHolder<O,P,S>,
      J extends org.eclipse.jdt.core.dom.Statement>
      extends StatementImpl<O,P,S,J>
      implements BodyHolder<O,P,S>,
      JdtStatementWrapper<O,S,J>
{

   protected BodiedStatementImpl()
   {
   }

   protected BlockStatement<O,S> wrap(StatementSource<?,?,?> body)
   {
      if (body instanceof BlockStatement)
      {
         return (BlockStatement<O,S>) body;
      } else
      {
         BlockStatement<O,S> block = new BlockStatementImpl<O,S>(body);
         return block;
      }
   }


   public org.eclipse.jdt.core.dom.Statement wireAndGetStatement(Statement<O,S,?> statement, S parent, AST ast)
   {
      ASTNode<J> node = (ASTNode<J>) statement;
      statement.setOrigin(parent);
      node.setAst(ast);
      org.eclipse.jdt.core.dom.Statement stat = node.materialize(ast);

      if (statement.hasLabel())
      {
         LabeledStatement labeledStatement = ast.newLabeledStatement();
         labeledStatement.setBody(stat);
         labeledStatement.setLabel(ast.newSimpleName(statement.getLabel()));
         return labeledStatement;
      } else
      {
         return stat;
      }
   }


}
