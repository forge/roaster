/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.jboss.forge.roaster.model.expressions.BasicExpressionFactory;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.impl.expressions.BasicExpressionFactoryImpl;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.DeclareStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeclareStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends StatementImpl<O,P,DeclareStatement<O,P>,VariableDeclarationStatement>
      implements DeclareStatement<O,P>
{

   private VariableDeclarationStatement var;

   private BasicExpressionFactory<O,DeclareStatement<O,P>> factory = new BasicExpressionFactoryImpl<O,DeclareStatement<O,P>,org.eclipse.jdt.core.dom.Expression>();

   private Map<String,ExpressionSource<O,DeclareStatement<O,P>,?>> initializers = new HashMap<String,ExpressionSource<O,DeclareStatement<O,P>,?>>();
   private List<String> names = new ArrayList<String>();
   private String type;

   public DeclareStatementImpl()
   {
   }

   @Override
   public List<String> getVariableNames()
   {
      return names;
   }

   @Override
   public String getVariableName()
   {
      return names.get(0);
   }

   @Override
   public String getVariableType()
   {
      return type;
   }

   @Override
   public DeclareStatement<O,P> setVariable(Class klass, String name)
   {
      return setVariable(klass.getName(), name);
   }

   @Override
   public DeclareStatement<O,P> setVariable(String klass, String name)
   {
      return setVariable(klass, name, null);
   }

   @Override
   public DeclareStatement<O,P> setVariable(Class klass, String name, ExpressionSource<?,?,?> init)
   {
      return setVariable(klass.getName(), name, init);
   }

   @Override
   public DeclareStatement<O,P> setVariable(String klass, String name, ExpressionSource<?,?,?> init)
   {
      this.type = klass;
      names.add(name);
      if (init != null)
      {
         initializers.put(name, (ExpressionSource<O,DeclareStatement<O,P>,?>) init);
      }
      return this;
   }

   @Override
   public DeclareStatement<O,P> addVariable(String name)
   {
      names.add(name);
      return this;
   }

   @Override
   public DeclareStatement<O,P> addVariable(String name, ExpressionSource<?,?,?> init)
   {
      addVariable(name);
      if (init != null)
      {
         this.initializers.put(name, (ExpressionSource<O,DeclareStatement<O,P>,?>) init);
      }
      return this;
   }


   @Override
   public boolean isSingleDeclaration()
   {
      return names.size() == 1;
   }


   @Override
   public Map<String,ExpressionSource<O,DeclareStatement<O,P>,?>> getInitExpressions()
   {
      return Collections.unmodifiableMap(initializers);
   }

   @Override
   public ExpressionSource<O,DeclareStatement<O,P>,?> getInitExpression()
   {
      return initializers.containsKey(getVariableName()) ? initializers.get(getVariableName()) : null;
   }

   @Override
   public VariableDeclarationStatement materialize(AST ast)
   {
      if (var != null)
      {
         return var;
      }

      List<VariableDeclarationFragment> vdfs = new ArrayList<VariableDeclarationFragment>(names.size());
      for (String name : names)
      {
         VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
         vdf.setName(ast.newSimpleName(name));
         if (initializers.containsKey(name))
         {
            vdf.setInitializer(wireAndGetExpression(initializers.get(name), this, ast));
         }
         vdfs.add(vdf);
      }

      var = ast.newVariableDeclarationStatement(vdfs.get(0));
      if (vdfs.size() > 1)
      {
         for (int j = 1; j < vdfs.size(); j++)
         {
            var.fragments().add(vdfs.get(j));
         }
      }

      var.setType(JDTHelper.getType(type, ast));
      return var;
   }

   @Override
   public VariableDeclarationStatement getInternal()
   {
      return var;
   }

   @Override
   public void setInternal(VariableDeclarationStatement jdtNode)
   {
      super.setInternal(jdtNode);
      this.var = jdtNode;
   }
}
