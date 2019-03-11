/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.Streams;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaUnitTest
{

   private static final String MAIN_CLASS_NAME = "MockMultipleClassDeclarationsInAFile";
   private static final String SUB_CLASS_NAME = "MockAcompanyingClass";

   private InputStream getFileContents()
   {
      return JavaUnitTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/" + MAIN_CLASS_NAME + ".java");
   }

   @Test
   public void testParseUnitContents()
   {
      String contents = Streams.toString(getFileContents());
      JavaUnit unit = Roaster.parseUnit(contents);
      assertEquals(contents, unit.toUnformattedString());
   }

   @Test
   public void testParseUnitStructure()
   {
      JavaUnit unit = Roaster.parseUnit(getFileContents());
      assertNotNull(unit);
      assertThat((Object) unit.getGoverningType()).isInstanceOf(JavaClassSource.class);
      JavaClassSource governingType = unit.getGoverningType();
      assertEquals(2, unit.getTopLevelTypes().size());
      assertSame(governingType, unit.getTopLevelTypes().get(0));
      assertThat(unit.getTopLevelTypes().get(1)).isInstanceOf(JavaClassSource.class);
      assertEquals(MAIN_CLASS_NAME, governingType.getName());
      assertEquals(SUB_CLASS_NAME, unit.getTopLevelTypes().get(1).getName());
   }
}
