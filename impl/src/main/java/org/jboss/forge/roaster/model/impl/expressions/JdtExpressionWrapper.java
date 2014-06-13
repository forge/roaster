/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;


import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public interface JdtExpressionWrapper<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      J extends org.eclipse.jdt.core.dom.Expression>
      extends ASTNode<J>
{

   public org.eclipse.jdt.core.dom.Expression wireAndGetExpression(ExpressionSource<O,P,?> expression, P parent, AST ast);

}