/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:asafbennatan@gmail.com">Asaf Ben Natan</a>
 */
public class ClassLoaderWildcardImportResolverTest
{
   private JavaClassSource javaClass;

   @BeforeEach
   public void reset() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockWildcardClass.java";
      try (InputStream stream = ClassLoaderWildcardImportResolverTest.class.getResourceAsStream(fileName))
      {
         javaClass = Roaster.parse(JavaClassSource.class, stream);
      }
   }

   @Test
   public void testType()
   {
      Assertions.assertEquals(List.class.getCanonicalName(),javaClass.getMethod("getList").getReturnType().getQualifiedName());

   }


}