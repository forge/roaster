/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.grammar.java;

import java.net.URL;

@SuppressWarnings
public enum MockEnum
{
   FOO, BAR, BAZ;

   private String field;

   private MockEnum()
   {
   }

   String getName()
   {
      return name();
   }
}
