/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jboss.forge.roaster.model.source.VisibilityScopedSource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class VisibilityTest
{
   private VisibilityScopedSource<?> target;

   public void setTarget(VisibilityScopedSource<?> target)
   {
      this.target = target;
   }

   @Before
   public void reset() throws IOException
   {
      resetTests();
   }

   public abstract void resetTests() throws IOException;

   @Test
   public void testSetPublic()
   {
      target.setPublic();
      assertTrue(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertFalse(target.isProtected());
   }

   @Test
   public void testSetPrivate()
   {
      target.setPrivate();
      assertFalse(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertTrue(target.isPrivate());
      assertFalse(target.isProtected());
   }

   @Test
   public void testSetProtected()
   {
      target.setProtected();
      assertFalse(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertTrue(target.isProtected());
   }

   @Test
   public void testSetPackagePrivate()
   {
      target.setPackagePrivate();
      assertFalse(target.isPublic());
      assertTrue(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertFalse(target.isProtected());
   }
}