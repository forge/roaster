/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;


import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represent a class literal expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ClassLiteral<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends Literal<O,P,ClassLiteral<O,P>>,
      NonPrimitiveExpression<O,P,ClassLiteral<O,P>>,
      Accessor<O,P,ClassLiteral<O,P>>
{

      /**
       * Returns the name of the class represented by this literal expression
       * @return the name of the class
       */
      public String getValue();
}
