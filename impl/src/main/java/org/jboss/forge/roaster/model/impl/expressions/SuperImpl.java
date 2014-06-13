/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.expressions.Super;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactoryImpl;

public class SuperImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends ExpressionFactoryImpl<O,P,Super<O,P>,org.eclipse.jdt.core.dom.Expression>
      implements Super<O,P>
{

   @Override
   public Field<O,Super<O,P>> field(String fieldName)
   {
      return new SuperFieldImpl<O,Super<O,P>>(fieldName);

   }

   @Override
   public Getter<O,Super<O,P>> getter(String field, String klass)
   {
      return new SuperGetterImpl<O,Super<O,P>>(field, klass);
   }

   @Override
   public Getter<O,Super<O,P>> getter(String field, Class klass)
   {
      return getter(field, klass.getName());
   }

   @Override
   public MethodCallExpression<O,Super<O,P>> invoke(String method)
   {
      return new SuperMethodInvokeImpl<O,Super<O,P>>(method);
   }

   @Override
   public Setter<O,Super<O,P>> setter(String fldName, Class type, ExpressionSource<?,?,?> value)
   {
      return setter(fldName, type.getName(), value);
   }

   @Override
   public Setter<O,Super<O,P>> setter(String fldName, String type, ExpressionSource<?,?,?> value)
   {
      return new SuperSetterImpl<O,Super<O,P>>(fldName, type, value);
   }
}
