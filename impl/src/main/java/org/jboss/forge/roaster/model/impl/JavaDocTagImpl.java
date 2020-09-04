/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import org.eclipse.jdt.core.dom.TagElement;
import org.jboss.forge.roaster.model.JavaDoc;
import org.jboss.forge.roaster.model.JavaDocTag;

/**
 * A {@link JavaDocTag} contained in a {@link JavaDoc} element
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaDocTagImpl implements JavaDocTag
{
   private final TagElement tagElement;

   public JavaDocTagImpl(TagElement tagElement)
   {
      this.tagElement = tagElement;
   }

   @Override
   public String getName()
   {
      return tagElement.getTagName();
   }

   @Override
   public String getValue()
   {
      StringBuilder sb = new StringBuilder();
      for (Object fragment : tagElement.fragments())
      {
         sb.append(fragment);
         sb.append(" ");
      }
      return sb.toString().trim();
   }

   @Override
   public Object getInternal()
   {
      return tagElement;
   }
}
