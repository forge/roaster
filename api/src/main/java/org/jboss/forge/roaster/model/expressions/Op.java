/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;


public enum Op
{

   TIMES("*"),
   DIVIDE("/"),
   REMAINDER("%"),
   PLUS("+"),
   MINUS("-"),
   LEFT_SHIFT("<<"),
   RIGHT_SHIFT_SIGNED(">>"),
   RIGHT_SHIFT_UNSIGNED(">>>"),
   LESS("<"),
   GREATER(">"),
   LESS_EQUALS("<="),
   GREATER_EQUALS(">="),
   EQUALS("=="),
   NOT_EQUALS("!="),
   BITWISE_XOR("^"),
   BITWISE_AND("&"),
   BITWISE_OR("|"),
   OR("||"),
   AND("&&");

   private String op;

   Op(String o)
   {
      op = o;
   }

   public String getOp()
   {
      return op;
   }

   public static Op build(String x)
   {
      for (Op en : Op.values())
      {
         if (en.getOp().equals(x))
         {
            return en;
         }
      }
      throw new IllegalStateException("Unrecognized operation " + x);
   }
}
