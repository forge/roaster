/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.AnnotationElementSource;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaAnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.test.roaster.model.common.MockEnumType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Matt Benson
 */
public class JavaAnnotationTest
{
   private JavaAnnotationSource javaAnnotation;

   @Before
   public void setup() throws IOException
   {
      String fileName = "/org/jboss/forge/grammar/java/MockJavaAnnotationType.java";
      try (InputStream stream = JavaAnnotationTest.class.getResourceAsStream(fileName))
      {
         javaAnnotation = Roaster.parse(JavaAnnotationSource.class, stream);
      }
   }

   @Test
   public void testCanParseOuterAnnotation()
   {
      assertEquals("MockJavaAnnotationType", javaAnnotation.getName());
      assertEquals(1, javaAnnotation.getAnnotationElements().size());

      assertTrue(javaAnnotation.hasAnnotationElement("value"));
      AnnotationElementSource value = javaAnnotation.getAnnotationElement("value");
      assertTrue(javaAnnotation.hasAnnotationElement(value));
      assertEquals("value", value.getName());
      assertEquals("org.jboss.forge.grammar.java.MockNestedJavaAnnotationType",
               value.getType().getQualifiedName());
      AnnotationSource<JavaAnnotationSource> valueDefaultValueAnnotation = value.getDefaultValue().getAnnotation();
      assertEquals("MockNestedJavaAnnotationType",
               valueDefaultValueAnnotation.getName());
      assertTrue(valueDefaultValueAnnotation.isSingleValue());
      assertEquals("-1", valueDefaultValueAnnotation.getLiteralValue());
   }

   @Test
   public void testCanParseInnerAnnotation()
   {
      assertEquals(1, javaAnnotation.getNestedTypes().size());
      JavaAnnotationSource nestedAnnotation = (JavaAnnotationSource) javaAnnotation.getNestedTypes().get(0);
      assertEquals("MockNestedJavaAnnotationType", nestedAnnotation.getName());
      assertEquals(5, nestedAnnotation.getAnnotationElements().size());

      assertTrue(nestedAnnotation.hasAnnotationElement("value"));
      AnnotationElementSource value = nestedAnnotation.getAnnotationElement("value");
      assertTrue(nestedAnnotation.hasAnnotationElement(value));
      assertEquals("value", value.getName());
      assertEquals("int", value.getType().getName());
      assertTrue(value.getType().isPrimitive());
      assertNull(value.getDefaultValue().getLiteral());

      assertTrue(nestedAnnotation.hasAnnotationElement("charSequenceType"));
      AnnotationElementSource charSequenceType = nestedAnnotation.getAnnotationElement("charSequenceType");
      assertTrue(nestedAnnotation.hasAnnotationElement(charSequenceType));
      assertEquals("charSequenceType", charSequenceType.getName());
      Type<JavaAnnotationSource> charSequenceTypeType = charSequenceType.getType();
      assertEquals(Class.class.getSimpleName(), charSequenceTypeType.getName());
      assertTrue(charSequenceTypeType.isParameterized());
      assertEquals(1,
               charSequenceTypeType.getTypeArguments().size());
      assertEquals("? extends CharSequence",
               charSequenceTypeType.getTypeArguments().get(0).getName());
      assertTrue(charSequenceTypeType.getTypeArguments().get(0).isWildcard());
      assertEquals("String.class", charSequenceType.getDefaultValue().getLiteral());
      assertEquals(String.class, charSequenceType.getDefaultValue().getSingleClass());

      assertTrue(nestedAnnotation.hasAnnotationElement("metasyntacticVariable"));
      AnnotationElementSource metasyntacticVariable = nestedAnnotation.getAnnotationElement("metasyntacticVariable");
      assertTrue(nestedAnnotation.hasAnnotationElement(metasyntacticVariable));
      assertEquals("metasyntacticVariable", metasyntacticVariable.getName());
      Type<JavaAnnotationSource> metasyntacticVariableType = metasyntacticVariable.getType();
      assertEquals("org.jboss.forge.test.roaster.model.common.MockEnumType",
               metasyntacticVariableType.getQualifiedName());
      assertFalse(metasyntacticVariableType.isArray());
      assertEquals(1, metasyntacticVariable.getAnnotations().size());
      assertEquals("Deprecated",
               metasyntacticVariable.getAnnotations().get(0).getName());
      assertSame(MockEnumType.FOO, metasyntacticVariable.getDefaultValue().getEnum(MockEnumType.class));

      assertTrue(nestedAnnotation.hasAnnotationElement("numberTypes"));
      AnnotationElementSource numberTypes = nestedAnnotation.getAnnotationElement("numberTypes");
      assertTrue(nestedAnnotation.hasAnnotationElement(numberTypes));
      assertEquals("numberTypes", numberTypes.getName());
      Type<JavaAnnotationSource> numberTypesType = numberTypes.getType();
      assertEquals(Class.class.getSimpleName() + "[]", numberTypesType.getName());
      assertTrue(numberTypesType.isParameterized());
      assertEquals(1, numberTypesType.getTypeArguments().size());
      assertEquals("? extends Number", numberTypesType.getTypeArguments().get(0).getName());
      assertTrue(numberTypesType.getTypeArguments().get(0).isWildcard());
      assertEquals(0, numberTypes.getDefaultValue().getClassArray().length);
      numberTypes.getDefaultValue().setClassArray(Long.class, Double.class);
      assertArrayEquals(new Class[] { Long.class, Double.class }, numberTypes.getDefaultValue().getClassArray());

      assertTrue(nestedAnnotation.hasAnnotationElement("metasyntacticVariables"));
      AnnotationElementSource metasyntacticVariables = nestedAnnotation.getAnnotationElement("metasyntacticVariables");
      assertTrue(nestedAnnotation.hasAnnotationElement(metasyntacticVariables));
      assertEquals("metasyntacticVariables", metasyntacticVariables.getName());
      Type<JavaAnnotationSource> metasyntacticVariablesType = metasyntacticVariables.getType();
      assertEquals("org.jboss.forge.test.roaster.model.common.MockEnumType",
               metasyntacticVariablesType.getQualifiedName());
      assertTrue(metasyntacticVariablesType.isArray());
      assertEquals(0, metasyntacticVariables.getDefaultValue().getEnumArray(MockEnumType.class).length);
      metasyntacticVariables.getDefaultValue().setEnumArray(MockEnumType.values());
      assertArrayEquals(MockEnumType.values(), metasyntacticVariables.getDefaultValue()
               .getEnumArray(MockEnumType.class));
   }

