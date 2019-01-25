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
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.test.roaster.model.common.VisibilityTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldVisibilityTest extends VisibilityTest
{
   @Override
   public void resetTests() throws IOException
   {
      String fieldName = "/org/jboss/forge/grammar/java/MockAnnotatedField.java";
      try (InputStream stream = FieldVisibilityTest.class.getResourceAsStream(fieldName))
      {
         FieldSource<JavaClassSource> field = Roaster.parse(JavaClassSource.class, stream).getFields().get(0);
         setTarget(field);
      }
   }
}