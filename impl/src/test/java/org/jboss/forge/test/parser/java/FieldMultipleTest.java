package org.jboss.forge.test.parser.java;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.source.FieldSource;
import org.jboss.forge.parser.java.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Test;

public class FieldMultipleTest
{
   @Test
   public void testFieldTypesByteExtraDimensionDeclaration()
   {
      final JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public byte content1[], content2;");
      Assert.assertEquals(2, javaClass.getFields().size());
      Assert.assertEquals("content1", field.getName());
      Assert.assertEquals("byte[]", field.getType().getName());
      Assert.assertEquals("byte", field.getType().getQualifiedName());
      Assert.assertTrue(field.getType().isArray());
      Assert.assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testMultipleFieldDeclaration() throws Exception
   {
      final JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      javaClass.addField("public String a,b,c[];");
      List<FieldSource<JavaClassSource>> fields = javaClass.getFields();

      Assert.assertEquals(3, fields.size());

      Assert.assertEquals("a", fields.get(0).getName());
      Assert.assertEquals("java.lang.String", fields.get(0).getType().getQualifiedName());
      Assert.assertEquals("String", fields.get(0).getType().getName());
      Assert.assertEquals(true, fields.get(0).isPublic());
      Assert.assertFalse(fields.get(0).getType().isArray());

      Assert.assertEquals("b", fields.get(1).getName());
      Assert.assertEquals("java.lang.String", fields.get(1).getType().getQualifiedName());
      Assert.assertEquals("String", fields.get(1).getType().getName());
      Assert.assertEquals(true, fields.get(1).isPublic());
      Assert.assertFalse(fields.get(1).getType().isArray());

      Assert.assertEquals("c", fields.get(2).getName());
      Assert.assertEquals("java.lang.String", fields.get(2).getType().getQualifiedName());
      Assert.assertEquals("String[]", fields.get(2).getType().getName());
      Assert.assertEquals(true, fields.get(2).isPublic());
      Assert.assertTrue(fields.get(2).getType().isArray());
      Assert.assertEquals(1, fields.get(2).getType().getArrayDimensions());
   }

   @Test
   public void testMultipleFieldDeclarationWithAnnotation() throws Exception
   {
      final JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      javaClass.addField("@javax.xml.bind.annotation.XmlElement public String a,b,c[];");
      List<FieldSource<JavaClassSource>> fields = javaClass.getFields();

      Assert.assertEquals(3, fields.size());

      Assert.assertEquals("a", fields.get(0).getName());
      Assert.assertEquals("java.lang.String", fields.get(0).getType().getQualifiedName());
      Assert.assertEquals("String", fields.get(0).getType().getName());
      Assert.assertFalse(fields.get(0).getType().isArray());
      Assert.assertTrue(fields.get(0).hasAnnotation(XmlElement.class));

      Assert.assertEquals("b", fields.get(1).getName());
      Assert.assertEquals("java.lang.String", fields.get(1).getType().getQualifiedName());
      Assert.assertEquals("String", fields.get(1).getType().getName());
      Assert.assertFalse(fields.get(1).getType().isArray());
      Assert.assertTrue(fields.get(1).hasAnnotation(XmlElement.class));

      Assert.assertEquals("c", fields.get(2).getName());
      Assert.assertEquals("java.lang.String", fields.get(2).getType().getQualifiedName());
      Assert.assertEquals("String[]", fields.get(2).getType().getName());
      Assert.assertTrue(fields.get(2).getType().isArray());
      Assert.assertEquals(1, fields.get(2).getType().getArrayDimensions());
      Assert.assertTrue(fields.get(2).hasAnnotation(XmlElement.class));
   }

   @Test
   public void testMultipleFieldDeclarationWithInitializers() throws Exception
   {
      final JavaClassSource javaClass = JavaParser.create(JavaClassSource.class);
      javaClass.addField("private static final String a = \"A\",b =\"B\",c[] = {\"C\"};");
      List<FieldSource<JavaClassSource>> fields = javaClass.getFields();

      Assert.assertEquals(3, fields.size());

      Assert.assertEquals("a", fields.get(0).getName());
      Assert.assertEquals("java.lang.String", fields.get(0).getType().getQualifiedName());
      Assert.assertEquals("String", fields.get(0).getType().getName());
      Assert.assertEquals(true, fields.get(0).isPrivate());
      Assert.assertEquals(true, fields.get(0).isStatic());
      Assert.assertEquals("A", fields.get(0).getStringInitializer());
      Assert.assertFalse(fields.get(0).getType().isArray());

      Assert.assertEquals("b", fields.get(1).getName());
      Assert.assertEquals("java.lang.String", fields.get(1).getType().getQualifiedName());
      Assert.assertEquals("String", fields.get(1).getType().getName());
      Assert.assertEquals(true, fields.get(1).isPrivate());
      Assert.assertEquals(true, fields.get(1).isStatic());
      Assert.assertEquals("B", fields.get(1).getStringInitializer());
      Assert.assertFalse(fields.get(1).getType().isArray());

      Assert.assertEquals("c", fields.get(2).getName());
      Assert.assertEquals("java.lang.String", fields.get(2).getType().getQualifiedName());
      Assert.assertEquals("String[]", fields.get(2).getType().getName());
      Assert.assertEquals(true, fields.get(2).isPrivate());
      Assert.assertEquals(true, fields.get(2).isStatic());
      Assert.assertEquals("{\"C\"}", fields.get(2).getLiteralInitializer());
      Assert.assertTrue(fields.get(2).getType().isArray());
      Assert.assertEquals(1, fields.get(2).getType().getArrayDimensions());
   }

}
