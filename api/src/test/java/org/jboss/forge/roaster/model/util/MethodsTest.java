/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.List;
import java.util.Arrays;
import java.util.Map;

import org.hamcrest.CoreMatchers;
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
   public void testParametersSameTypes()
   {
      String[] paramNames = Methods.generateParameterNames(new Class[] { List.class, List.class });
      assertThat(Arrays.asList(paramNames), CoreMatchers.hasItems("list", "list1"));
   }

   @Test
   public void testParametersDifferentTypes()
   {
      String[] paramNames = Methods.generateParameterNames(new Class[] { Map.class, List.class });
      assertThat(Arrays.asList(paramNames), CoreMatchers.hasItems("map", "list"));
   }

}
