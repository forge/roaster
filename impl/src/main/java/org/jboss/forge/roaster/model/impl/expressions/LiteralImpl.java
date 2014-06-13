/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Literal;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class LiteralImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      L extends Literal<O,P,L>,
      J extends Expression>
      extends ExpressionImpl<O,P,L,J>
      implements Literal<O,P,L>
{

   public LiteralImpl()
   {
   }

}
