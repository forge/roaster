/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.JavaSource;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumTest
{
   @Test
   public void testCanParseEnum() throws Exception
   {
      InputStream stream =
               JavaEnumTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockEnum.java");
      JavaSource<?> javaClass = JavaParser.parse(JavaEnum.class, stream);
      String name = javaClass.getName();
      assertEquals("MockEnum", name);
   }

}
