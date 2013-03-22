/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Method;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ResolveTypeTest
{
   @Test
   public void testResolveTypePrimitiveByte() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public byte get()");
      Assert.assertEquals("byte", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveShort() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public short get()");
      Assert.assertEquals("short", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveInt() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public int get()");
      Assert.assertEquals("int", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveLong() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public long get()");
      Assert.assertEquals("long", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveFloat() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public float get()");
      Assert.assertEquals("float", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveDouble() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public double get()");
      Assert.assertEquals("double", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveBoolean() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public boolean get()");
      Assert.assertEquals("boolean", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypePrimitiveChar() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public char get()");
      Assert.assertEquals("char", method.getQualifiedReturnType());
   }

   /* Object Types */
   @Test
   public void testResolveTypeByte() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Byte get()");
      Assert.assertEquals("java.lang.Byte", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeShort() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Short get()");
      Assert.assertEquals("java.lang.Short", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeInt() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Integer get()");
      Assert.assertEquals("java.lang.Integer", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeLong() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Long get()");
      Assert.assertEquals("java.lang.Long", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeFloat() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Float get()");
      Assert.assertEquals("java.lang.Float", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeDouble() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Double get()");
      Assert.assertEquals("java.lang.Double", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeBoolean() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public Boolean get()");
      Assert.assertEquals("java.lang.Boolean", method.getQualifiedReturnType());
   }

   @Test
   public void testResolveTypeChar() throws Exception
   {
      Method<JavaClass> method = JavaParser.create(JavaClass.class).addMethod("public CharSequence get()");
      Assert.assertEquals("java.lang.CharSequence", method.getQualifiedReturnType());
   }

}
