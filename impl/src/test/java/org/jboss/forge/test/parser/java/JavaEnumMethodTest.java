/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.Member;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.parser.java.Parameter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumMethodTest
{
   private InputStream stream;
   private JavaEnum javaEnum;
   private Method<JavaEnum> method;

   @Before
   public void reset()
   {
      stream = JavaEnumMethodTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockEnum.java");
      javaEnum = JavaParser.parse(JavaEnum.class, stream);
      javaEnum.addMethod("public URL rewriteURL(String pattern, String replacement) { return null; }");
      method = javaEnum.getMethods().get(javaEnum.getMethods().size() - 1);
   }

   @Test
   public void testSetName() throws Exception
   {
      assertEquals("rewriteURL", method.getName());
      method.setName("doSomething");
      assertEquals("doSomething", method.getName());
   }

   @Test
   public void testSetReturnType() throws Exception
   {
      assertEquals("java.net.URL", method.getQualifiedReturnType());
      method.setReturnType(Class.class);
      assertEquals("Class", method.getReturnType());
      assertFalse(method.isReturnTypeVoid());
   }

   @Test
   public void testSetReturnTypeVoid() throws Exception
   {
      assertEquals("java.net.URL", method.getQualifiedReturnType());
      method.setReturnTypeVoid();
      assertEquals(null, method.getReturnType());
      assertTrue(method.isReturnTypeVoid());
   }

   @Test
   public void testSetConstructor() throws Exception
   {
      assertFalse(method.isConstructor());
      method.setConstructor(true);
      assertTrue(method.isConstructor());
      assertEquals(javaEnum.getName(), method.getName());
   }

   @Test
   public void testSetAbstract() throws Exception
   {
      method.setAbstract(true);
      assertTrue(method.isAbstract());
   }

   @Test
   public void testSetParameters() throws Exception
   {
      method.setParameters("final int foo, final String bar");
      List<Parameter<JavaEnum>> parameters = method.getParameters();
      assertEquals(2, parameters.size());
      assertEquals("foo", parameters.get(0).getName());
      assertEquals("bar", parameters.get(1).getName());
   }

   @Test
   public void testGetParameterType() throws Exception
   {
      method.setParameters("final int foo, final String bar");
      List<Parameter<JavaEnum>> parameters = method.getParameters();
      assertEquals(2, parameters.size());
      assertEquals("int", parameters.get(0).getType());
      assertEquals("String", parameters.get(1).getType());
      assertFalse(javaEnum.hasMethodSignature(method.getName()));
   }

   @Test
   public void testHasMethodZeroParametersIgnoresMethodWithParameters() throws Exception
   {
      assertTrue(javaEnum.hasMethodSignature(method));
      assertFalse(javaEnum.hasMethodSignature(method.getName()));
   }

   @Test
   public void testHasMethodZeroParameters() throws Exception
   {
      javaEnum.addMethod("public void doSomething(){/*done*/}");
      assertTrue(javaEnum.hasMethodSignature("doSomething"));
   }

   @Test
   public void testGetMembers() throws Exception
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class).addMethod("public void doSomething();").getOrigin()
               .addField("private int id;").getOrigin();
      List<Member<JavaClass, ?>> members = javaClass.getMembers();
      assertEquals(2, members.size());
   }
}
