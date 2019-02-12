/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import org.jboss.forge.roaster.model.ValuePair;

/**
 * Represents an annotation value pair
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ValuePairImpl implements ValuePair
{
   private final String name;
   private final String value;

   public ValuePairImpl(String name, String value)
   {
      this.name = name;
      this.value = value;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public String getLiteralValue()
   {
      return value;
   }

   @Override
   public String getStringValue()
   {
      return Strings.unquote(getLiteralValue());
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      ValuePairImpl other = (ValuePairImpl) obj;
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      if (value == null)
      {
         if (other.value != null)
         {
            return false;
         }
      }
      else if (!value.equals(other.value))
      {
         return false;
      }
      return true;
   }

}
