package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.impl.MethodImpl;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:salemelrahal@gmail.com">Salem Elrahal</a>
 */
public class MethodModifierTest {

	   @Test
	   public void testDuplicateMethodModifier() throws Exception
	   {
		  MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public static void test()");
	      ((MethodImpl<?>)method).setStatic(true);
	      Assert.assertFalse(method.toString().contains("static static"));
	      Assert.assertTrue(method.toString().contains("static"));
	   }
}
