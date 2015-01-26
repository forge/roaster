/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class FormatterProfileReaderTest
{
   /**
    * Test method for
    * {@link org.jboss.forge.roaster.model.util.FormatterProfileReader#fromEclipseXml(java.io.InputStream)}.
    */
   @Test
   public void testFormatterProfileReaderTwoProfiles() throws Exception
   {
      InputStream resource = getClass().getResourceAsStream("forge_profile.xml");
      FormatterProfileReader reader = FormatterProfileReader.fromEclipseXml(resource);
      Assert.assertNotNull(reader);
      Assert.assertNotNull(reader.getProfileNames());
      Assert.assertEquals(2, reader.getProfileNames().size());
      Assert.assertThat(reader.getProfileNames(), allOf(hasItem("Forge"), hasItem("Eclipse [built-in]")));
      Assert.assertNotNull(reader.getPropertiesFor("Forge"));
      Assert.assertNotNull(reader.getPropertiesFor("Eclipse [built-in]"));
      Assert.assertNull(reader.getPropertiesFor("Something Else"));
      Assert.assertNotNull(reader.getDefaultProperties());
   }

   @Test
   public void testFormatterProfileReaderOneProfile() throws Exception
   {
      InputStream resource = getClass().getResourceAsStream("eclipse_profile.xml");
      FormatterProfileReader reader = FormatterProfileReader.fromEclipseXml(resource);
      Assert.assertNotNull(reader);
      Assert.assertNotNull(reader.getProfileNames());
      Assert.assertEquals(1, reader.getProfileNames().size());
      Assert.assertThat(reader.getProfileNames(), hasItem("Eclipse [built-in]"));
      Assert.assertNotNull(reader.getPropertiesFor("Eclipse [built-in]"));
      Assert.assertNull(reader.getPropertiesFor("Something Else"));
      Assert.assertNotNull(reader.getDefaultProperties());
   }
}
