/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.test.parser.java.common.VisibilityTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldVisibilityTest extends VisibilityTest
{
   @Override
   @SuppressWarnings("rawtypes")
   public void resetTests()
   {
      InputStream stream = FieldVisibilityTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockAnnotatedField.java");
      Field field = JavaParser.parse(JavaClass.class, stream).getFields().get(0);
      setTarget(field);
   }
}
