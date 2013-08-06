package org.jboss.forge.test.parser.java;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.junit.Assert;
import org.junit.Test;

public class JavaSourceCompatibilityTest {

	@Test
	public void testSupportsGenericsSource() throws Exception {
		JavaSource<?> source = JavaParser.parse("public class Test{public void test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}}");
		Assert.assertFalse(source.hasSyntaxErrors());
	}
	
	@Test
	public void testSupportsGenericsSourceFromMethod() throws Exception {
		JavaClass source = JavaParser.parse(JavaClass.class,"public class Test{}");
		source.addMethod("public void test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}");
		Assert.assertFalse(source.hasSyntaxErrors());
	}
}
