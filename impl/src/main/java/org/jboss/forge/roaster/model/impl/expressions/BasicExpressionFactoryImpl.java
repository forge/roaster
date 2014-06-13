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
import org.jboss.forge.roaster.model.expressions.Argument;
import org.jboss.forge.roaster.model.expressions.ArrayConstructorExpression;
import org.jboss.forge.roaster.model.expressions.ArrayInit;
import org.jboss.forge.roaster.model.expressions.AssignExpression;
import org.jboss.forge.roaster.model.expressions.Assignment;
import org.jboss.forge.roaster.model.expressions.BasicExpressionFactory;
import org.jboss.forge.roaster.model.expressions.BooleanLiteral;
import org.jboss.forge.roaster.model.expressions.CastExpression;
import org.jboss.forge.roaster.model.expressions.CharacterLiteral;
import org.jboss.forge.roaster.model.expressions.ClassLiteral;
import org.jboss.forge.roaster.model.expressions.ConstructorExpression;
import org.jboss.forge.roaster.model.expressions.DeclareExpression;
import org.jboss.forge.roaster.model.expressions.ExpressionSource;
import org.jboss.forge.roaster.model.expressions.InstanceofExpression;
import org.jboss.forge.roaster.model.expressions.Literal;
import org.jboss.forge.roaster.model.expressions.NullLiteral;
import org.jboss.forge.roaster.model.expressions.NumberLiteral;
import org.jboss.forge.roaster.model.expressions.Op;
import org.jboss.forge.roaster.model.expressions.OperatorExpression;
import org.jboss.forge.roaster.model.expressions.ParenExpression;
import org.jboss.forge.roaster.model.expressions.PrefixOp;
import org.jboss.forge.roaster.model.expressions.PrimitiveLiteral;
import org.jboss.forge.roaster.model.expressions.StringLiteral;
import org.jboss.forge.roaster.model.expressions.Super;
import org.jboss.forge.roaster.model.expressions.TernaryExpression;
import org.jboss.forge.roaster.model.expressions.ThisLiteral;
import org.jboss.forge.roaster.model.expressions.UnaryExpression;
import org.jboss.forge.roaster.model.expressions.Variable;
import org.jboss.forge.roaster.model.impl.NodeImpl;
import org.jboss.forge.roaster.model.source.JavaSource;

