/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.test.roaster.model.common.AnnotationTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodAnnotationTest extends AnnotationTest<JavaClassSource, MethodSource<JavaClassSource>>
{
   @Override
   public void resetTests() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockAnnotatedMethod.java";
      try (InputStream stream = MethodAnnotationTest.class.getResourceAsStream(fileName))
      {
         MethodSource<JavaClassSource> method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
         setTarget(method);
      }
   }
}