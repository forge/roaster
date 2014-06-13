/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.statements;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.source.BlockHolder;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Abstract interface that represents generic expression/method/etc.. statements in source format
 *
 * @author <a href="dsotty@gmail.com">Davide Sottara</a>
 */
public interface ExpressionStatement<O extends JavaSource<O>,
      P extends BlockHolder<O>,
      S extends ExpressionStatement<O,P,S>>
      extends StatementSource<O,P,S>,
      ExpressionHolder<O>
{

   /**
    * Returns the inner expression evaluated by this statement
    * @return the inner expression evaluated by this statement
    */
   public ExpressionSource<O,S,?> getExpr();

}
