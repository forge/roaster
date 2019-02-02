/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import static org.junit.Assert.assertArrayEquals;

import java.awt.List;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import javax.swing.UIManager;

import org.jboss.forge.roaster.model.util.Methods;
import org.junit.Test;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class MethodsTest
{

   @Test
   public void testGenerateParametersNamesWithDifferentCases()
   {
      String[] paramNames = Methods
               .generateParameterNames(new Class[] { InputStream.class, UIManager.class, myclass.class });
      assertArrayEquals(new String[] { "inputStream", "uiManager", "myclass" }, paramNames);
   }

   @Test
   public void testGenerateParametersNamesWithSameTypes()
   {
      String[] paramNames = Methods.generateParameterNames(new Class[] { List.class, List.class });
      assertArrayEquals(new String[] { "list", "list1" }, paramNames);
   }

   @Test
   public void testGenerateParametersNamesWithDifferentTypes()
   {
      String[] paramNames = Methods.generateParameterNames(new Class[] { Map.class, List.class });
      assertArrayEquals(new String[] { "map", "list" }, paramNames);
   }

   @Test
   public void testGenerateParametersNamesWithString()
   {
      String[] paramNames = Methods
               .generateParameterNames(new Class[] { String.class, Date.class, Object.class, List.class });
      assertArrayEquals(new String[] { "s", "date", "o", "list" }, paramNames);
   }

   private static class myclass
   {
      // empty test class
   }
}