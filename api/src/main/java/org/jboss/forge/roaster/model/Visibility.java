/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.model.source.VisibilityScopedSource;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public enum Visibility
{
   PUBLIC("public"), PROTECTED("protected"), PRIVATE("private"), PACKAGE_PRIVATE("");

   Visibility(String scope)
   {
      this.scope = scope;
   }

   private final String scope;

   /**
    * private, public, protected, package private("")
    */
   public String scope()
   {
      return scope;
   }

   public static Visibility getFrom(VisibilityScoped target)
   {
      requireNonNull(target, "VisibilityScoped<T> target must not be null.");

      if (target.isPackagePrivate())
      {
         return PACKAGE_PRIVATE;
      }
      if (target.isPrivate())
      {
         return PRIVATE;
      }
      if (target.isPublic())
      {
         return PUBLIC;
      }
      if (target.isProtected())
      {
         return PROTECTED;
      }
      throw new IllegalStateException(VisibilityScoped.class.getSimpleName()
               + " target does not comply with visibility scoping. Must be one of "
               + Arrays.toString(Visibility.values()) + "[ "
               + target + "]");
   }

   @Override
   public String toString()
   {
      return scope;
   }

   public static <T extends VisibilityScopedSource<?>> T set(T target, Visibility scope)
   {
      requireNonNull(target, "VisibilityScoped<T> target must not be null.");
      requireNonNull(scope, "Visibility scope must not be null");

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