   @Test
   public void testAddAnnotationElement()
   {
      AnnotationElementSource someElement = javaAnnotation.addAnnotationElement().setName("someElement")
               .setType(String[].class);
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotationSource> someElementType = someElement.getType();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());
   }

   @Test
   public void testAddAnnotationElementFromDeclaration()
   {
      AnnotationElementSource someElement = javaAnnotation.addAnnotationElement("String[] someElement();");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotationSource> someElementType = someElement.getType();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());
   }

   @Test
   public void testAddAnnotationElementFromDeclarationWithDefaultValue()
   {
      AnnotationElementSource someElement = javaAnnotation
               .addAnnotationElement("String[] someElement() default {\"A\",\"B\",\"C\"};");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotationSource> someElementType = someElement.getType();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());

      assertEquals("{\"A\",\"B\",\"C\"}", someElement.getDefaultValue().getLiteral());
   }

   @Test
   public void testAddAnnotationElementFromDeclarationNoSemi()
   {
      AnnotationElementSource someElement = javaAnnotation.addAnnotationElement("String[] someElement()");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotationSource> someElementType = someElement.getType();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());
   }

   @Test
   public void testAddAnnotationElementFromDeclarationWithDefaultValueNoSemi()
   {
      AnnotationElementSource someElement = javaAnnotation
               .addAnnotationElement("String[] someElement() default {\"A\",\"B\",\"C\"}");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotationSource> someElementType = someElement.getType();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());

      assertEquals("{\"A\",\"B\",\"C\"}", someElement.getDefaultValue().getLiteral());
   }

   @Test
   public void testRemoveAnnotationElement()
   {
      AnnotationElementSource annotationElement = javaAnnotation.getAnnotationElements().get(0);
      assertTrue(javaAnnotation.hasAnnotationElement(annotationElement));
      javaAnnotation.removeAnnotationElement(annotationElement);
      assertFalse(javaAnnotation.hasAnnotationElement(annotationElement));
   }

   @Test
   public void testClearDefaultValue()
   {
      assertNotNull(javaAnnotation.getAnnotationElement("value").getDefaultValue().getAnnotation());
      javaAnnotation.getAnnotationElement("value").getDefaultValue().setLiteral(null);
      assertNull(javaAnnotation.getAnnotationElement("value").getDefaultValue().getAnnotation());
      assertNull(javaAnnotation.getAnnotationElement("value").getDefaultValue().getLiteral());
   }

   @Test
   public void testChangeDefaultValue()
   {
      AnnotationElementSource value = javaAnnotation.getAnnotationElement("value");
      AnnotationSource<JavaAnnotationSource> valueDefaultValue = value.getDefaultValue().getAnnotation();
      assertNotNull(valueDefaultValue);
      assertEquals("MockNestedJavaAnnotationType", valueDefaultValue.getName());
      assertTrue(valueDefaultValue.isSingleValue());
      assertEquals("-1", valueDefaultValue.getLiteralValue());

      value.getDefaultValue().setAnnotation().setName("MockNestedJavaAnnotationType").setLiteralValue("0")
               .setLiteralValue("charSequenceType", "StringBuffer.class")
               .setEnumValue("metasyntacticVariable", MockEnumType.BAR);
      valueDefaultValue = value.getDefaultValue().getAnnotation();
      assertNotNull(valueDefaultValue);
      assertEquals("MockNestedJavaAnnotationType", valueDefaultValue.getName());
      assertTrue(valueDefaultValue.isNormal());
      assertEquals("0", valueDefaultValue.getLiteralValue());
      assertEquals(StringBuffer.class.getName(), valueDefaultValue.getLiteralValue("charSequenceType"));
      assertEquals(MockEnumType.BAR, valueDefaultValue.getEnumValue(MockEnumType.class, "metasyntacticVariable"));
   }

   @Test
   public void testAnnotationGetStringArrayValueShouldNotReturnValueIfEmpty()
   {
      String data = "@Test(test = {}) public class AnnotationTest {}";
      JavaClassSource type = Roaster.parse(JavaClassSource.class, data);
      AnnotationSource<JavaClassSource> ann = type.getAnnotation("Test");
      assertThat(ann, notNullValue());
      String[] arrayValue = ann.getStringArrayValue("test");
      assertThat(arrayValue.length, equalTo(0));
   }

}
