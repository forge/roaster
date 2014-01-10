package org.jboss.forge.test.parser.java;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.parser.java.impl.MethodImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:salemelrahal@gmail.com">Salem Elrahal</a>
 */
public class MethodModifierTest
{

   @Test
   public void testDuplicateMethodModifier() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public static void test()");
      ((MethodImpl<?>) method).setStatic(true);
      Assert.assertFalse(method.toString().contains("static static"));
      Assert.assertTrue(method.toString().contains("static"));
   }
}
