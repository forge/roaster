/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.parser.java;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.AnnotationElement;
import org.jboss.forge.parser.java.JavaAnnotation;
import org.jboss.forge.parser.java.SourceType;
import org.jboss.forge.parser.java.Type;
import org.jboss.forge.test.parser.java.common.MockEnumType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Matt Benson
 */
public class JavaAnnotationTest
{
   private JavaAnnotation javaAnnotation;

   @Before
   public void setup()
   {
      InputStream stream =
               JavaAnnotationTest.class
                        .getResourceAsStream("/org/jboss/forge/grammar/java/MockJavaAnnotationType.java");
      javaAnnotation = JavaParser.parse(JavaAnnotation.class, stream);
   }

   @Test
   public void testCanParseOuterAnnotation() throws Exception
   {
      assertEquals("MockJavaAnnotationType", javaAnnotation.getName());
      assertEquals(1, javaAnnotation.getAnnotationElements().size());

      assertTrue(javaAnnotation.hasAnnotationElement("value"));
      AnnotationElement value = javaAnnotation.getAnnotationElement("value");
      assertTrue(javaAnnotation.hasAnnotationElement(value));
      assertEquals("value", value.getName());
      assertEquals("org.jboss.forge.grammar.java.MockNestedJavaAnnotationType",
               value.getTypeInspector().getQualifiedName());
      Annotation<JavaAnnotation> valueDefaultValueAnnotation = value.getDefaultValue().getAnnotation();
      assertEquals("MockNestedJavaAnnotationType",
               valueDefaultValueAnnotation.getName());
      assertTrue(valueDefaultValueAnnotation.isSingleValue());
      assertEquals("-1", valueDefaultValueAnnotation.getLiteralValue());
   }

   @Test
   public void testCanParseInnerAnnotation() throws Exception
   {
      assertEquals(1, javaAnnotation.getNestedClasses().size());
      assertSame(SourceType.ANNOTATION,
               javaAnnotation.getNestedClasses().get(0).getSourceType());
      JavaAnnotation nestedAnnotation = (JavaAnnotation) javaAnnotation.getNestedClasses().get(0);
      assertEquals("MockNestedJavaAnnotationType", nestedAnnotation.getName());
      assertEquals(5, nestedAnnotation.getAnnotationElements().size());

      assertTrue(nestedAnnotation.hasAnnotationElement("value"));
      AnnotationElement value = nestedAnnotation.getAnnotationElement("value");
      assertTrue(nestedAnnotation.hasAnnotationElement(value));
      assertEquals("value", value.getName());
      assertEquals("int", value.getType());
      assertTrue(value.getTypeInspector().isPrimitive());
      assertNull(value.getDefaultValue().getLiteral());

      assertTrue(nestedAnnotation.hasAnnotationElement("charSequenceType"));
      AnnotationElement charSequenceType = nestedAnnotation.getAnnotationElement("charSequenceType");
      assertTrue(nestedAnnotation.hasAnnotationElement(charSequenceType));
      assertEquals("charSequenceType", charSequenceType.getName());
      Type<JavaAnnotation> charSequenceTypeType = charSequenceType.getTypeInspector();
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
      AnnotationElement metasyntacticVariable = nestedAnnotation.getAnnotationElement("metasyntacticVariable");
      assertTrue(nestedAnnotation.hasAnnotationElement(metasyntacticVariable));
      assertEquals("metasyntacticVariable", metasyntacticVariable.getName());
      Type<JavaAnnotation> metasyntacticVariableType = metasyntacticVariable.getTypeInspector();
      assertEquals("org.jboss.forge.test.parser.java.common.MockEnumType", metasyntacticVariableType.getQualifiedName());
      assertFalse(metasyntacticVariableType.isArray());
      assertEquals(1, metasyntacticVariable.getAnnotations().size());
      assertEquals("Deprecated",
               metasyntacticVariable.getAnnotations().get(0).getName());
      assertSame(MockEnumType.FOO, metasyntacticVariable.getDefaultValue().getEnum(MockEnumType.class));

      assertTrue(nestedAnnotation.hasAnnotationElement("numberTypes"));
      AnnotationElement numberTypes = nestedAnnotation.getAnnotationElement("numberTypes");
      assertTrue(nestedAnnotation.hasAnnotationElement(numberTypes));
      assertEquals("numberTypes", numberTypes.getName());
      Type<JavaAnnotation> numberTypesType = numberTypes.getTypeInspector();
      assertEquals(Class.class.getSimpleName() + "[]", numberTypesType.getName());
      assertTrue(numberTypesType.isParameterized());
      assertEquals(1, numberTypesType.getTypeArguments().size());
      assertEquals("? extends Number", numberTypesType.getTypeArguments().get(0).getName());
      assertTrue(numberTypesType.getTypeArguments().get(0).isWildcard());
      assertEquals(0, numberTypes.getDefaultValue().getClassArray().length);
      numberTypes.getDefaultValue().setClassArray(Long.class, Double.class);
      assertArrayEquals(new Class[] { Long.class, Double.class }, numberTypes.getDefaultValue().getClassArray());

      assertTrue(nestedAnnotation.hasAnnotationElement("metasyntacticVariables"));
      AnnotationElement metasyntacticVariables = nestedAnnotation.getAnnotationElement("metasyntacticVariables");
      assertTrue(nestedAnnotation.hasAnnotationElement(metasyntacticVariables));
      assertEquals("metasyntacticVariables", metasyntacticVariables.getName());
      Type<JavaAnnotation> metasyntacticVariablesType = metasyntacticVariables.getTypeInspector();
      assertEquals("org.jboss.forge.test.parser.java.common.MockEnumType", metasyntacticVariablesType.getQualifiedName());
      assertTrue(metasyntacticVariablesType.isArray());
      assertEquals(0, metasyntacticVariables.getDefaultValue().getEnumArray(MockEnumType.class).length);
      metasyntacticVariables.getDefaultValue().setEnumArray(MockEnumType.values());
      assertArrayEquals(MockEnumType.values(), metasyntacticVariables.getDefaultValue().getEnumArray(MockEnumType.class));
   }

