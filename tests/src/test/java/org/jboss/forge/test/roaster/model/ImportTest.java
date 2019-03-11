package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportTest
{
   @Test
   public void testAddImportForSameClassNameDifferentPackageReturnsNull()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);
      final Import imprtOne = javaClassSource.addImport("packageOne.ClassOne");
      final Import imprtTwo = javaClassSource.addImport("packageTwo.ClassOne");

      assertThat(imprtOne).isNotNull();
      assertThat(imprtTwo).isNull();
   }

   @Test
   public void testWildCardImportReturnsCorrectData()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);
      String pckage = "packageOne.packageTwo";
      final Import imprt = javaClassSource.addImport(pckage + ".*");
      assertTrue(imprt.isWildcard());
      assertEquals(pckage, imprt.getPackage());
      assertEquals(Import.WILDCARD, imprt.getSimpleName());
      assertEquals(pckage + ".*", imprt.getQualifiedName());
   }

   @Test
   public void testGetSimpleName()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);

      assertEquals("Class2", javaClassSource.addImport("p1.Class2").getSimpleName());
      assertEquals("Class3", javaClassSource.addImport("p2.p3.Class3").getSimpleName());
      assertEquals(Import.WILDCARD, javaClassSource.addImport("p4.*").getSimpleName());
      assertEquals(Import.WILDCARD, javaClassSource.addImport("p5.p6.*").getSimpleName());
   }

   @Test
   public void testGetPackage()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);

      assertEquals("p1", javaClassSource.addImport("p1.Class2").getPackage());
      assertEquals("p2.p3", javaClassSource.addImport("p2.p3.Class3").getPackage());
      assertEquals("p4", javaClassSource.addImport("p4.*").getPackage());
      assertEquals("p5.p6", javaClassSource.addImport("p5.p6.*").getPackage());
   }

   @Test
   public void testGetQualifiedName()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);

      assertEquals("p1.Class2", javaClassSource.addImport("p1.Class2").getQualifiedName());
      assertEquals("p2.p3.Class3", javaClassSource.addImport("p2.p3.Class3").getQualifiedName());
      assertEquals("p4.*", javaClassSource.addImport("p4.*").getQualifiedName());
      assertEquals("p5.p6.*", javaClassSource.addImport("p5.p6.*").getQualifiedName());
   }
}