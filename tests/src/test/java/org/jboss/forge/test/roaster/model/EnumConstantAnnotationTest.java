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
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.test.roaster.model.common.AnnotationTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class EnumConstantAnnotationTest extends AnnotationTest<JavaEnumSource, EnumConstantSource>
{
   @Override
   public void resetTests() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockAnnotatedEnumConstant.java";
      try (InputStream stream = EnumConstantAnnotationTest.class.getResourceAsStream(fileName))
      {
         EnumConstantSource field = Roaster.parse(JavaEnumSource.class, stream).getEnumConstants().get(0);
         setTarget(field);
      }
   }
}