/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.forge.parser.java.VisibilityScoped;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class VisibilityTest
{
   private VisibilityScoped<?> target;

   public void setTarget(VisibilityScoped<?> target)
   {
      this.target = target;
   }

   @Before
   public void reset()
   {
      resetTests();
   }

   public abstract void resetTests();

   @Test
   public void testSetPublic() throws Exception
   {
      target.setPublic();
      assertTrue(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertFalse(target.isProtected());
   }

   @Test
   public void testSetPrivate() throws Exception
   {
      target.setPrivate();
      assertFalse(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertTrue(target.isPrivate());
      assertFalse(target.isProtected());
   }

   @Test
   public void testSetProtected() throws Exception
   {
      target.setProtected();
      assertFalse(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertTrue(target.isProtected());
   }

   @Test
   public void testSetPackagePrivate() throws Exception
   {
      target.setPackagePrivate();
      assertFalse(target.isPublic());
      assertTrue(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertFalse(target.isProtected());
   }
}
