/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

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
   public void resetTests()
   {
      InputStream stream = MethodAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedMethod.java");
      MethodSource<JavaClassSource> method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      setTarget(method);
      
      stream = MethodAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedMethodWithoutMockAnnotation.java");
      method = Roaster.parse(JavaClassSource.class, stream).getMethods().get(0);
      setTargetWithoutMockAnnotation(method);
   }
}
