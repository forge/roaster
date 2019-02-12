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
   public void testWildCardImportReturnsCorrectData()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);
      String pckage = "packageOne.packageTwo";
      final Import imprt = javaClassSource.addImport(pckage + ".*");
      assertThat(imprt.isWildcard(), is(true));
      assertThat(imprt.getPackage(), is(pckage));
      assertThat(imprt.getSimpleName(), is(Import.WILDCARD));
      assertThat(imprt.getQualifiedName(), is(pckage + ".*"));
   }

   @Test
   public void testGetSimpleName()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);

      assertThat(javaClassSource.addImport("p1.Class2").getSimpleName(), is("Class2"));
      assertThat(javaClassSource.addImport("p2.p3.Class3").getSimpleName(), is("Class3"));
      assertThat(javaClassSource.addImport("p4.*").getSimpleName(), is(Import.WILDCARD));
      assertThat(javaClassSource.addImport("p5.p6.*").getSimpleName(), is(Import.WILDCARD));
   }

   @Test
   public void testGetPackage()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);

      assertThat(javaClassSource.addImport("p1.Class2").getPackage(), is("p1"));
      assertThat(javaClassSource.addImport("p2.p3.Class3").getPackage(), is("p2.p3"));
      assertThat(javaClassSource.addImport("p4.*").getPackage(), is("p4"));
      assertThat(javaClassSource.addImport("p5.p6.*").getPackage(), is("p5.p6"));
   }

   @Test
   public void testGetQualifiedName()
   {
      final JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);

      assertThat(javaClassSource.addImport("p1.Class2").getQualifiedName(), is("p1.Class2"));
      assertThat(javaClassSource.addImport("p2.p3.Class3").getQualifiedName(), is("p2.p3.Class3"));
      assertThat(javaClassSource.addImport("p4.*").getQualifiedName(), is("p4.*"));
      assertThat(javaClassSource.addImport("p5.p6.*").getQualifiedName(), is("p5.p6.*"));
   }
}