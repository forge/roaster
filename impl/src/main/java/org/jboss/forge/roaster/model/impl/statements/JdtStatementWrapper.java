/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ASTNode;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.Statement;

public interface JdtStatementWrapper<O extends JavaSource<O>,
      P extends BlockHolder<O>,
      J extends org.eclipse.jdt.core.dom.Statement>
      extends ASTNode<J>
{

   // return type is not J because of LabeledStatements which wrap the actual Statement type
   org.eclipse.jdt.core.dom.Statement wireAndGetStatement(Statement<O,P,?> statement,
                                                          P parent,
                                                          AST ast);
}
