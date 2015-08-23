/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.statements;

import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.statements.BlockStatement;
import org.jboss.forge.roaster.model.statements.StatementSource;

public class BlockStatementImpl<O extends JavaSource<O>,
      P extends BlockHolder<O>>
      extends InnerBlockImpl<O,P,BlockStatement<O,P>>
      implements BlockStatement<O,P>
{

   public BlockStatementImpl()
   {
   }

   public BlockStatementImpl(StatementSource<?,?,?> body)
   {
      addStatement(body);
   }

   @Override
   public BlockStatement<O,P> setLabel(String label)
   {
      this.label = label;
      return this;
   }
}
