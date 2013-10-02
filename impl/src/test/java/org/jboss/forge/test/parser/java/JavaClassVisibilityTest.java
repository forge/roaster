/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;
import org.jboss.forge.test.parser.java.common.VisibilityTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassVisibilityTest extends VisibilityTest
{
   @Override
   public void resetTests()
   {
      InputStream stream = JavaClassVisibilityTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockClass.java");
      JavaClass clazz = JavaParser.parse(JavaClass.class, stream);
      setTarget(clazz);
   }
}
