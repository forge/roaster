/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.spi.Streams;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
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
      Assert.assertEquals(contents, unit.toUnformattedString());
   }

   @Test
   public void testParseUnitStructure()
   {
      JavaUnit unit = Roaster.parseUnit(getFileContents());
      Assert.assertNotNull(unit);
      Assert.assertThat(unit.getGoverningType(), is(instanceOf(JavaClassSource.class)));
      JavaClassSource governingType = unit.getGoverningType();
      Assert.assertThat(unit.getTopLevelTypes().size(), is(2));
      Assert.assertSame(governingType, unit.getTopLevelTypes().get(0));
      Assert.assertThat(unit.getTopLevelTypes().get(1), is(instanceOf(JavaClassSource.class)));
      Assert.assertEquals(MAIN_CLASS_NAME, governingType.getName());
      Assert.assertEquals(SUB_CLASS_NAME, unit.getTopLevelTypes().get(1).getName());
   }
}
