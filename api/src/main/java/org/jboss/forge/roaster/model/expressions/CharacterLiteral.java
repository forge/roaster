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
 * Represent a character literal expression in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface CharacterLiteral<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends PrimitiveLiteral<O,P>
{

   /**
    * Returns the value represented by this literal expression
    * @return the literal value of this expression
    */
   public Character getValue();
}
