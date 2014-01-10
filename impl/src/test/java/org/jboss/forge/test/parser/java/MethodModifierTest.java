package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.impl.MethodImpl;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.jboss.forge.parser.java.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:salemelrahal@gmail.com">Salem Elrahal</a>
 */
public class MethodModifierTest {
	
	   @Test
	   public void testDuplicateMethodModifier() throws Exception
	   {
		  MethodSource<JavaClassSource> method = JavaParser.create(JavaClassSource.class).addMethod("public static void test()");
	      ((MethodImpl<?>)method).setStatic(true);
	      Assert.assertFalse(method.toString().contains("static static"));
	      Assert.assertTrue(method.toString().contains("static"));
	   }
}
