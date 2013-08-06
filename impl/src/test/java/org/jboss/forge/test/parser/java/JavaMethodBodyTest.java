package org.jboss.forge.test.parser.java;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaSource;
import org.junit.Assert;
import org.junit.Test;

public class JavaMethodBodyTest {

	@Test
	public void testSupportsGenericsSource() throws Exception {
		JavaSource<?> source = JavaParser.parse("public class Test{public void test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}}");
		Assert.assertFalse(source.hasSyntaxErrors());
	}
}
