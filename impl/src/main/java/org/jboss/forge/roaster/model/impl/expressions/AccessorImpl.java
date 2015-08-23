/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.NonPrimitiveExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class AccessorImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends NonPrimitiveExpression<O,P,E>,
      J extends org.eclipse.jdt.core.dom.Expression>
      extends NonPrimitiveExpressionImpl<O,P,E,J>
      implements Accessor<O,P,E>
{

   public Field<O,E> field(String fieldName)
   {
      return new DotAccessorImpl<O,P,E>(this).field(fieldName);
   }

   public Getter<O,E> getter(String field, String klass)
   {
      return new DotAccessorImpl<O,P,E>(this).getter(field, klass);
   }

   public Getter<O,E> getter(String field, Class klass)
   {
      return new DotAccessorImpl<O,P,E>(this).getter(field, klass);
   }

   public Setter<O,E> setter(String fldName, String type, ExpressionSource<?,?,?> value)
   {
      return new DotAccessorImpl<O,P,E>(this).setter(fldName, type, value);
   }

   public Setter<O,E> setter(String fldName, Class type, ExpressionSource<?,?,?> value)
   {
      return new DotAccessorImpl<O,P,E>(this).setter(fldName, type, value);
   }

   public MethodCallExpression<O,E> invoke(String method)
   {
      return new DotAccessorImpl<O,P,E>(this).invoke(method);
   }

   @Override
   public ArrayIndexer<O,E> itemAt(ExpressionSource<?,?,?> index)
   {
      return new DotAccessorImpl<O,P,E>(this).itemAt(index);
   }
}
