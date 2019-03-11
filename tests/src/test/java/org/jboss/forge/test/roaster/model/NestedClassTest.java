/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NestedClassTest
{

   @Test
   public void testImportNestedClass()
   {
      JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      Import imprt = javaClass.addImport(NestedClass.class);

      assertEquals("org.jboss.forge.test.roaster.model.NestedClassTest.NestedClass",
               imprt.getQualifiedName());
   }

   @Test
   public void testGetNestedClasses()
   {
      String pckage = "package org.example;";
      String outerClass = "public class OuterClass{";
      String innerClass1 = "public class InnerClass1{";
      String innerClass3 = "public class InnerClass3{}";
      String innerClass2 = "public class InnerClass2{}";

      JavaClassSource javaClass = Roaster
               .parse(JavaClassSource.class, pckage + outerClass + innerClass1 + innerClass3 + "}" + innerClass2 + "}");

      assertEquals("org.example.OuterClass", javaClass.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaClass.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      JavaClassSource inner3 = (JavaClassSource) inner1.getNestedTypes().get(0);
      assertEquals(javaClass, inner1.getEnclosingType());
      assertEquals("org.example.OuterClass.InnerClass1", inner1.getCanonicalName());
      assertEquals("org.example.OuterClass$InnerClass1", inner1.getQualifiedName());
      assertEquals(javaClass, inner2.getEnclosingType());
      assertEquals("InnerClass1", inner1.getName());
      assertEquals("org.example.OuterClass.InnerClass2", inner2.getCanonicalName());
      assertEquals("org.example.OuterClass$InnerClass2", inner2.getQualifiedName());
      assertEquals("InnerClass2", inner2.getName());
      assertEquals(2, nestedClasses.size());

      assertEquals(pckage.length(), javaClass.getStartPosition());
      assertThat(inner1.getStartPosition()).isEqualTo(javaClass.getStartPosition() + outerClass.length());
      assertThat(inner2.getStartPosition())
               .isEqualTo(inner1.getStartPosition() + innerClass1.length() + innerClass3.length() + 1);
      assertThat(inner3.getStartPosition()).isEqualTo(inner1.getStartPosition() + innerClass1.length());

      assertThat(javaClass.getColumnNumber()).isEqualTo(pckage.length());
      assertThat(inner1.getColumnNumber()).isEqualTo(javaClass.getColumnNumber() + outerClass.length());
      assertThat(inner2.getColumnNumber())
               .isEqualTo(inner1.getColumnNumber() + innerClass1.length() + innerClass3.length() + 1);
      assertThat(inner3.getColumnNumber()).isEqualTo(inner1.getColumnNumber() + innerClass1.length());

      assertThat(javaClass.getEndPosition()).isEqualTo(pckage.length() + outerClass.length() + innerClass1.length()
               + innerClass2.length() + innerClass3.length() + 2);
      assertThat(inner1.getEndPosition())
               .isEqualTo(pckage.length() + outerClass.length() + innerClass1.length() + innerClass3.length() + 1);
      assertThat(inner2.getEndPosition()).isEqualTo(javaClass.getEndPosition() - 1);
      assertThat(inner3.getEndPosition()).isEqualTo(inner1.getEndPosition() - 1);

      //no line separators -> so line number 1
      assertEquals(1, javaClass.getLineNumber());
      assertEquals(1, inner1.getLineNumber());
      assertEquals(1, inner2.getLineNumber());
      assertEquals(1, inner3.getLineNumber());

      assertEquals("org.example.OuterClass$InnerClass1$InnerClass3", inner3.getQualifiedName());
      assertEquals("org.example.OuterClass.InnerClass1.InnerClass3", inner3.getCanonicalName());
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

      assertTrue(javaClass.toString().contains("@Deprecated"));
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

      assertEquals("org.example.OuterInterface", javaInterface.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaInterface.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      assertEquals(javaInterface, inner1.getEnclosingType());
      assertEquals("org.example.OuterInterface.InnerClass1", inner1.getCanonicalName());
      assertEquals("org.example.OuterInterface$InnerClass1", inner1.getQualifiedName());
      assertEquals(javaInterface, inner2.getEnclosingType());
      assertEquals("InnerClass1", inner1.getName());
      assertEquals("org.example.OuterInterface.InnerClass2", inner2.getCanonicalName());
      assertEquals("org.example.OuterInterface$InnerClass2", inner2.getQualifiedName());
      assertEquals("InnerClass2", inner2.getName());
      assertEquals(2, nestedClasses.size());
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

      assertEquals("org.example.OuterEnum", javaEnum.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaEnum.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      assertEquals(javaEnum, inner1.getEnclosingType());
      assertEquals("org.example.OuterEnum.InnerClass1", inner1.getCanonicalName());
      assertEquals("org.example.OuterEnum$InnerClass1", inner1.getQualifiedName());
      assertEquals(javaEnum, inner2.getEnclosingType());
      assertEquals("InnerClass1", inner1.getName());
      assertEquals("org.example.OuterEnum.InnerClass2", inner2.getCanonicalName());
      assertEquals("org.example.OuterEnum$InnerClass2", inner2.getQualifiedName());
      assertEquals("InnerClass2", inner2.getName());
      assertEquals(2, nestedClasses.size());
   }

   @Test
   public void testClassWithNestedEnum()
   {
      JavaClassSource javaClass = Roaster
               .parse(JavaClassSource.class, "package org.example; "
                        + "public class OuterClass { " +
                        "  public enum InnerEnum{A,B,C;} " +
                        "}");

      assertEquals("org.example.OuterClass", javaClass.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaClass.getNestedTypes();
      JavaEnumSource inner1 = (JavaEnumSource) nestedClasses.get(0);
      assertEquals(javaClass, inner1.getEnclosingType());
      assertEquals("org.example.OuterClass.InnerEnum", inner1.getCanonicalName());
      assertEquals("org.example.OuterClass$InnerEnum", inner1.getQualifiedName());
      assertEquals("InnerEnum", inner1.getName());
      assertEquals(1, nestedClasses.size());
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

      assertEquals("org.example.OuterAnnotation", javaAnnotation.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaAnnotation.getNestedTypes();
      JavaClassSource inner1 = (JavaClassSource) nestedClasses.get(0);
      JavaClassSource inner2 = (JavaClassSource) nestedClasses.get(1);
      assertEquals(javaAnnotation, inner1.getEnclosingType());
      assertEquals("org.example.OuterAnnotation.InnerClass1", inner1.getCanonicalName());
      assertEquals("org.example.OuterAnnotation$InnerClass1", inner1.getQualifiedName());
      assertEquals(javaAnnotation, inner2.getEnclosingType());
      assertEquals("InnerClass1", inner1.getName());
      assertEquals("org.example.OuterAnnotation.InnerClass2", inner2.getCanonicalName());
      assertEquals("org.example.OuterAnnotation$InnerClass2", inner2.getQualifiedName());
      assertEquals("InnerClass2", inner2.getName());
      assertEquals(2, nestedClasses.size());
   }

   @Test
   public void testAddNestedAnnotationInAnnotation()
   {
      JavaAnnotationSource javaAnnotation = Roaster.create(JavaAnnotationSource.class);
      JavaAnnotationSource nestedAnnotation = javaAnnotation.addNestedType(JavaAnnotationSource.class);
      assertNotNull(nestedAnnotation);
      nestedAnnotation.setName("List").addAnnotationElement().setName("value").setType("String[]");
      assertTrue(javaAnnotation.hasNestedType(nestedAnnotation));
      assertTrue(javaAnnotation.hasNestedType("List"));
      JavaAnnotationSource nestedType = (JavaAnnotationSource) javaAnnotation.getNestedType("List");
      assertEquals(nestedAnnotation, nestedType);
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
      assertThat(innerClass1).isInstanceOf(JavaClassSource.class);
      javaClass.removeNestedType(innerClass1);
      assertFalse(javaClass.hasNestedType("InnerClass1"));
      assertFalse(javaClass.hasNestedType(innerClass1));
      assertEquals(1, javaClass.getNestedTypes().size());
   }

   @Test
   public void testRenamedOuterClass()
   {
      JavaClassSource source = Roaster
               .parse(JavaClassSource.class,
                        "public class Outer {public class Inner { String innerField; public Inner() {} public Inner(String innerField){this.innerField=innerField;}} public Outer() {}}");

      JavaClassSource nestedType = (JavaClassSource) source.getNestedType("Inner");
      assertNotNull(nestedType);
      assertEquals("Inner", nestedType.getName());
      assertEquals(1, source.getMethods().size());
      assertEquals(2, nestedType.getMethods().size());

      assertTrue(source.getMethods().get(0).isConstructor());
      assertEquals("Outer", source.getMethods().get(0).getName());

      assertTrue(nestedType.getMethods().get(0).isConstructor());
      assertTrue(nestedType.getMethods().get(1).isConstructor());
      assertEquals("Inner", nestedType.getMethods().get(0).getName());
      assertEquals("Inner", nestedType.getMethods().get(1).getName());

      source.setName("RenamedOuter");
      nestedType = (JavaClassSource) source.getNestedType("Inner");
      assertTrue(source.getMethods().get(0).isConstructor());
      assertEquals("RenamedOuter", source.getMethods().get(0).getName());

      assertTrue(nestedType.getMethods().get(0).isConstructor());
      assertTrue(nestedType.getMethods().get(1).isConstructor());
      assertEquals("Inner", nestedType.getMethods().get(0).getName());
      assertEquals("Inner", nestedType.getMethods().get(1).getName());
   }

   public class NestedClass
   {
      // empty for testing
   }
}
