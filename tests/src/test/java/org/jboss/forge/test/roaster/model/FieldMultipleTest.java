/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldMultipleTest
{
   @Test
   public void testFieldTypesByteExtraDimensionDeclaration()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      final FieldSource<JavaClassSource> field = javaClass.addField("public byte content1[], content2;");
      assertEquals(2, javaClass.getFields().size());
      assertEquals("content1", field.getName());
      assertEquals("byte[]", field.getType().getName());
      assertEquals("byte", field.getType().getQualifiedName());
      assertTrue(field.getType().isArray());
      assertEquals(1, field.getType().getArrayDimensions());
   }

   @Test
   public void testMultipleFieldDeclaration()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addField("public String a,b,c[];");
      List<FieldSource<JavaClassSource>> fields = javaClass.getFields();

      assertEquals(3, fields.size());

      assertEquals("a", fields.get(0).getName());
      assertEquals("java.lang.String", fields.get(0).getType().getQualifiedName());
      assertEquals("String", fields.get(0).getType().getName());
      assertEquals(true, fields.get(0).isPublic());
      assertFalse(fields.get(0).getType().isArray());

      assertEquals("b", fields.get(1).getName());
      assertEquals("java.lang.String", fields.get(1).getType().getQualifiedName());
      assertEquals("String", fields.get(1).getType().getName());
      assertEquals(true, fields.get(1).isPublic());
      assertFalse(fields.get(1).getType().isArray());

      assertEquals("c", fields.get(2).getName());
      assertEquals("java.lang.String", fields.get(2).getType().getQualifiedName());
      assertEquals("String[]", fields.get(2).getType().getName());
      assertEquals(true, fields.get(2).isPublic());
      assertTrue(fields.get(2).getType().isArray());
      assertEquals(1, fields.get(2).getType().getArrayDimensions());
   }

   @Test
   public void testMultipleFieldDeclarationWithAnnotation()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addField("@javax.xml.bind.annotation.XmlElement public String a,b,c[];");
      List<FieldSource<JavaClassSource>> fields = javaClass.getFields();

      assertEquals(3, fields.size());

      assertEquals("a", fields.get(0).getName());
      assertEquals("java.lang.String", fields.get(0).getType().getQualifiedName());
      assertEquals("String", fields.get(0).getType().getName());
      assertFalse(fields.get(0).getType().isArray());
      assertTrue(fields.get(0).hasAnnotation("javax.xml.bind.annotation.XmlElement"));

      assertEquals("b", fields.get(1).getName());
      assertEquals("java.lang.String", fields.get(1).getType().getQualifiedName());
      assertEquals("String", fields.get(1).getType().getName());
      assertFalse(fields.get(1).getType().isArray());
      assertTrue(fields.get(1).hasAnnotation("javax.xml.bind.annotation.XmlElement"));

      assertEquals("c", fields.get(2).getName());
      assertEquals("java.lang.String", fields.get(2).getType().getQualifiedName());
      assertEquals("String[]", fields.get(2).getType().getName());
      assertTrue(fields.get(2).getType().isArray());
      assertEquals(1, fields.get(2).getType().getArrayDimensions());
      assertTrue(fields.get(2).hasAnnotation("javax.xml.bind.annotation.XmlElement"));
   }

   @Test
   public void testMultipleFieldDeclarationWithInitializers()
   {
      final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
      javaClass.addField("private static final String a = \"A\",b =\"B\",c[] = {\"C\"};");
      List<FieldSource<JavaClassSource>> fields = javaClass.getFields();

      assertEquals(3, fields.size());

      assertEquals("a", fields.get(0).getName());
      assertEquals("java.lang.String", fields.get(0).getType().getQualifiedName());
      assertEquals("String", fields.get(0).getType().getName());
      assertEquals(true, fields.get(0).isPrivate());
      assertEquals(true, fields.get(0).isStatic());
      assertEquals("A", fields.get(0).getStringInitializer());
      assertFalse(fields.get(0).getType().isArray());

      assertEquals("b", fields.get(1).getName());
      assertEquals("java.lang.String", fields.get(1).getType().getQualifiedName());
      assertEquals("String", fields.get(1).getType().getName());
      assertEquals(true, fields.get(1).isPrivate());
      assertEquals(true, fields.get(1).isStatic());
      assertEquals("B", fields.get(1).getStringInitializer());
      assertFalse(fields.get(1).getType().isArray());

      assertEquals("c", fields.get(2).getName());
      assertEquals("java.lang.String", fields.get(2).getType().getQualifiedName());
      assertEquals("String[]", fields.get(2).getType().getName());
      assertEquals(true, fields.get(2).isPrivate());
      assertEquals(true, fields.get(2).isStatic());
      assertEquals("{\"C\"}", fields.get(2).getLiteralInitializer());
      assertTrue(fields.get(2).getType().isArray());
      assertEquals(1, fields.get(2).getType().getArrayDimensions());
   }

    @Test
    public void testMultipleFieldDeclarationParse()
    {
        JavaClassSource javaClassSource = Roaster.parse(JavaClassSource.class, "public class Test { private String a,b; }");
        assertThat(javaClassSource.getFields()).hasSize(2).extracting(FieldSource::getName).containsExactly("a", "b");
    }
}
