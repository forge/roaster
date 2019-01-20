/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class JavaClassTestBase
{
   private JavaClassSource source;

   @Before
   public void reset()
   {
      this.source = getSource();
   }

   protected abstract JavaClassSource getSource();

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

      assertTrue(Pattern.compile(".*Telephone\\s*\\{.*", Pattern.DOTALL).matcher(output).matches());
      assertTrue(Pattern.compile(".*\\)\\s*\\{.*", Pattern.DOTALL).matcher(output).matches());
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
      MethodSource<JavaClassSource> constructor = source.addMethod().setConstructor(true).setPublic();
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

   @Test()
   public void testAddImportPrimitiveThrowsException()
   {
      assertNull(source.addImport(boolean.class));
   }

   @Test
   public void testAddImport() throws Exception
   {
      source.addImport(List.class.getName());
      assertEquals(2, source.getImports().size());
      assertEquals(URL.class.getName(), source.getImports().get(0).getQualifiedName());
      assertEquals(List.class.getName(), source.getImports().get(1).getQualifiedName());
   }

   @Test()
   public void testCannotAddSimpleClassImport() throws Exception
   {
      assertNull(source.addImport("List"));
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
      assertFalse(source.requiresImport(String.class));
      assertTrue(source.requiresImport(Annotation.class));
      assertFalse(source.requiresImport(source.getPackage() + ".Foo"));
   }
   
   @Test
   public void testRequiresImportNested()
   {
     JavaClassSource source = Roaster.create(JavaClassSource.class);
     assertTrue(source.requiresImport("package1.Class1<package2.Class2>"));
     
     source.addImport("package1.Class1<?>");
     assertTrue(source.getImports().size() == 1);
     assertTrue(source.requiresImport("package1.Class1<package2.Class2>"));
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
      MethodSource<JavaClassSource> method = source.addMethod().setName("testMethod").setReturnTypeVoid().setBody("");
      List<MethodSource<JavaClassSource>> methods = source.getMethods();
      assertEquals(size + 1, methods.size());
      assertTrue(method.isReturnTypeVoid());
   }

   @Test
   public void testAddMethodFromString() throws Exception
   {
      int size = source.getMethods().size();
      MethodSource<JavaClassSource> method = source.addMethod(
               "public URL rewriteURL(String pattern, String replacement) { return null; }")
               .setPackagePrivate();
      List<MethodSource<JavaClassSource>> methods = source.getMethods();
      assertEquals(size + 1, methods.size());
      assertEquals("URL", method.getReturnType().getName());
      assertEquals("rewriteURL", method.getName());

      String body = method.getBody();
      assertEquals("return null;".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testRemoveMethod() throws Exception
   {
      int size = source.getMethods().size();
      List<MethodSource<JavaClassSource>> methods = source.getMethods();
      source.removeMethod(methods.get(0));
      methods = source.getMethods();
      assertEquals(size - 1, methods.size());
   }

   @Test
   public void testAddConstructor() throws Exception
   {
      int size = source.getMethods().size();
      MethodSource<JavaClassSource> method = source.addMethod().setName("testMethod").setConstructor(true)
               .setProtected()
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
      MethodSource<JavaClassSource> method = source.addMethod().setName("testMethod").setConstructor(true).setPrivate()
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
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Base extends Super {}");
      assertEquals("Super", source.getSuperType());

      source.setSuperType(getClass());
      assertEquals(getClass().getName(), source.getSuperType());

      source = Roaster.create(JavaClassSource.class);
      source.setName("Foo").setSuperType("bar.Foo");
      assertEquals("bar.Foo", source.getSuperType());
      assertEquals(0, source.getImports().size());

      source.setSuperType("foo.Bar");
      // getSuperType retuns a fqn name
      assertEquals("foo.Bar", source.getSuperType());
      assertEquals(source.getImports().get(0).getQualifiedName(), "foo.Bar");
   }

   @Test
   public void testSuperTypeWithNull()
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Base{}");
      String objectClass = Object.class.getName();
      assertThat(source.getSuperType(), is(objectClass));

      // set super type per Class
      source.setSuperType("Super");
      assertThat(source.getSuperType(), is("Super"));
      source.setSuperType((Class<?>) null);
      assertThat(source.getSuperType(), is(objectClass));

      // set super type per JavaClass
      source.setSuperType("Super");
      assertThat(source.getSuperType(), is("Super"));
      source.setSuperType((JavaClass<?>) null);
      assertThat(source.getSuperType(), is(objectClass));

      // set super type per String (null)
      source.setSuperType("Super");
      assertThat(source.getSuperType(), is("Super"));
      source.setSuperType((String) null);
      assertThat(source.getSuperType(), is(objectClass));

      // set super type per String (empty)
      source.setSuperType("Super");
      assertThat(source.getSuperType(), is("Super"));
      source.setSuperType("  ");
      assertThat(source.getSuperType(), is(objectClass));
   }

   @Test
   public void testSuperTypeJavaLang() throws Exception
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Base extends Integer {}");
      assertEquals("java.lang.Integer", source.getSuperType());

      source.setSuperType(getClass());
      assertEquals(getClass().getName(), source.getSuperType());
   }

   @Test
   public void testSuperTypeImport() throws Exception
   {
      JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Base extends Super {}");
      assertEquals("Super", source.getSuperType());

      source.extendSuperType(NumberFormat.class);
      assertEquals(NumberFormat.class.getName(), source.getSuperType());
      assertFalse(source.hasSyntaxErrors());
      assertEquals(3, source.getMethods().size());
   }

   @Test
   public void testSuperTypeGenericsWithSpaces()
   {
      final JavaClassSource myClass = Roaster.create(JavaClassSource.class);
      myClass.setPackage("test");
      myClass.setPublic()
               .setName("MyClass")
               .setSuperType("test.MyClassParent<java.util.String, java.util.Object>");
      Assert.assertTrue(myClass.hasImport("java.util.String"));
      Assert.assertTrue(myClass.hasImport("java.util.Object"));
      Assert.assertTrue(myClass.hasImport("test.MyClassParent"));
      Assert.assertThat(myClass.getSuperType(), equalTo("test.MyClassParent<String,Object>"));
   }

   @Test
   public void testSuperTypeWithConflictingImport()
   {
      final JavaClassSource myClass = Roaster.create(JavaClassSource.class);
      myClass.setPackage("test");
      final Import utilListImport = myClass.addImport(List.class);
      myClass.setSuperType("java.awt.List");

      assertEquals("Class should only contain one import.", 1, myClass.getImports().size());
      assertEquals("Wrong import detected.", utilListImport, myClass.getImport("List"));
      assertEquals("Wrong super type set.", "java.awt.List", myClass.getSuperType());
   }

   @Test
   public void testFinal() throws Exception
   {
      source.setFinal(false);
      assertFalse(source.isFinal());
      source.setFinal(true);
      assertTrue(source.isFinal());
   }

   @Test
   public void testStatic()
   {
      source.setStatic(true);
      assertTrue(source.isStatic());
      source.setStatic(false);
      assertFalse(source.isStatic());
   }

   @Test
   public void testNoDuplicateImportForTypes()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("testPackage").setName("testClass");
      javaClass.addField().setName("field1").setType("package1.Type");
      javaClass.addField().setName("field2").setType("package2.Type");
      assertThat(javaClass.getImports().size(), is(1));
      assertThat(javaClass.getImports().get(0).getQualifiedName(), is("package1.Type"));
   }

   @Test
   public void testNoImportForTypeWithClassName()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.setPackage("testPackage").setName("testClass");
      javaClass.addField().setName("field1").setType("package1.testClass");
      assertThat(javaClass.getImports().size(), is(0));
   }
}