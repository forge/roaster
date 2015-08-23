/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeclareExpressionImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends NonPrimitiveExpressionImpl<O,P,DeclareExpression<O,P>,VariableDeclarationExpression>
      implements DeclareExpression<O,P>
{

   private VariableDeclarationExpression var;

   private String type;
   private List<String> names = new ArrayList<String>();
   private Map<String,ExpressionSource<O,DeclareExpression<O,P>,?>> initExpressions = new HashMap<String,ExpressionSource<O,DeclareExpression<O,P>,?>>();

   public DeclareExpressionImpl(String type, String name)
   {
      this.type = type;
      this.names.add(name);
   }

   public DeclareExpressionImpl(String klass, String name, ExpressionSource<O,?,?> init)
   {
      this(klass, name);
      this.initExpressions.put(name, (ExpressionSource<O,DeclareExpression<O,P>,?>) init);
   }

   public DeclareExpression<O,P> addDeclaration(String name, ExpressionSource<?,?,?> expr)
   {
      addDeclaration(name);
      if (expr != null)
      {
         initExpressions.put(name, (ExpressionSource<O,DeclareExpression<O,P>,?>) expr);
      }
      return this;
   }

   public DeclareExpression<O,P> addDeclaration(String name)
   {
      this.names.add(name);
      return this;
   }

   @Override
   public VariableDeclarationExpression materialize(AST ast)
   {
      if (isMaterialized())
      {
         return var;
      }
      VariableDeclarationFragment frag = ast.newVariableDeclarationFragment();

      frag.setName(ast.newSimpleName(getVariableName()));
      var = ast.newVariableDeclarationExpression(frag);
      var.setType(JDTHelper.getType(getVariableType(), ast));
      if (getInitExpression() != null)
      {
         frag.setInitializer(wireAndGetExpression(getInitExpression(), this, ast));
      }

      if (names.size() > 1)
      {
         for (int j = 1; j < names.size(); j++)
         {
            VariableDeclarationFragment moreFrag = ast.newVariableDeclarationFragment();
            String n = names.get(j);
            moreFrag.setName(ast.newSimpleName(n));
            if (initExpressions.containsKey(n))
            {
               moreFrag.setInitializer(wireAndGetExpression(initExpressions.get(n), this, ast));
            }
            var.fragments().add(moreFrag);
         }
      }

      return var;
   }

   @Override
   public VariableDeclarationExpression getInternal()
   {
      return var;
   }

   @Override
   public void setInternal(VariableDeclarationExpression jdtNode)
   {
      super.setInternal(jdtNode);
      this.var = jdtNode;
   }

   @Override
   public String getVariableName()
   {
      return names.isEmpty() ? null : names.get(0);
   }

   @Override
   public String getVariableType()
   {
      return type;
   }

   @Override
   public List<String> getVariableNames()
   {
      return names;
   }

   @Override
   public Map<String,ExpressionSource<O,DeclareExpression<O,P>,?>> getInitExpressions()
   {
      return Collections.unmodifiableMap(initExpressions);
   }

   @Override
   public boolean isSingleDeclaration()
   {
      return names.size() == 1;
   }

   @Override
   public int getNumVariables()
   {
      return names.size();
   }

   @Override
   public ExpressionSource<O,DeclareExpression<O,P>,?> getInitExpression()
   {
      return getVariableName() != null ? initExpressions.get(getVariableName()) : null;
   }
}
