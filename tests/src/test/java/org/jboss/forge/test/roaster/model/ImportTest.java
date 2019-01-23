package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
   public void testWildCardImportReturnsCorrectData() {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);
      String pckage = "packageOne.packageTwo";
      final Import imprt = javaClassSource.addImport(pckage + ".*");
      assertThat(imprt.isWildcard(), is(true));
      assertThat(imprt.getPackage(), is(pckage));
      assertThat(imprt.getSimpleName(), is(Import.WILDCARD));
      assertThat(imprt.getQualifiedName(), is(pckage));
   }
}