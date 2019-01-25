/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ResolveTypeTest
{
   @Test
   public void testResolveTypePrimitiveByte() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public byte get()");
      Assert.assertEquals("byte", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveShort() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public short get()");
      Assert.assertEquals("short", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveInt() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public int get()");
      Assert.assertEquals("int", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveLong() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public long get()");
      Assert.assertEquals("long", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveFloat() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public float get()");
      Assert.assertEquals("float", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveDouble() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public double get()");
      Assert.assertEquals("double", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveBoolean() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public boolean get()");
      Assert.assertEquals("boolean", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveChar() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public char get()");
      Assert.assertEquals("char", method.getReturnType().getQualifiedName());
   }

   /* Object Types */
   @Test
   public void testResolveTypeByte() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Byte get()");
      Assert.assertEquals("java.lang.Byte", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeShort() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Short get()");
      Assert.assertEquals("java.lang.Short", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeInt() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Integer get()");
      Assert.assertEquals("java.lang.Integer", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeLong() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Long get()");
      Assert.assertEquals("java.lang.Long", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeFloat() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Float get()");
      Assert.assertEquals("java.lang.Float", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeDouble() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Double get()");
      Assert.assertEquals("java.lang.Double", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeBoolean() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Boolean get()");
      Assert.assertEquals("java.lang.Boolean", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeChar() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public CharSequence get()");
      Assert.assertEquals("java.lang.CharSequence", method.getReturnType().getQualifiedName());
   }
}