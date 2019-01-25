/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Assert;
import org.junit.Test;

public class NestedClassTest
{

   @Test
   public void testImportNestedClass()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      Import imprt = javaClass.addImport(NestedClass.class);

      Assert.assertEquals("org.jboss.forge.test.roaster.model.NestedClassTest.NestedClass",
               imprt.getQualifiedName());
   }

   @Test
   public void testGetNestedClasses()
   {
      JavaClassSource javaClass = Roaster
               .parse(JavaClassSource.class, "package org.example; public class OuterClass { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterClass", javaClass.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaClass.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      JavaClassSource inner3 = (JavaClassSource) inner1.getNestedTypes().get(0);
      Assert.assertEquals(javaClass, inner1.getEnclosingType());
      Assert.assertEquals("org.example.OuterClass.InnerClass1", inner1.getCanonicalName());
      Assert.assertEquals("org.example.OuterClass$InnerClass1", inner1.getQualifiedName());
      Assert.assertEquals(javaClass, inner2.getEnclosingType());
      Assert.assertEquals("InnerClass1", inner1.getName());
      Assert.assertEquals("org.example.OuterClass.InnerClass2", inner2.getCanonicalName());
      Assert.assertEquals("org.example.OuterClass$InnerClass2", inner2.getQualifiedName());
      Assert.assertEquals("InnerClass2", inner2.getName());
      Assert.assertEquals(2, nestedClasses.size());

      Assert.assertEquals("org.example.OuterClass$InnerClass1$InnerClass3", inner3.getQualifiedName());
      Assert.assertEquals("org.example.OuterClass.InnerClass1.InnerClass3", inner3.getCanonicalName());
   }

   @Test
   public void testModifyNestedClassModifiesParentSource()
   {
      JavaClassSource javaClass = Roaster
               .parse(JavaClassSource.class, "package org.example; public class OuterClass { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      List<JavaSource<?>> nestedClasses = javaClass.getNestedTypes();
      JavaSource<?> inner1 = nestedClasses.get(0);
      inner1.addAnnotation(Deprecated.class);

      Assert.assertTrue(javaClass.toString().contains("@Deprecated"));
   }

   @Test
   public void testInterfaceWithNestedClass()
   {
      JavaInterfaceSource javaInterface = Roaster
               .parse(JavaInterfaceSource.class, "package org.example; public interface OuterInterface { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterInterface", javaInterface.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaInterface.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      Assert.assertEquals(javaInterface, inner1.getEnclosingType());
      Assert.assertEquals("org.example.OuterInterface.InnerClass1", inner1.getCanonicalName());
      Assert.assertEquals("org.example.OuterInterface$InnerClass1", inner1.getQualifiedName());
      Assert.assertEquals(javaInterface, inner2.getEnclosingType());
      Assert.assertEquals("InnerClass1", inner1.getName());
      Assert.assertEquals("org.example.OuterInterface.InnerClass2", inner2.getCanonicalName());
      Assert.assertEquals("org.example.OuterInterface$InnerClass2", inner2.getQualifiedName());
      Assert.assertEquals("InnerClass2", inner2.getName());
      Assert.assertEquals(2, nestedClasses.size());
   }

   @Test
   public void testEnumWithNestedClass()
   {
      JavaEnumSource javaEnum = Roaster
               .parse(JavaEnumSource.class, "package org.example; public enum OuterEnum { " +
                        "  FOO, BAR, BAZ; " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterEnum", javaEnum.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaEnum.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      Assert.assertEquals(javaEnum, inner1.getEnclosingType());
      Assert.assertEquals("org.example.OuterEnum.InnerClass1", inner1.getCanonicalName());
      Assert.assertEquals("org.example.OuterEnum$InnerClass1", inner1.getQualifiedName());
      Assert.assertEquals(javaEnum, inner2.getEnclosingType());
      Assert.assertEquals("InnerClass1", inner1.getName());
      Assert.assertEquals("org.example.OuterEnum.InnerClass2", inner2.getCanonicalName());
      Assert.assertEquals("org.example.OuterEnum$InnerClass2", inner2.getQualifiedName());
      Assert.assertEquals("InnerClass2", inner2.getName());
      Assert.assertEquals(2, nestedClasses.size());
   }

   @Test
   public void testClassWithNestedEnum()
   {
      JavaClassSource javaClass = Roaster
               .parse(JavaClassSource.class, "package org.example; "
                        + "public class OuterClass { " +
                        "  public enum InnerEnum{A,B,C;} " +
                        "}");

      Assert.assertEquals("org.example.OuterClass", javaClass.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaClass.getNestedTypes();
      JavaEnumSource inner1 = (JavaEnumSource) nestedClasses.get(0);
      Assert.assertEquals(javaClass, inner1.getEnclosingType());
      Assert.assertEquals("org.example.OuterClass.InnerEnum", inner1.getCanonicalName());
      Assert.assertEquals("org.example.OuterClass$InnerEnum", inner1.getQualifiedName());
      Assert.assertEquals("InnerEnum", inner1.getName());
      Assert.assertEquals(1, nestedClasses.size());
   }

   @Test
   public void testAnnotationWithNestedClass()
   {
      JavaAnnotationSource javaAnnotation = Roaster
               .parse(JavaAnnotationSource.class, "package org.example; public @interface OuterAnnotation { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterAnnotation", javaAnnotation.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaAnnotation.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      Assert.assertEquals(javaAnnotation, inner1.getEnclosingType());
      Assert.assertEquals("org.example.OuterAnnotation.InnerClass1", inner1.getCanonicalName());
      Assert.assertEquals("org.example.OuterAnnotation$InnerClass1", inner1.getQualifiedName());
      Assert.assertEquals(javaAnnotation, inner2.getEnclosingType());
      Assert.assertEquals("InnerClass1", inner1.getName());
      Assert.assertEquals("org.example.OuterAnnotation.InnerClass2", inner2.getCanonicalName());
      Assert.assertEquals("org.example.OuterAnnotation$InnerClass2", inner2.getQualifiedName());
      Assert.assertEquals("InnerClass2", inner2.getName());
      Assert.assertEquals(2, nestedClasses.size());
   }

   @Test
   public void testAddNestedAnnotationInAnnotation()
   {
      JavaAnnotationSource javaAnnotation = Roaster.create(JavaAnnotationSource.class);
      JavaAnnotationSource nestedAnnotation = javaAnnotation.addNestedType(JavaAnnotationSource.class);
      Assert.assertNotNull(nestedAnnotation);
      nestedAnnotation.setName("List").addAnnotationElement().setName("value").setType("String[]");
      Assert.assertTrue(javaAnnotation.hasNestedType(nestedAnnotation));
      Assert.assertTrue(javaAnnotation.hasNestedType("List"));
      JavaAnnotationSource nestedType = (JavaAnnotationSource) javaAnnotation.getNestedType("List");
      Assert.assertEquals(nestedAnnotation, nestedType);
   }

   @Test
   public void testRemoveNestedClassInClass()
   {
      JavaClassSource javaClass = Roaster
               .parse(JavaClassSource.class, "package org.example; public class OuterClass { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");
      JavaSource<?> innerClass1 = javaClass.getNestedType("InnerClass1");
      Assert.assertThat(innerClass1, CoreMatchers.instanceOf(JavaClassSource.class));
      javaClass.removeNestedType(innerClass1);
      Assert.assertFalse(javaClass.hasNestedType("InnerClass1"));
      Assert.assertFalse(javaClass.hasNestedType(innerClass1));
      Assert.assertEquals(1, javaClass.getNestedTypes().size());
   }

   @Test
   public void testRenamedOuterClass() 
   {
      JavaClassSource source = Roaster
               .parse(JavaClassSource.class,
                        "public class Outer {public class Inner { String innerField; public Inner() {} public Inner(String innerField){this.innerField=innerField;}} public Outer() {}}");

      JavaClassSource nestedType = (JavaClassSource) source.getNestedType("Inner");
      Assert.assertNotNull(nestedType);
      Assert.assertEquals("Inner", nestedType.getName());
      Assert.assertEquals(1, source.getMethods().size());
      Assert.assertEquals(2, nestedType.getMethods().size());

      Assert.assertTrue(source.getMethods().get(0).isConstructor());
      Assert.assertEquals("Outer", source.getMethods().get(0).getName());

      Assert.assertTrue(nestedType.getMethods().get(0).isConstructor());
      Assert.assertTrue(nestedType.getMethods().get(1).isConstructor());
      Assert.assertEquals("Inner", nestedType.getMethods().get(0).getName());
      Assert.assertEquals("Inner", nestedType.getMethods().get(1).getName());

      source.setName("RenamedOuter");
      nestedType = (JavaClassSource) source.getNestedType("Inner");
      Assert.assertTrue(source.getMethods().get(0).isConstructor());
      Assert.assertEquals("RenamedOuter", source.getMethods().get(0).getName());

      Assert.assertTrue(nestedType.getMethods().get(0).isConstructor());
      Assert.assertTrue(nestedType.getMethods().get(1).isConstructor());
      Assert.assertEquals("Inner", nestedType.getMethods().get(0).getName());
      Assert.assertEquals("Inner", nestedType.getMethods().get(1).getName());
   }

   public class NestedClass
   {
      //empty for testing
   }
}