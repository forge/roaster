package org.jboss.forge.test.parser.java;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.junit.Assert;
import org.junit.Test;

public class FieldMultipleTest
{
   @Test
   public void testFieldTypesByteExtraDimensionDeclaration()
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      final Field<JavaClass> field = javaClass.addField("public byte content1[], content2;");
      Assert.assertEquals(2, javaClass.getFields().size());
      Assert.assertEquals("content1", field.getName());
      Assert.assertEquals("byte[]", field.getQualifiedType());
      Assert.assertEquals("byte[]", field.getType());
      Assert.assertTrue(field.getTypeInspector().isArray());
   }
   
   @Test
   public void testMultipleFieldDeclaration() throws Exception
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.addField("public String a,b,c[];");
      List<Field<JavaClass>> fields = javaClass.getFields();
      
      Assert.assertEquals(3, fields.size());
      
      Assert.assertEquals("a", fields.get(0).getName());
      Assert.assertEquals("java.lang.String", fields.get(0).getQualifiedType());
      Assert.assertEquals("String", fields.get(0).getType());
      Assert.assertEquals(true, fields.get(0).isPublic());
      Assert.assertFalse(fields.get(0).getTypeInspector().isArray());
      
      Assert.assertEquals("b", fields.get(1).getName());
      Assert.assertEquals("java.lang.String", fields.get(1).getQualifiedType());
      Assert.assertEquals("String", fields.get(1).getType());
      Assert.assertEquals(true, fields.get(0).isPublic());
      Assert.assertFalse(fields.get(1).getTypeInspector().isArray());
      
      Assert.assertEquals("c", fields.get(2).getName());
      Assert.assertEquals("java.lang.String[]", fields.get(2).getQualifiedType());
      Assert.assertEquals("String[]", fields.get(2).getType());
      Assert.assertEquals(true, fields.get(0).isPublic());
      Assert.assertTrue(fields.get(2).getTypeInspector().isArray());
   }

   @Test
   public void testMultipleFieldDeclarationWithAnnotation() throws Exception
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.addField("@javax.xml.bind.annotation.XmlElement public String a,b,c[];");
      List<Field<JavaClass>> fields = javaClass.getFields();
      
      Assert.assertEquals(3, fields.size());
      
      Assert.assertEquals("a", fields.get(0).getName());
      Assert.assertEquals("java.lang.String", fields.get(0).getQualifiedType());
      Assert.assertEquals("String", fields.get(0).getType());
      Assert.assertFalse(fields.get(0).getTypeInspector().isArray());
      Assert.assertTrue(fields.get(0).hasAnnotation(XmlElement.class));
      
      Assert.assertEquals("b", fields.get(1).getName());
      Assert.assertEquals("java.lang.String", fields.get(1).getQualifiedType());
      Assert.assertEquals("String", fields.get(1).getType());
      Assert.assertFalse(fields.get(1).getTypeInspector().isArray());
      Assert.assertTrue(fields.get(1).hasAnnotation(XmlElement.class));

      Assert.assertEquals("c", fields.get(2).getName());
      Assert.assertEquals("java.lang.String[]", fields.get(2).getQualifiedType());
      Assert.assertEquals("String[]", fields.get(2).getType());
      Assert.assertTrue(fields.get(2).getTypeInspector().isArray());
      Assert.assertTrue(fields.get(2).hasAnnotation(XmlElement.class));
   }
   
   @Test
   public void testMultipleFieldDeclarationWithInitializers() throws Exception
   {
      final JavaClass javaClass = JavaParser.create(JavaClass.class);
      javaClass.addField("private static final String a = \"A\",b =\"B\",c[] = {\"C\"};");
      List<Field<JavaClass>> fields = javaClass.getFields();
      
      Assert.assertEquals(3, fields.size());
      
      Assert.assertEquals("a", fields.get(0).getName());
      Assert.assertEquals("java.lang.String", fields.get(0).getQualifiedType());
      Assert.assertEquals("String", fields.get(0).getType());
      Assert.assertEquals(true, fields.get(0).isPrivate());
      Assert.assertEquals(true, fields.get(0).isStatic());
      Assert.assertEquals("A", fields.get(0).getStringInitializer());
      Assert.assertFalse(fields.get(0).getTypeInspector().isArray());
      
      Assert.assertEquals("b", fields.get(1).getName());
      Assert.assertEquals("java.lang.String", fields.get(1).getQualifiedType());
      Assert.assertEquals("String", fields.get(1).getType());
      Assert.assertEquals(true, fields.get(1).isPrivate());
      Assert.assertEquals(true, fields.get(1).isStatic());
      Assert.assertEquals("B", fields.get(1).getStringInitializer());
      Assert.assertFalse(fields.get(1).getTypeInspector().isArray());
      
      Assert.assertEquals("c", fields.get(2).getName());
      Assert.assertEquals("java.lang.String[]", fields.get(2).getQualifiedType());
      Assert.assertEquals("String[]", fields.get(2).getType());
      Assert.assertEquals(true, fields.get(2).isPrivate());
      Assert.assertEquals(true, fields.get(2).isStatic());
      Assert.assertEquals("{\"C\"}", fields.get(2).getLiteralInitializer());
      Assert.assertTrue(fields.get(2).getTypeInspector().isArray());
   }

}
