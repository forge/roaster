/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.Expression;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.OrdinalArgument;
import org.jboss.forge.roaster.model.expressions.PostFixExpression;
import org.jboss.forge.roaster.model.expressions.PostfixOp;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class SimpleAccessorImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends OrdinalArgument<O,P,E>,
      J extends Expression>
      extends AccessorImpl<O,P,E,J>
      implements OrdinalArgument<O,P,E>
{


   public PostFixExpression<O,OrdinalArgument<O,P,E>> inc()
   {
      return new PostFixImpl<O,OrdinalArgument<O,P,E>>(PostfixOp.INC).setExpression(this);
   }

   public PostFixExpression<O,OrdinalArgument<O,P,E>> dec()
   {
      return new PostFixImpl<O,OrdinalArgument<O,P,E>>(PostfixOp.DEC).setExpression(this);
   }

}
