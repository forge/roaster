/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.parser.java;

import org.jboss.forge.parser.java.ReadVisibilityScoped.VisibilityScoped;
import org.jboss.forge.parser.java.util.Assert;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public enum Visibility
{
   PUBLIC("public"),
   PROTECTED("protected"),
   PRIVATE("private"),
   PACKAGE_PRIVATE("");

   private Visibility(String scope)
   {
      this.scope = scope;
   }

   private String scope;

   /**
    * private, public, protected, package private("")
    */
   public String scope()
   {
      return scope;
   }

   public static Visibility getFrom(ReadVisibilityScoped target)
   {
      Assert.notNull(target, "VisibilityScoped<T> target must not be null.");

      if (target.isPackagePrivate())
         return PACKAGE_PRIVATE;
      if (target.isPrivate())
         return PRIVATE;
      if (target.isPublic())
         return PUBLIC;
      if (target.isProtected())
         return PROTECTED;

      else
      {
         throw new IllegalStateException(ReadVisibilityScoped.class.getSimpleName()
                  + " target does not comply with visibility scoping. Must be one of " + Visibility.values() + "[ "
                  + target + "]");
      }
   }

   @Override
   public String toString()
   {
      return scope;
   }

   public static <T extends VisibilityScoped<?>> T set(T target, Visibility scope)
   {
      Assert.notNull(target, "VisibilityScoped<T> target must not be null.");
      Assert.notNull(scope, "Visibility scope must not be null");

      switch (scope)
      {
      case PRIVATE:
         target.setPrivate();
         break;
      case PACKAGE_PRIVATE:
         target.setPackagePrivate();
         break;
      case PROTECTED:
         target.setProtected();
         break;
      case PUBLIC:
         target.setPublic();
         break;
      default:
         throw new IllegalStateException("Unknown Visibility scope.");
      }
      return target;
   }
}
