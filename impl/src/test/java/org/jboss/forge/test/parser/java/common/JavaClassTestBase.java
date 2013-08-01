/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.Method;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class JavaClassTestBase
{
   private JavaClass source;

   @Before
   public void reset()
   {
      this.source = getSource();
      assertTrue(this.source.isEditable());
   }

   protected abstract JavaClass getSource();

   @Test
   public void testApplyChangesNotRequiredForModification() throws Exception
   {
      assertEquals("MockClass", source.getName());
      source.setName("Telephone");
      assertEquals("Telephone", source.getName());

      String output = source.toString();
      assertTrue(output.contains("Telephone"));
      assertFalse(output.contains("MockClass"));
   }

   @Test
   public void testFormattingIsNotChanged() throws Exception
   {
      assertEquals("MockClass", source.getName());
      source.setName("Telephone");
      assertEquals("Telephone", source.getName());

      String output = source.toString();

      assertTrue(Pattern.compile(".*Telephone\\s*(\\r?\\n)\\s*\\{.*", Pattern.DOTALL).matcher(output).matches());
      assertTrue(Pattern.compile(".*\\)\\s*(\\r?\\n)\\s*\\{.*", Pattern.DOTALL).matcher(output).matches());
   }

   @Test
   public void testParse() throws Exception
   {
      assertEquals(URL.class.getName(), source.getImports().get(0).getQualifiedName());
      assertEquals(1, source.getMethods().size());
      assertEquals("MockClass", source.getName());
      assertTrue(source.isPublic());
      assertFalse(source.isAbstract());
   }

   @Test
   public void testSetName() throws Exception
   {
      assertEquals("MockClass", source.getName());
      source.setName("Telephone");
      assertEquals("Telephone", source.getName());
   }

   @Test
   public void testSetNameUpdatesConstructorNames() throws Exception
   {
      Method<JavaClass> constructor = source.addMethod().setConstructor(true).setPublic();
      assertEquals("MockClass", source.getName());
      assertEquals("MockClass", constructor.getName());
      source.setName("Telephone");
      assertEquals("Telephone", source.getName());
      assertEquals("Telephone", constructor.getName());
   }

   @Test
   public void testSetPackage() throws Exception
   {
      source.setPackage("org.lincoln");
      assertEquals("org.lincoln", source.getPackage());
      assertFalse(source.isDefaultPackage());
   }

   @Test
   public void testSetAbstract() throws Exception
   {
      source.setAbstract(true);
      assertTrue(source.isAbstract());
   }

   @Test
   public void testSetPackageDefault() throws Exception
   {
      source.setDefaultPackage();
      assertNull(source.getPackage());
      assertTrue(source.isDefaultPackage());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddImportPrimitiveThrowsException() throws Exception
   {
      source.addImport(boolean.class);
   }

   @Test
   public void testAddImport() throws Exception
   {
      source.addImport(List.class.getName());
      assertEquals(2, source.getImports().size());
      assertEquals(URL.class.getName(), source.getImports().get(0).getQualifiedName());
      assertEquals(List.class.getName(), source.getImports().get(1).getQualifiedName());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCannotAddSimpleClassImport() throws Exception
   {
      source.addImport("List");
   }

   @Test
   public void testAddImportClasses() throws Exception
   {
      assertEquals(1, source.getImports().size());

      source.addImport(List.class);
      source.addImport(Map.class);

      assertEquals(3, source.getImports().size());
      assertEquals(Map.class.getName(), source.getImports().get(2).getQualifiedName());
   }

   @Test
   public void testAddImportStatic() throws Exception
   {
      assertEquals(1, source.getImports().size());
      source.addImport(List.class).setStatic(true);
      assertEquals(2, source.getImports().size());
      assertTrue(source.getImports().get(1).isStatic());
   }

   @Test
   public void testHasImport() throws Exception
   {
      assertEquals(1, source.getImports().size());
      assertFalse(source.hasImport(List.class));
      source.addImport(List.class);
      assertEquals(2, source.getImports().size());
      assertTrue(source.hasImport(List.class));
   }

   @Test
   public void testCannotAddDuplicateImport() throws Exception
   {
      assertEquals(1, source.getImports().size());
      assertFalse(source.hasImport(List.class));
      source.addImport(List.class);
      source.addImport(List.class);
      assertEquals(2, source.getImports().size());
      assertTrue(source.hasImport(List.class));
   }

   @Test
   public void testRemoveImportByClass() throws Exception
   {
      List<Import> imports = source.getImports();
      assertEquals(1, imports.size());
      assertEquals(URL.class.getName(), imports.get(0).getQualifiedName());

      source.removeImport(URL.class);
      imports = source.getImports();
      assertEquals(0, imports.size());
   }

   @Test
   public void testRemoveImportByName() throws Exception
   {
      assertEquals(1, source.getImports().size());
      assertEquals(URL.class.getName(), source.getImports().get(0).getQualifiedName());

      source.removeImport(URL.class.getName());
      assertEquals(0, source.getImports().size());
   }

   @Test
   public void testRemoveImportByReference() throws Exception
   {
      assertEquals(1, source.getImports().size());
      assertEquals(URL.class.getName(), source.getImports().get(0).getQualifiedName());

      source.removeImport(source.getImports().get(0));
      assertEquals(0, source.getImports().size());
   }

   @Test
   public void testRequiresImport() throws Exception
   {
      assertFalse(source.hasImport(JavaClassTestBase.class));
      assertTrue(source.requiresImport(JavaClassTestBase.class));
      source.addImport(JavaClassTestBase.class);
      assertTrue(source.hasImport(JavaClassTestBase.class));
      assertFalse(source.requiresImport(JavaClassTestBase.class));
   }

   @Test
   public void testAddImportAcceptsJavaLangPackage() throws Exception
   {
      assertFalse(source.hasImport(String.class));
      assertFalse(source.requiresImport(String.class));
      source.addImport(String.class);
      assertTrue(source.hasImport(String.class));
      assertFalse(source.requiresImport(String.class));
   }

   @Test
   public void testAddMethod() throws Exception
   {
      int size = source.getMethods().size();
      Method<JavaClass> method = source.addMethod().setName("testMethod").setReturnTypeVoid().setBody("");
      List<Method<JavaClass>> methods = source.getMethods();
      assertEquals(size + 1, methods.size());
      assertNull(method.getReturnType());
   }

   @Test
   public void testAddMethodFromString() throws Exception
   {
      int size = source.getMethods().size();
      Method<JavaClass> method = source.addMethod(
               "public URL rewriteURL(String pattern, String replacement) { return null; }")
               .setPackagePrivate();
      List<Method<JavaClass>> methods = source.getMethods();
      assertEquals(size + 1, methods.size());
      assertEquals("URL", method.getReturnType());
      assertEquals("rewriteURL", method.getName());

      String body = method.getBody();
      assertEquals("return null;".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testRemoveMethod() throws Exception
   {
      int size = source.getMethods().size();
      List<Method<JavaClass>> methods = source.getMethods();
      source.removeMethod(methods.get(0));
      methods = source.getMethods();
      assertEquals(size - 1, methods.size());
   }

   @Test
   public void testAddConstructor() throws Exception
   {
      int size = source.getMethods().size();
      Method<JavaClass> method = source.addMethod().setName("testMethod").setConstructor(true).setProtected()
               .setReturnType(String.class)
               .setBody("System.out.println(\"I am a constructor!\");");
      assertEquals(size + 1, source.getMethods().size());
      assertEquals(source.getName(), method.getName());
      assertTrue(method.isProtected());
      assertTrue(method.isConstructor());
      assertNull(method.getReturnType());
      String body = method.getBody();
      assertEquals("System.out.println(\"I am a constructor!\");".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testAddConstructorIgnoresReturnTypeAndName() throws Exception
   {
      int size = source.getMethods().size();
      Method<JavaClass> method = source.addMethod().setName("testMethod").setConstructor(true).setPrivate()
               .setReturnType(String.class)
               .setBody("System.out.println(\"I am a constructor!\");");
      assertEquals(size + 1, source.getMethods().size());
      assertTrue(method.isPrivate());
      assertTrue(method.isConstructor());
      assertNull(method.getReturnType());
      assertEquals(source.getName(), method.getName());
      String body = method.getBody();
      assertEquals("System.out.println(\"I am a constructor!\");".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testSuperType() throws Exception
   {
      JavaClass source = JavaParser.parse(JavaClass.class, "public class Base extends Super {}");
      assertEquals("Super", source.getSuperType());

      source.setSuperType(getClass());
      assertEquals(getClass().getName(), source.getSuperType());
   }

   @Test
   public void testSuperTypeJavaLang() throws Exception
   {
      JavaClass source = JavaParser.parse(JavaClass.class, "public class Base extends Integer {}");
      assertEquals("java.lang.Integer", source.getSuperType());

      source.setSuperType(getClass());
      assertEquals(getClass().getName(), source.getSuperType());
   }
}
