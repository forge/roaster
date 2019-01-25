/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.test.roaster.model.common.AnnotationTest;
import org.jboss.forge.test.roaster.model.common.MockEnumType;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldAnnotationTest extends AnnotationTest<JavaClassSource, FieldSource<JavaClassSource>>
{
   @Override
   public void resetTests() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockAnnotatedField.java";
      try (InputStream stream = FieldAnnotationTest.class.getResourceAsStream(fileName))
      {
         FieldSource<JavaClassSource> field = Roaster.parse(JavaClassSource.class, stream).getFields().get(0);
         setTarget(field);
      }
   }

   @Test
   public void testParseEnumValueStaticImport()
   {
      List<AnnotationSource<JavaClassSource>> annotations = getTarget().getAnnotations();
      AnnotationSource<JavaClassSource> annotation = annotations.get(3);
      MockEnumType enumValue = annotation.getEnumValue(MockEnumType.class);
      assertEquals(MockEnumType.FOO, enumValue);
   }
}
