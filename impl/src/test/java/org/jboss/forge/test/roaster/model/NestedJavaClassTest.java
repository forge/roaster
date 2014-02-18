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
      return (JavaClassSource) Roaster.parse(JavaClassSource.class, stream).getNestedClasses().get(0);
   }
}
