/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.test.roaster.model.common.AnnotationTest;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ParameterAnnotationTest extends AnnotationTest<JavaClassSource, ParameterSource<JavaClassSource>>
{
   @Override
   public void resetTests()
   {
      InputStream stream = ParameterAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedParameter.java");
      MethodSource<JavaClassSource> method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      setTarget(method.getParameters().get(0));
      
      stream = ParameterAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedParameterWithoutMockAnnotation.java");
      method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      setTargetWithoutMockAnnotation(method.getParameters().get(0));
   }

   @Test
   public void testNoAnnotations()
   {
      InputStream stream = ParameterAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedParameter.java");
      MethodSource<JavaClassSource> method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      assertEquals(0, method.getParameters().get(1).getAnnotations().size());
   }
}
