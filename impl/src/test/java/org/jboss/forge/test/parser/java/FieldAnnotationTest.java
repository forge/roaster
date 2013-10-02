/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadAnnotation.Annotation;
import org.jboss.forge.parser.java.ReadField.Field;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.jboss.forge.test.parser.java.common.AnnotationTest;
import org.jboss.forge.test.parser.java.common.MockEnumType;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldAnnotationTest extends AnnotationTest<JavaClass, Field<JavaClass>>
{
   @Override
   public void resetTests()
   {
      InputStream stream = FieldAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedField.java");
      Field<JavaClass> field = JavaParser.parse(JavaClass.class, stream).getFields().get(0);
      setTarget(field);
   }

   @Test
   public void testParseEnumValueStaticImport() throws Exception
   {
      List<Annotation<JavaClass>> annotations = getTarget().getAnnotations();
      Annotation<JavaClass> annotation = annotations.get(annotations.size() - 2);
      MockEnumType enumValue = annotation.getEnumValue(MockEnumType.class);
      assertEquals(MockEnumType.FOO, enumValue);
   }
}
