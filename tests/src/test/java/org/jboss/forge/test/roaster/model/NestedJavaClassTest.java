/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class NestedJavaClassTest extends JavaClassTest
{
   @Override
   public JavaClassSource getSource()
   {
      InputStream stream = JavaClassTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/NestedMockClass.java");
      return (JavaClassSource) Roaster.parse(JavaClassSource.class, stream).getNestedTypes().get(0);
   }
}
