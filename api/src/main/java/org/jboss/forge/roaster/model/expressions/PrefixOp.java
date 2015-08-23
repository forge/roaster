/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.expressions;


public enum PrefixOp
{

   INC("++"),
   DEC("--"),
   PLUS("+"),
   MINUS("-"),
   NOT("!"),
   NEG("~");

   private String op;

   PrefixOp(String o)
   {
      op = o;
   }

   public String getOp()
   {
      return op;
   }

   public static PrefixOp build(String x)
   {
      for (PrefixOp en : PrefixOp.values())
      {
         if (en.getOp().equals(x))
         {
            return en;
         }
      }
      throw new IllegalStateException("Unrecognized operation " + x);
   }
}
