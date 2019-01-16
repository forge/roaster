package org.jboss.forge.test.roaster.model;

import org.assertj.core.api.Assertions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

}
