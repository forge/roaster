/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl.expressions;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.jboss.forge.roaster.model.ExpressionHolder;
import org.jboss.forge.roaster.model.expressions.ClassLiteral;
import org.jboss.forge.roaster.model.impl.JDTHelper;
import org.jboss.forge.roaster.model.source.JavaSource;

public class ClassLiteralImpl<O extends JavaSource<O>,
      P extends ExpressionHolder<O>>
      extends AccessorImpl<O,P,ClassLiteral<O,P>,TypeLiteral>
      implements ClassLiteral<O,P>
{

   private String val;

   private TypeLiteral literal;

   public ClassLiteralImpl(String val)
   {
      this.val = val;
   }

   @Override
   public TypeLiteral materialize(AST ast)
   {
      if (isMaterialized())
      {
         return literal;
      }
      literal = ast.newTypeLiteral();
      literal.setType(JDTHelper.getType(val, ast));
      return literal;
   }

   @Override
   public TypeLiteral getInternal()
   {
      return literal;
   }

   @Override
   public void setInternal(TypeLiteral jdtNode)
   {
      super.setInternal(jdtNode);
      this.literal = jdtNode;
   }

   public String getValue()
   {
      return val;
   }
}
