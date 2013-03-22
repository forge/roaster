/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.test.parser.java.common.VisibilityTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodVisibilityTest extends VisibilityTest
{
   @Override
   @SuppressWarnings("rawtypes")
   public void resetTests()
   {
      InputStream stream = MethodVisibilityTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedMethod.java");
      Method method = JavaParser.parse(JavaClass.class, stream).getMethods().get(0);
      setTarget(method);
   }
}
