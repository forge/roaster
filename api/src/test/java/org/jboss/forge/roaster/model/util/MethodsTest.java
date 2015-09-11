/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.List;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class MethodsTest
{

   @Test
   public void testParamNameSingleUpperCase()
   {
      assertEquals("facesContext", Methods.toParamName("FacesContext"));
   }

   @Test
   public void testParamNameMultipleUpperCase()
   {
      assertEquals("uiComponent", Methods.toParamName("UIComponent"));
   }

   @Test
   public void testParamNameAllLowerCase()
   {
      assertEquals("myclass", Methods.toParamName("myclass"));
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
}
