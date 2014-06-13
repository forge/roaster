/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.Accessor;
import org.jboss.forge.roaster.model.expressions.ArrayIndexer;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.Expressions;
import org.jboss.forge.roaster.model.expressions.Field;
import org.jboss.forge.roaster.model.expressions.Getter;
import org.jboss.forge.roaster.model.expressions.MethodCallExpression;
import org.jboss.forge.roaster.model.expressions.NonPrimitiveExpression;
import org.jboss.forge.roaster.model.expressions.Setter;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.spi.ExpressionFactoryImpl;

public class DotAccessorImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      E extends NonPrimitiveExpression<O,P,E>>
      extends ExpressionImpl<O,P,E,org.eclipse.jdt.core.dom.Expression>
      implements Accessor<O,P,E>
{

   private ExpressionFactoryImpl<O,P,E,org.eclipse.jdt.core.dom.Expression> factory = new ExpressionFactoryImpl<O,P,E,org.eclipse.jdt.core.dom.Expression>();

   private ExpressionSource<O,P,?> owner;

   public DotAccessorImpl(ExpressionSource<O,P,?> parent)
   {
      this.owner = parent;
   }

   @Override
   public P getOrigin()
   {
      return null;
   }

   @Override
   public Field<O,E> field(String fieldName)
   {
      Field<O,E> field = factory.field(fieldName);
      swap(field, this.owner);
      return field;
   }

   @Override
   public Getter<O,E> getter(String fieldName, String type)
   {
      Getter<O,E> getter = factory.getter(fieldName, type);
      swap(getter, this.owner);
      return getter;
   }

   @Override
   public Getter<O,E> getter(String field, Class klass)
   {
      return getter(field, klass.getName());
   }

   @Override
   public Setter<O,E> setter(String fldName, String type, ExpressionSource<?,?,?> value)
   {
      Setter<O,E> setter = factory.setter(fldName, type, value);
      swap(setter, this.owner);
      return setter;
   }

   @Override
   public Setter<O,E> setter(String fldName, Class type, ExpressionSource<?,?,?> value)
   {
      return setter(fldName, type.getName(), value);
   }


    /*
    @Override
    public Variable<O,E> var( String varName ) {
        Variable<O,E> var = (Variable<O, E>) Expressions.var( varName );
        swap( var, this.owner );
        return var;
    }
    */


   @Override
   public MethodCallExpression<O,E> invoke(String method)
   {
      MethodCallExpression<O,E> invoke = (MethodCallExpression<O,E>) Expressions.invoke(method);
      swap(invoke, this.owner);
      return invoke;
   }

   @Override
   public ArrayIndexer<O,E> itemAt(ExpressionSource<?,?,?> index)
   {
      ArrayIndexer<O,E> accessor = factory.itemAt(index);
      swap(accessor, this.owner);
      return accessor;
   }


   private void swap(ExpressionHolder<O> originalChild, ExpressionSource<O,P,?> originalParent)
   {
      ExpressionChainLink<O,?> sub = (ExpressionChainLink<O,?>) originalChild;
      ExpressionChainLink<O,P> sup = (ExpressionChainLink<O,P>) originalParent;

      sub.linkParent(sup.getParent());
      sup.linkParent(originalChild);
      sub.chainExpression(originalParent);
   }

   @Override
   public org.eclipse.jdt.core.dom.Expression materialize(AST ast)
   {
      throw new UnsupportedOperationException("Method should not be called directly on this class");
   }

   @Override
   public org.eclipse.jdt.core.dom.Expression getInternal()
   {
      throw new UnsupportedOperationException("Method should not be called directly on this class");
   }

}
