package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.ReadJavaClass.JavaClass;

public class NestedJavaClassTest extends JavaClassTest
{
   @Override
   public JavaClass getSource()
   {
      InputStream stream = JavaClassTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/NestedMockClass.java");
      return (JavaClass) JavaParser.parse(JavaClass.class, stream).getNestedClasses().get(0);
   }
}
