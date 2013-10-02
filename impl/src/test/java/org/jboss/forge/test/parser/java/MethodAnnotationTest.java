/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.jboss.forge.parser.java.source.MethodSource;
import org.jboss.forge.test.parser.java.common.AnnotationTest;

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
      MethodSource<JavaClassSource> method = JavaParser.parse(JavaClassSource.class, stream).getMethods().get(0);
      setTarget(method);
   }
}
