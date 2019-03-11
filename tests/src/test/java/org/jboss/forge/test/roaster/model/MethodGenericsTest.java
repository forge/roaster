/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodGenericsTest
{

   @Test
   public void addAndRemoveGenericType()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);

      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addTypeVariable().setName("T");

      assertTrue(method.toString().contains("<T>"));
      assertTrue(method.getTypeVariables().get(0).getBounds().isEmpty());
      method.removeTypeVariable("T");
      assertFalse(method.toString().contains("<T>"));
   }

   @Test
   public void addMultipleGenerics()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();

      method.addTypeVariable().setName("I");
      method.addTypeVariable().setName("O");
      assertTrue(Pattern.compile("<I, *O>").matcher(method.toString()).find());
      method.removeTypeVariable("I");
      assertTrue(method.toString().contains("<O>"));
   }

   @Test
   public void getMethodGenerics()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();

      method.addTypeVariable().setName("I");
      method.addTypeVariable().setName("O");
      List<TypeVariableSource<JavaClassSource>> typeVariables = method.getTypeVariables();
      assertNotNull(typeVariables);
      assertEquals(2, typeVariables.size());
      assertEquals("I", typeVariables.get(0).getName());
      assertTrue(typeVariables.get(0).getBounds().isEmpty());
      assertEquals("O", typeVariables.get(1).getName());
      assertTrue(typeVariables.get(1).getBounds().isEmpty());
   }

   @Test
   public void classTypeVariableBounds()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addTypeVariable().setName("T").setBounds(CharSequence.class);
      assertTrue(method.toString().contains("<T extends CharSequence>"));
      method.getTypeVariable("T").setBounds(CharSequence.class, Serializable.class);
      assertTrue(method.toString().contains("<T extends CharSequence & Serializable>"));
      method.getTypeVariable("T").removeBounds();
      assertTrue(method.toString().contains("<T>"));
   }

   @Test
   public void javaTypeTypeVariableBounds()
   {
      JavaInterface<?> foo = Roaster.create(JavaInterfaceSource.class).setPackage("it.coopservice.test").setName("Foo");
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addTypeVariable().setName("T").setBounds(foo);
      assertTrue(method.toString().contains("<T extends Foo>"));
      JavaInterface<?> bar = Roaster.create(JavaInterfaceSource.class).setPackage("it.coopservice.test").setName("Bar");
      method.getTypeVariable("T").setBounds(foo, bar);
      assertTrue(method.toString().contains("<T extends Foo & Bar>"));
      method.getTypeVariable("T").removeBounds();
      assertTrue(method.toString().contains("<T>"));
   }

   @Test
   public void stringTypeVariableBounds()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addTypeVariable().setName("T").setBounds("com.something.Foo");
      assertTrue(method.toString().contains("<T extends com.something.Foo>"));
      method.getTypeVariable("T").setBounds("com.something.Foo", "com.something.Bar<T>");
      assertTrue(method.toString().contains("<T extends com.something.Foo & com.something.Bar<T>>"));
      method.getTypeVariable("T").removeBounds();
      assertTrue(method.toString().contains("<T>"));
   }

   @Test
   public void fullyQualifiedArrayArguments()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addTypeVariable().setName("T").setBounds(CharSequence.class);
      method.addParameter("java.util.Map<org.foo.String[],T>[]", "complexMap");
      Type<?> type = method.getParameters().get(0).getType();
      assertEquals("Map<org.foo.String[],T>[]", type.toString());
   }

   @Test
   public void nestedTypedParameter()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> method = javaClass.addMethod();
      method.addParameter("java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>", "map");
      Type<?> type = method.getParameters().get(0).getType();
      assertEquals("java.util.Map", type.getQualifiedName());
   }

   @Test
   public void testSetReturnTypeWithGenerics()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> srcMethod = source.addMethod();
      srcMethod.setName("name");
      srcMethod.setPublic();
      srcMethod.addTypeVariable("T");
      srcMethod.setReturnType("List");
      assertThat(srcMethod.toString()).contains("public <T>List name()");
      srcMethod.setReturnType("List<T>");
      assertThat(srcMethod.toString()).contains("public <T>List<T> name()");
   }

   @Test
   public void testTransferableReturnType()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> srcMethod = source.addMethod();
      srcMethod.setName("name");
      srcMethod.addTypeVariable("T");
      srcMethod.setReturnType("List<T>");
      JavaClassSource dest = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> destMethod = dest.addMethod().setName("name");
      destMethod.addTypeVariable("T");
      destMethod.setReturnType(srcMethod.getReturnType());
      assertThat(destMethod.toString()).contains("List<T> name()");
   }

   @Test
   public void testTransferableReturnTypeString()
   {
      JavaClassSource source = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> srcMethod = source.addMethod();
      srcMethod.setName("name");
      srcMethod.addTypeVariable("T");
      srcMethod.setReturnType("List<T>");
      JavaClassSource dest = Roaster.create(JavaClassSource.class);
      MethodSource<JavaClassSource> destMethod = dest.addMethod().setName("name");
      destMethod.addTypeVariable("T");
      destMethod.setReturnType(srcMethod.getReturnType().getQualifiedNameWithGenerics());
      assertThat(destMethod.toString()).contains("List<T> name()");
   }
}