public class BasicExpressionFactoryImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>,
      J extends org.eclipse.jdt.core.dom.Expression>
      extends NodeImpl<O,P,J>
      implements BasicExpressionFactory<O,P>
{


   public BasicExpressionFactoryImpl()
   {
   }

   @Override
   public J materialize(AST ast)
   {
      throw new UnsupportedOperationException("Method should not be called directly on this class");
   }

   @Override
   public J getInternal()
   {
      throw new UnsupportedOperationException("Method should not be called directly on this class");
   }

   @Override
   public StringLiteral<O,P> literal(String val)
   {
      return new StringLiteralImpl<O,P>(val);
   }

   @Override
   public NumberLiteral<O,P> literal(Number val)
   {
      return new NumberLiteralImpl<O,P>(val);
   }

   @Override
   public CharacterLiteral<O,P> literal(Character val)
   {
      return new CharacterLiteralImpl<O,P>(val);
   }

   @Override
   public ClassLiteral<O,P> classLiteral(Class<?> klass)
   {
      return classLiteral(klass.getCanonicalName());
   }

   @Override
   public ClassLiteral<O,P> classLiteral(String klass)
   {
      return new ClassLiteralImpl<O,P>(klass);
   }

   @Override
   public BooleanLiteral<O,P> literal(Boolean val)
   {
      return new BooleanLiteralImpl<O,P>(val);
   }

   public Literal<O,P,?> zeroLiteral(Class klass)
   {
      return zeroLiteral(klass.getCanonicalName());
   }

   public Literal<O,P,?> zeroLiteral(String klass)
   {
      if (boolean.class.getName().equals(klass))
      {
         return literal(false);
      } else if (byte.class.getName().equals(klass))
      {
         return literal(0);
      } else if (char.class.getName().equals(klass))
      {
         return literal(0);
      } else if (double.class.getName().equals(klass))
      {
         return literal(0.0);
      } else if (float.class.getName().equals(klass))
      {
         return literal(0.0f);
      } else if (int.class.getName().equals(klass))
      {
         return literal(0);
      } else if (long.class.getName().equals(klass))
      {
         return literal(0L);
      } else if (short.class.getName().equals(klass))
      {
         return literal(0);
      } else
      {
         return nullLiteral();
      }
   }

   @Override
   public OperatorExpression<O,P> operator(Op op)
   {
      return new OperatorImpl<O,P>(op);
   }

   @Override
   public UnaryExpression<O,P> operator(PrefixOp op)
   {
      return new UnaryImpl<O,P>(op);
   }

   @Override
   public NullLiteral<O,P> nullLiteral()
   {
      return new NullLiteralImpl<O,P>();
   }

   @Override
   public UnaryExpression<O,P> not(ExpressionSource<O,?,?> arg)
   {
      return new NotImpl<O,P>(arg);
   }

   @Override
   public DeclareExpression declare(String klass, String name)
   {
      return new DeclareExpressionImpl<O,P>(klass, name);
   }

   @Override
   public DeclareExpression declare(Class klass, String name)
   {
      return declare(klass.getCanonicalName(), name);
   }

   @Override
   public DeclareExpression declare(String klass, String name, ExpressionSource<O,?,?> init)
   {
      return new DeclareExpressionImpl<O,P>(klass, name, init);
   }

   @Override
   public DeclareExpression declare(Class klass, String name, ExpressionSource<O,?,?> init)
   {
      return declare(klass.getCanonicalName(), name, init);
   }

   @Override
   public CastExpression<O,P> cast(String klass, ExpressionSource<O,?,?> arg)
   {
      CastExpression<O,P> expr = new CastImpl<O,P>(klass, arg);
      return expr;
   }

   @Override
   public CastExpression<O,P> cast(Class klass, ExpressionSource<O,?,?> arg)
   {
      return cast(klass.getCanonicalName(), arg);
   }

   @Override
   public ConstructorExpression<O,P> construct(String klass)
   {
      return new ConstructorImpl<O,P>(klass);
   }

   @Override
   public ConstructorExpression<O,P> construct(Class<?> klass)
   {
      return construct(klass.getCanonicalName());
   }

   @Override
   public ArrayConstructorExpression<O,P> newArray(String klass)
   {
      return new ArrayImpl<O,P>(klass);
   }

   @Override
   public ArrayConstructorExpression<O,P> newArray(Class<?> klass)
   {
      return newArray(klass.getCanonicalName());
   }

   @Override
   public Variable<O,P> var(String varName)
   {
      return new VarArgumentImpl<O,P>(varName);
   }

   @Override
   public AssignExpression<O,P> assign(Assignment operator)
   {
      return new AssignImpl<O,P>(operator);
   }

   @Override
   public ThisLiteral<O,P> thisLiteral()
   {
      return new SelfArgumentImpl<O,P>();
   }

   public ParenExpression<O,P> paren(ExpressionSource<O,?,?> child)
   {
      return new ParenImpl<O,P>(child);
   }

   @Override
   public TernaryExpression<O,P> ternary()
   {
      return new TernaryImpl<O,P>();
   }

   public Accessor<O,P,?> classStatic(String klass)
   {
      return new StaticClassAccessorImpl<O,P>(klass);
   }

   public Accessor<O,P,?> classStatic(Class klass)
   {
      return classStatic(klass.getCanonicalName());
   }

   @Override
   public InstanceofExpression<O,P> instanceOf(String klass, ExpressionSource<O,?,?> arg)
   {
      InstanceofExpression<O,P> expr = new InstanceofImpl<O,P>(klass, arg);
      return expr;
   }

   @Override
   public InstanceofExpression<O,P> instanceOf(Class klass, ExpressionSource<O,?,?> arg)
   {
      return instanceOf(klass.getCanonicalName(), arg);
   }

   @Override
   public Super<O,P> sup()
   {
      return new SuperImpl();
   }

   @Override
   public ArrayInit<O,ArrayConstructorExpression<O,P>> vec()
   {
      return new ArrayInitImpl<O,ArrayConstructorExpression<O,P>>();
   }

}
