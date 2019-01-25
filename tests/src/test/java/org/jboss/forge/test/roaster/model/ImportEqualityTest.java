/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.impl.ImportImpl;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Test;

public class ImportEqualityTest
{

   private static final String DEFAULT_IMPORT_PACKAGE = "org.jboss.forge.roaster.model";
   private static final String DEFAULT_IMPORT_CLASS = "Import";
   private static final String DEFAULT_IMPORT = DEFAULT_IMPORT_PACKAGE + "." + DEFAULT_IMPORT_CLASS;

   @Test
   public void testImportEqualsAnotherInstance() 
   {
      Import firstImport = buildImport(DEFAULT_IMPORT);
      Import secondImport = buildImport(DEFAULT_IMPORT);
      assertEquals(firstImport, secondImport);
      assertEquals(secondImport, firstImport);
   }

   @Test
   public void testEqualsReturnsFalseForDifferentImport() 
   {
      Import classImport = buildImport(DEFAULT_IMPORT);
      Import interfaceImport = buildImport("org.jboss.forge.roaster.model.impl.ImportImpl");
      assertFalse(classImport.equals(interfaceImport));
   }

   @Test
   public void testEqualsIsReflexive() 
   {
      Import reflexiveImport = buildImport(DEFAULT_IMPORT);
      assertEquals(reflexiveImport, reflexiveImport);
   }

   @Test
   public void testEqualsIsTransitive() 
   {
      Import firstImport = buildImport(DEFAULT_IMPORT);
      Import secondImport = buildImport(DEFAULT_IMPORT);
      Import thirdImport = buildImport(DEFAULT_IMPORT);
      assertEquals(firstImport, secondImport);
      assertEquals(secondImport, thirdImport);
      assertEquals(firstImport, thirdImport);
   }

   @Test
   public void testNotEqualToNull() 
   {
      assertFalse(buildImport(DEFAULT_IMPORT).equals(null));
   }

   @Test
   @SuppressWarnings("unlikely-arg-type")
   public void testNotEqualsToDifferentClass() 
   {
      Import myImport = buildImport(DEFAULT_IMPORT);
      assertFalse(myImport.equals(DEFAULT_IMPORT));
   }

   @Test
   public void testEqualsToDifferentImportImplementation() 
   {
      Import myImport = buildImport(DEFAULT_IMPORT);
      Import mockImport = new MockImportImpl(DEFAULT_IMPORT_PACKAGE, DEFAULT_IMPORT_CLASS);
      assertEquals(myImport, mockImport);
   }

   private class MockImportImpl implements Import
   {

      private String packageName;
      private String simpleName;

      private MockImportImpl(String packageName, String simpleName)
      {
         this.packageName = packageName;
         this.simpleName = simpleName;
      }

      @Override
      public Object getInternal()
      {
         return null;
      }

      @Override
      public String getPackage()
      {
         return packageName;
      }

      @Override
      public String getSimpleName()
      {
         return simpleName;
      }

      @Override
      public String getQualifiedName()
      {
         return packageName + "." + simpleName;
      }

      @Override
      public Import setName(String name)
      {
         this.packageName = name.substring(0, name.lastIndexOf("."));
         this.simpleName = name.substring(name.lastIndexOf(".") + 1);
         return this;
      }

      @Override
      public boolean isStatic()
      {
         return false;
      }

      @Override
      public Import setStatic(boolean value)
      {
         return this;
      }

      @Override
      public boolean isWildcard()
      {
         return false;
      }

   }

   private Import buildImport(String importName)
   {
      return new ImportImpl(Roaster.parse(JavaSource.class, "public class MockClass {}")).setName(importName);
   }
}
