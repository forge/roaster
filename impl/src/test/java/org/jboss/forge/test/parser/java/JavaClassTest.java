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
import org.jboss.forge.test.parser.java.common.JavaClassTestBase;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassTest extends JavaClassTestBase
{
   @Override
   public JavaClass getSource()
   {
      InputStream stream = JavaClassTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockClass.java");
      JavaClass javaClass = JavaParser.parse(JavaClass.class, stream);
      return javaClass;
   }
}
