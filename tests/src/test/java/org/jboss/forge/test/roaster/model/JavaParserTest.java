/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaParserTest
{
   @Test(expected = ParserException.class)
   public void testExceptionWhenBadFileParsed()
   {
      Roaster.parse(JavaClass.class, "asdfa$%(*&#$%sdfdsf");
   }
}