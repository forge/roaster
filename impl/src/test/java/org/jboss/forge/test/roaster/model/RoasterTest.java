package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Test;

public class RoasterTest
{

   @Test
   public void test()
   {
      JavaInterfaceSource javaInterface = Roaster.create(JavaInterfaceSource.class);
      MethodSource<JavaInterfaceSource> method = javaInterface.addMethod();
      method.addAnnotation("annotationMethod");
      method.addThrows("foo.exception");
      method.addTypeVariable("typeVariable");
      method.setDefault(true);
      method.setFinal(true);
      method.setReturnType(String.class).addAnnotation("annotationRetour");
      method.addParameter("foo.paramType1", "paramName1").addAnnotation("annotationParam1");
      javaInterface.setPackage("foo");

      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.implementInterface(javaInterface);
      
   }

}
