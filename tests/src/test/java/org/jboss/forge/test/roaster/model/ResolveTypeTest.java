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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ResolveTypeTest
{
   @Test
   public void testResolveTypePrimitiveByte() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public byte get()");
      assertEquals("byte", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveShort() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public short get()");
      assertEquals("short", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveInt() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public int get()");
      assertEquals("int", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveLong() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public long get()");
      assertEquals("long", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveFloat() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public float get()");
      assertEquals("float", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveDouble() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public double get()");
      assertEquals("double", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveBoolean() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public boolean get()");
      assertEquals("boolean", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypePrimitiveChar() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public char get()");
      assertEquals("char", method.getReturnType().getQualifiedName());
   }

   /* Object Types */
   @Test
   public void testResolveTypeByte() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Byte get()");
      assertEquals("java.lang.Byte", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeShort() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Short get()");
      assertEquals("java.lang.Short", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeInt() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Integer get()");
      assertEquals("java.lang.Integer", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeLong() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Long get()");
      assertEquals("java.lang.Long", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeFloat() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Float get()");
      assertEquals("java.lang.Float", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeDouble() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Double get()");
      assertEquals("java.lang.Double", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeBoolean() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod("public Boolean get()");
      assertEquals("java.lang.Boolean", method.getReturnType().getQualifiedName());
   }

   @Test
   public void testResolveTypeChar() 
   {
      MethodSource<JavaClassSource> method = Roaster.create(JavaClassSource.class).addMethod(
               "public CharSequence get()");
      assertEquals("java.lang.CharSequence", method.getReturnType().getQualifiedName());
   }
}