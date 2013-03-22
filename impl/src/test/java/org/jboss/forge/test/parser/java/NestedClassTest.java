package org.jboss.forge.test.parser.java;

import java.util.List;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.JavaAnnotation;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.JavaInterface;
import org.jboss.forge.parser.java.JavaSource;
import org.junit.Assert;
import org.junit.Test;

public class NestedClassTest
{

   @Test
   public void testImportNestedClass()
   {
      JavaClass javaClass = JavaParser.create(JavaClass.class);
      Import imprt = javaClass.addImport(NestedClass.class);

      Assert.assertEquals("org.jboss.forge.test.parser.java.NestedClassTest.NestedClass",
               imprt.getQualifiedName());
   }

   @Test
   public void testGetNestedClasses()
   {
      JavaClass javaClass = (JavaClass) JavaParser
               .parse("package org.example; public class OuterClass { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterClass", javaClass.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaClass.getNestedClasses();
      JavaSource<?> inner1 = nestedClasses.get(0);
      JavaSource<?> inner2 = nestedClasses.get(1);
      Assert.assertEquals(javaClass, inner1.getEnclosingType());
      Assert.assertEquals("org.example.OuterClass.InnerClass1", inner1.getCanonicalName());
      Assert.assertEquals("org.example.OuterClass$InnerClass1", inner1.getQualifiedName());
      Assert.assertEquals(javaClass, inner2.getEnclosingType());
      Assert.assertEquals("InnerClass1", inner1.getName());
      Assert.assertEquals("org.example.OuterClass.InnerClass2", inner2.getCanonicalName());
      Assert.assertEquals("org.example.OuterClass$InnerClass2", inner2.getQualifiedName());
      Assert.assertEquals("InnerClass2", inner2.getName());
      Assert.assertEquals(2, nestedClasses.size());
   }

   @Test
   public void testModifyNestedClassModifiesParentSource()
   {
      JavaClass javaClass = (JavaClass) JavaParser
               .parse("package org.example; public class OuterClass { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      List<JavaSource<?>> nestedClasses = javaClass.getNestedClasses();
      JavaSource<?> inner1 = nestedClasses.get(0);
      inner1.addAnnotation(Deprecated.class);

      Assert.assertTrue(javaClass.toString().contains("@Deprecated"));
   }

   @Test
   public void testInterfaceWithNestedClass()
   {
      JavaInterface javaInterface = (JavaInterface) JavaParser
               .parse("package org.example; public interface OuterInterface { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterInterface", javaInterface.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaInterface.getNestedClasses();
      JavaSource<?> inner1 = nestedClasses.get(0);
      JavaSource<?> inner2 = nestedClasses.get(1);
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
      JavaEnum javaEnum = (JavaEnum) JavaParser
               .parse("package org.example; public enum OuterEnum { " +
                        "  FOO, BAR, BAZ; " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterEnum", javaEnum.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaEnum.getNestedClasses();
      JavaSource<?> inner1 = nestedClasses.get(0);
      JavaSource<?> inner2 = nestedClasses.get(1);
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
   public void testAnnotationWithNestedClass()
   {
      JavaAnnotation javaAnnotation = (JavaAnnotation) JavaParser
               .parse("package org.example; public @interface OuterAnnotation { " +
                        "  public class InnerClass1{ " +
                        "    public class InnerClass3{}" +
                        "  } " +
                        "  public class InnerClass2{} " +
                        "}");

      Assert.assertEquals("org.example.OuterAnnotation", javaAnnotation.getCanonicalName());
      List<JavaSource<?>> nestedClasses = javaAnnotation.getNestedClasses();
      JavaSource<?> inner1 = nestedClasses.get(0);
      JavaSource<?> inner2 = nestedClasses.get(1);
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

   public class NestedClass
   {
   }

}
