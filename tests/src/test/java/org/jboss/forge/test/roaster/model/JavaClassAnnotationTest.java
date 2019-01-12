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
import org.jboss.forge.test.roaster.model.common.AnnotationTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JavaClassAnnotationTest extends AnnotationTest
{
   @Override
   public void resetTests()
   {
      InputStream stream = JavaClassAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedClass.java");
      JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, stream);
      setTarget(javaClass);
      
      stream = JavaClassAnnotationTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedClassWithoutMockAnnotation.java");
      javaClass = Roaster.parse(JavaClassSource.class, stream);
      setTargetWithoutMockAnnotation(javaClass);
   }
}
