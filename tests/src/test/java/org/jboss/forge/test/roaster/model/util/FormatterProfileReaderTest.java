/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.model.util.FormatterProfileReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link FormatterProfileReader}
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class FormatterProfileReaderTest
{
   @Test
   public void testFormatterProfileReaderTwoProfiles() throws IOException
   {
      String fileName = "/org/jboss/forge/roaster/model/util/forge_profile.xml";
      try (InputStream resource = getClass().getResourceAsStream(fileName))
      {
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
   }

   @Test
   public void testFormatterProfileReaderOneProfile() throws IOException
   {
      String fileName = "/org/jboss/forge/roaster/model/util/eclipse_profile.xml";
      try (InputStream resource = getClass().getResourceAsStream(fileName))
      {
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
}