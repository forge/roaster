/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;

public enum Assignment
{

   ASSIGN("="),
   DIVIDE_ASSIGN("/="),
   PLUS_ASSIGN("+="),
   MINUS_ASSIGN("-="),
   TIMES_ASSIGN("*="),
   REMAINDER_ASSIGN("%="),

   LEFT_SHIFT_ASSIGN("<<="),
   RIGHT_SHIFT_ASSIGN(">>="),
   RIGHT_SHIFT_UNSIGNED_ASSIGN(">>>="),
   BITWISE_XOR_ASSIGN("^="),
   BITWISE_AND_ASSIGN("&="),
   BITWISE_OR_ASSIGN("|=");

   private String op;

   Assignment(String o)
   {
      op = o;
   }

   public String getOp()
   {
      return op;
   }


   public static Assignment build(String x)
   {
      for (Assignment en : Assignment.values())
      {
         if (en.getOp().equals(x))
         {
            return en;
         }
      }
      throw new IllegalStateException("Unrecognized operation " + x);
   }

}
