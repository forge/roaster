package org.jboss.forge.test.parser.java;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.JavaClassSource;

public class NestedJavaClassTest extends JavaClassTest
{
   @Override
   public JavaClassSource getSource()
   {
      InputStream stream = JavaClassTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/NestedMockClass.java");
      return (JavaClassSource) JavaParser.parse(JavaClassSource.class, stream).getNestedClasses().get(0);
   }
}