   @Test
   public void testAddAnnotationElement()
   {
      AnnotationElement someElement = javaAnnotation.addAnnotationElement().setName("someElement")
               .setType(String[].class);
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotation> someElementType = someElement.getTypeInspector();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());
   }

   @Test
   public void testAddAnnotationElementFromDeclaration()
   {
      AnnotationElement someElement = javaAnnotation.addAnnotationElement("String[] someElement();");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotation> someElementType = someElement.getTypeInspector();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());
   }

   @Test
   public void testAddAnnotationElementFromDeclarationWithDefaultValue()
   {
      AnnotationElement someElement = javaAnnotation
               .addAnnotationElement("String[] someElement() default {\"A\",\"B\",\"C\"};");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotation> someElementType = someElement.getTypeInspector();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());

      assertEquals("{\"A\",\"B\",\"C\"}", someElement.getDefaultValue().getLiteral());
   }

   @Test
   public void testAddAnnotationElementFromDeclarationNoSemi()
   {
      AnnotationElement someElement = javaAnnotation.addAnnotationElement("String[] someElement()");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotation> someElementType = someElement.getTypeInspector();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());
   }

   @Test
   public void testAddAnnotationElementFromDeclarationWithDefaultValueNoSemi()
   {
      AnnotationElement someElement = javaAnnotation
               .addAnnotationElement("String[] someElement() default {\"A\",\"B\",\"C\"}");
      assertTrue(javaAnnotation.hasAnnotationElement(someElement));

      assertEquals("someElement", someElement.getName());
      Type<JavaAnnotation> someElementType = someElement.getTypeInspector();
      assertTrue(someElementType.isArray());
      assertEquals(1, someElementType.getArrayDimensions());
      assertEquals("String[]", someElementType.getName());

      assertEquals("{\"A\",\"B\",\"C\"}", someElement.getDefaultValue().getLiteral());
   }

   @Test
   public void testRemoveAnnotationElement()
   {
      AnnotationElement annotationElement = javaAnnotation.getAnnotationElements().get(0);
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
      AnnotationElement value = javaAnnotation.getAnnotationElement("value");
      Annotation<JavaAnnotation> valueDefaultValue = value.getDefaultValue().getAnnotation();
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
      assertEquals("StringBuffer.class", valueDefaultValue.getLiteralValue("charSequenceType"));
      assertEquals(MockEnumType.BAR, valueDefaultValue.getEnumValue(MockEnumType.class, "metasyntacticVariable"));
   }

}
