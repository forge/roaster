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
	public void testSupportsGenericsSourceFromConstructor() throws Exception {
		JavaSource<?> source = JavaParser.parse("public class Test{public Test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}}");
		Assert.assertFalse(source.hasSyntaxErrors());
	}

	@Test
	public void testSupportsGenericsSourceFromMethod() throws Exception {
		JavaClass source = JavaParser.parse(JavaClass.class,"public class Test{}");
		source.addMethod("public void test() {java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}}");
		Assert.assertFalse(source.hasSyntaxErrors());
	}
	
	@Test
   public void testSupportsGenericsSourceFromAddedConstructor() throws Exception {
      JavaClass source = JavaParser.parse(JavaClass.class, "public class Test{}");
      // Add a new method to get JDT to recognize the new ASTs
      source.addMethod().setConstructor(true).setBody("java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}");
      // Forces a rewrite to happen via AbstractJavaSource
      source.toString();
      Assert.assertFalse(source.hasSyntaxErrors());
   }
	
	@Test
   public void testSupportsGenericsSourceFromAddedMethod() throws Exception {
      JavaClass source = JavaParser.parse(JavaClass.class, "public class Test{}");
      // Add a new method to get JDT to recognize the new ASTs
      source.addMethod().setName("test").setBody("java.util.List<String> s = new java.util.ArrayList<String>(); for (String item : s){}");
      // Forces a rewrite to happen via AbstractJavaSource
      source.toString();
      Assert.assertFalse(source.hasSyntaxErrors());
   }
}
