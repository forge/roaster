/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.jboss.forge.parser.java.ReadMethod.Method;
import org.jboss.forge.parser.java.ReadParameter.Parameter;
import org.jboss.forge.test.parser.java.common.AnnotationTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ParameterAnnotationTest extends AnnotationTest<JavaClass, Parameter<JavaClass>>
{
   @Override
   public void resetTests()
   {
      InputStream stream = ParameterAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedParameter.java");
      Method<JavaClass> method = JavaParser.parse(JavaClass.class, stream).getMethods().get(0);
      setTarget(method.getParameters().get(0));
   }

   public void testNoAnnotations()
   {
      InputStream stream = ParameterAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedParameter.java");
      Method<JavaClass> method = JavaParser.parse(JavaClass.class, stream).getMethods().get(0);
      assertEquals(0, method.getParameters().get(1).getAnnotations().size());
   }
}
