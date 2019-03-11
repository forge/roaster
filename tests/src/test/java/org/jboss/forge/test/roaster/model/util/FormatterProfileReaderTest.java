/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model.util;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.model.util.FormatterProfileReader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
         assertNotNull(reader);
         assertNotNull(reader.getProfileNames());
         assertEquals(2, reader.getProfileNames().size());
         assertThat(reader.getProfileNames()).contains("Forge", "Eclipse [built-in]");
         assertNotNull(reader.getPropertiesFor("Forge"));
         assertNotNull(reader.getPropertiesFor("Eclipse [built-in]"));
         assertNull(reader.getPropertiesFor("Something Else"));
         assertNotNull(reader.getDefaultProperties());
      }
   }

   @Test
   public void testFormatterProfileReaderOneProfile() throws IOException
   {
      String fileName = "/org/jboss/forge/roaster/model/util/eclipse_profile.xml";
      try (InputStream resource = getClass().getResourceAsStream(fileName))
      {
         FormatterProfileReader reader = FormatterProfileReader.fromEclipseXml(resource);
         assertNotNull(reader);
         assertNotNull(reader.getProfileNames());
         assertEquals(1, reader.getProfileNames().size());
         assertThat(reader.getProfileNames()).contains("Eclipse [built-in]");
         assertNotNull(reader.getPropertiesFor("Eclipse [built-in]"));
         assertNull(reader.getPropertiesFor("Something Else"));
         assertNotNull(reader.getDefaultProperties());
      }
   }
}