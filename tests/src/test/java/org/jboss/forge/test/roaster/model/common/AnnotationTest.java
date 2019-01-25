/*
 * Copyright 2012-2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.Importer;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @param <O> the type where the annotation might are present
 * @param <T> the type of the annotation
 */
public abstract class AnnotationTest<O extends JavaSource<O>, T>
{
   private AnnotationTargetSource<O, T> target;

   protected AnnotationTargetSource<O, T> getTarget()
   {
      return target;
   }

   protected void setTarget(final AnnotationTargetSource<O, T> target)
   {
      this.target = target;
   }

   @Before
   public void reset() throws IOException
   {
      resetTests();
   }

   public abstract void resetTests() throws IOException;

   @Test
   public void testParseAnnotation() 
   {
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(6, annotations.size());
      assertEquals("deprecation", annotations.get(1).getStringValue());
      assertEquals("deprecation", annotations.get(1).getStringValue("value"));
      assertEquals("value", annotations.get(1).getValues().get(0).getName());
      assertEquals("deprecation", annotations.get(1).getValues().get(0).getStringValue());

      assertEquals("unchecked", annotations.get(2).getStringValue("value"));
      assertEquals("unchecked", annotations.get(2).getStringValue());
      assertEquals("value", annotations.get(2).getValues().get(0).getName());
      assertEquals("unchecked", annotations.get(2).getValues().get(0).getStringValue());
      assertEquals("MockAnnotation", annotations.get(3).getName());
      assertEquals("MockNestingAnnotation", annotations.get(4).getName());
      assertEquals("MockNestedAnnotation", annotations.get(4).getAnnotationValue().getName());
      assertEquals("MockContainerAnnotation", annotations.get(5).getName());
      assertEquals("MockContainedAnnotation", annotations.get(5).getAnnotationArrayValue()[0].getName());
   }

   @Test
   public void testAddAnnotation() 
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> annotation = target.addAnnotation().setName("RequestScoped");
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());
      assertEquals(annotation, target.getAnnotations().get(size));
      assertEquals("RequestScoped", annotation.getName());
   }

   @Test
   public void testAddAnonymousAnnotation() 
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> annotation = target.addAnnotation();
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());
      assertEquals(annotation, target.getAnnotations().get(size));
      assertEquals("@MISSING", annotation.toString());
   }

   @Test
   public void testAddAnnotationByClass() 
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> annotation = target.addAnnotation(Test.class);
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());
      assertEquals(annotation, target.getAnnotations().get(size));
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertTrue(target.toString().contains("@" + Test.class.getSimpleName()));
      assertTrue(target.getOrigin().hasImport(Test.class));
   }

   @Test
   public void testAddAnnotationByName() 
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> annotation = target.addAnnotation("RequestScoped");
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());
      assertEquals(annotation, target.getAnnotations().get(size));
      assertEquals("RequestScoped", annotation.getName());
      assertTrue(target.toString().contains("@RequestScoped"));
      assertFalse(target.getOrigin().hasImport("RequestScoped"));
   }

   @Test
   public void testCanAddAnnotationDuplicate() 
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> anno1 = target.addAnnotation(Test.class);
      AnnotationSource<O> anno2 = target.addAnnotation(Test.class);
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 2, annotations.size());
      assertEquals(Test.class.getSimpleName(), anno1.getName());
      assertEquals(Test.class.getSimpleName(), anno2.getName());
      Pattern pattern = Pattern.compile("@" + Test.class.getSimpleName() + "\\s*" + "@" + Test.class.getSimpleName());
      Matcher matcher = pattern.matcher(target.toString());
      assertTrue(matcher.find());
      assertTrue(target.getOrigin().hasImport(Test.class));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCannotAddAnnotationWithIllegalName() 
   {
      target.addAnnotation("sdf*(&#$%");
   }

   @Test
   public void testAddEnumValue() 
   {
      target.addAnnotation(Test.class).setEnumValue(MockEnumType.FOO);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType enumValue = annotation.getEnumValue(MockEnumType.class);
      assertEquals(MockEnumType.FOO, enumValue);
   }

   @Test
   public void testAddEnumNameValue() 
   {
      target.addAnnotation(Test.class).setEnumValue("name", MockEnumType.BAR);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType enumValue = annotation.getEnumValue(MockEnumType.class, "name");
      assertEquals(MockEnumType.BAR, enumValue);
   }

   @Test
   public void testAddEnumArrayValue() 
   {
      target.addAnnotation(Test.class).setEnumValue(MockEnumType.FOO, MockEnumType.BAR);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType[] enumArrayValue = annotation.getEnumArrayValue(MockEnumType.class);
      assertArrayEquals(MockEnumType.values(), enumArrayValue);
   }

   @Test
   public void testAddEnumArrayNameValue() 
   {
      target.addAnnotation(Test.class).setEnumArrayValue("name", MockEnumType.FOO, MockEnumType.BAR);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType[] enumArrayValue = annotation.getEnumArrayValue(MockEnumType.class, "name");
      assertArrayEquals(MockEnumType.values(), enumArrayValue);
   }

   @Test
   public void testAddClassValue() 
   {
      target.addAnnotation(Test.class).setClassValue(Integer.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Integer.class, annotation.getClassValue());

      annotation.setClassValue(int.class);
      assertEquals(int.class, annotation.getClassValue());
   }

   @Test
   public void testAddClassNameValue() 
   {
      target.addAnnotation(Test.class).setClassValue("type", Integer.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Integer.class, annotation.getClassValue("type"));

      annotation.setClassValue("type", int.class);
      assertEquals(int.class, annotation.getClassValue("type"));
   }

   @Test
   public void testAddClassArrayValue() 
   {
      target.addAnnotation(Test.class).setClassArrayValue(Integer.class, int.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertArrayEquals(new Class[] { Integer.class, int.class }, annotation.getClassArrayValue());
   }

   @Test
   public void testAddClassArrayNameValue() 
   {
      target.addAnnotation(Test.class).setClassArrayValue("types", Integer.class, int.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertArrayEquals(new Class[] { Integer.class, int.class }, annotation.getClassArrayValue("types"));
   }

   @Test
   public void testAddNestedAnnotationValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue().setName("com.test.Foo")
               .setEnumValue(ElementType.FIELD, ElementType.METHOD);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@com.test.Foo({ElementType.FIELD,ElementType.METHOD})", annotation.getLiteralValue());

      AnnotationSource<O> nested = annotation.getAnnotationValue();
      assertEquals("com.test.Foo", nested.getName());
      assertEquals("{ElementType.FIELD,ElementType.METHOD}", nested.getLiteralValue());
   }

   @Test
   public void testAddNestedAnnotationNameValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue("foo").setName("com.test.Foo").setStringValue("bar", "baz");

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@com.test.Foo(bar=\"baz\")", annotation.getLiteralValue("foo"));

      AnnotationSource<O> nested = annotation.getAnnotationValue("foo");
      assertEquals("com.test.Foo", nested.getName());
      assertEquals("baz", nested.getStringValue("bar"));
   }

   @Test
   public void testAddNestedAnonymousAnnotationValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue()
               .setEnumValue(ElementType.FIELD, ElementType.METHOD);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@MISSING({ElementType.FIELD,ElementType.METHOD})", annotation.getLiteralValue());

      AnnotationSource<O> nested = annotation.getAnnotationValue();
      assertEquals("MISSING", nested.getName());
      assertEquals("{ElementType.FIELD,ElementType.METHOD}", nested.getLiteralValue());
   }

   @Test
   public void testAddNestedAnonymousAnnotationNameValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue("foo").setStringValue("bar", "baz");

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@MISSING(bar=\"baz\")", annotation.getLiteralValue("foo"));

      AnnotationSource<O> nested = annotation.getAnnotationValue("foo");
      assertEquals("MISSING", nested.getName());
      assertEquals("baz", nested.getStringValue("bar"));
   }

   @Test
   public void testAddDeeplyNestedAnnotationValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue().setName("com.test.Foo")
               .setAnnotationValue().setName("com.test.Bar");

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@com.test.Foo(@com.test.Bar)", annotation.getLiteralValue());

      AnnotationSource<O> deeplyNested = annotation.getAnnotationValue().getAnnotationValue();
      assertEquals("com.test.Bar", deeplyNested.getName());
      assertTrue(deeplyNested.isMarker());
   }

   @Test
   public void testAddDeeplyNestedAnnotationNameValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue("foo").setName("com.test.Foo").setAnnotationValue("bar")
               .setName("com.test.Bar");

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@com.test.Foo(bar=@com.test.Bar)", annotation.getLiteralValue("foo"));

      AnnotationSource<O> deeplyNested = annotation.getAnnotationValue("foo").getAnnotationValue("bar");
      assertEquals("com.test.Bar", deeplyNested.getName());
      assertTrue(deeplyNested.isMarker());
   }

   @Test
   public void testAddDeeplyNestedAnonymousAnnotationValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue().setName("com.test.Foo")
               .setAnnotationValue();

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@com.test.Foo(@MISSING)", annotation.getLiteralValue());

      AnnotationSource<O> deeplyNested = annotation.getAnnotationValue().getAnnotationValue();
      assertEquals("MISSING", deeplyNested.getName());
      assertTrue(deeplyNested.isMarker());
   }

   @Test
   public void testAddDeeplyNestedAnonymousAnnotationNameValue() 
   {
      target.addAnnotation(Test.class).setAnnotationValue("foo").setName("com.test.Foo").setAnnotationValue("bar");

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals("@com.test.Foo(bar=@MISSING)", annotation.getLiteralValue("foo"));

      AnnotationSource<O> deeplyNested = annotation.getAnnotationValue("foo").getAnnotationValue("bar");
      assertEquals("MISSING", deeplyNested.getName());
      assertTrue(deeplyNested.isMarker());
   }

   @Test
   public void testAddLiteralValue() 
   {
      int size = target.getAnnotations().size();

      target.addAnnotation(Test.class).setLiteralValue("435");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertEquals("435", annotation.getLiteralValue());
   }

   @Test
   public void testAddObjectValue() 
   {
      int size = target.getAnnotations().size();

      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class")
               .setLiteralValue("foo", "bar");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertEquals(null, annotation.getLiteralValue());
      assertEquals(RuntimeException.class.getName(), annotation.getLiteralValue("expected"));
      assertEquals("bar", annotation.getLiteralValue("foo"));
   }

   @Test
   public void testAddValueConvertsToNormalAnnotation() 
   {
      target.addAnnotation(Test.class).setLiteralValue("RuntimeException.class");
      AnnotationSource<O> annotation = target.getAnnotations().get(target.getAnnotations().size() - 1);

      assertEquals(RuntimeException.class.getName(), annotation.getLiteralValue());
      assertTrue(annotation.isSingleValue());

      annotation.setLiteralValue("foo", "bar");
      assertFalse(annotation.isSingleValue());
      assertTrue(annotation.isNormal());

      assertEquals(RuntimeException.class.getName(), annotation.getLiteralValue());
      assertEquals(RuntimeException.class.getName(), annotation.getLiteralValue("value"));
      assertEquals("bar", annotation.getLiteralValue("foo"));
   }

   @Test
   public void testAnnotationBeginsAsMarker() 
   {
      AnnotationSource<O> anno = target.addAnnotation(Test.class);
      assertTrue(anno.isMarker());
      assertFalse(anno.isSingleValue());
      assertFalse(anno.isNormal());

      anno.setLiteralValue("\"Foo!\"");
      assertFalse(anno.isMarker());
      assertTrue(anno.isSingleValue());
      assertFalse(anno.isNormal());

      anno.setStringValue("bar", "Foo!");
      assertFalse(anno.isMarker());
      assertFalse(anno.isSingleValue());
      assertTrue(anno.isNormal());

      assertEquals("\"Foo!\"", anno.getLiteralValue("bar"));
      assertEquals("Foo!", anno.getStringValue("bar"));

      anno.removeAllValues();
      assertTrue(anno.isMarker());
      assertFalse(anno.isSingleValue());
      assertFalse(anno.isNormal());
   }

   @Test
   public void testHasAnnotationClassType() 
   {
      target.addAnnotation(Test.class);
      assertTrue(target.hasAnnotation(Test.class));
   }

   @Test
   public void testHasAnnotationStringType() 
   {
      target.addAnnotation(Test.class);
      assertTrue(target.hasAnnotation("Test"));
      assertTrue(target.hasAnnotation(Test.class.getName()));
   }

   @Test
   public void testHasAnnotationStringTypeSimple() 
   {
      target.addAnnotation(Test.class);
      assertNotNull(target.getAnnotation("Test"));
      assertNotNull(target.getAnnotation(Test.class.getSimpleName()));
   }

   @Test
   public void testGetAnnotationClassType() 
   {
      target.addAnnotation(Test.class);
      assertNotNull(target.getAnnotation(Test.class));
   }

   @Test
   public void testGetAnnotationStringType() 
   {
      target.addAnnotation(Test.class);
      assertNotNull(target.getAnnotation("org.junit.Test"));
      assertNotNull(target.getAnnotation(Test.class.getName()));
   }

   @Test
   public void testGetAnnotationStringTypeSimple() 
   {
      target.addAnnotation(Test.class);
      assertTrue(target.hasAnnotation("Test"));
      assertTrue(target.hasAnnotation(Test.class.getSimpleName()));
   }

   @Test
   public void testRemoveAllValues() 
   {
      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      annotation.removeAllValues();

      assertEquals(0, annotation.getValues().size());
   }

   @Test
   public void testGetNames() 
   {
      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class");

      AnnotationSource<O> annotation = target.getAnnotation(Test.class);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertEquals(Test.class.getName(), annotation.getQualifiedName());
   }

   @Test
   public void testReAddObjectValue() 
   {
      int size = target.getAnnotations().size();

      target.addAnnotation(Test.class)
               .setLiteralValue("foo", "bar").setLiteralValue("foo", "baz");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertEquals(null, annotation.getLiteralValue());
      assertEquals("baz", annotation.getLiteralValue("foo"));
   }

   @Test
   public void testParseRegularExp()
   {
      AnnotationSource<O> annotation = target.addAnnotation("MyAnnotation");
      annotation.setStringValue("regexp", "^\\d{9}[\\d|X]$");
      assertEquals("^\\d{9}[\\d|X]$", annotation.getStringValue("regexp"));
   }

   @Test
   public void testStringArrayElement()
   {
      AnnotationSource<O> ann = target.addAnnotation("MyAnnotation");
      String[] values = new String[] { "A", "B", "C" };
      ann.setStringArrayValue(values);
      Assert.assertArrayEquals(values, ann.getStringArrayValue());
   }

   @Test
   public void testNamedStringArrayElement()
   {
      AnnotationSource<O> ann = target.addAnnotation("MyAnnotation");
      String[] values = new String[] { "A", "B", "C" };
      ann.setStringArrayValue("aName", values);
      Assert.assertArrayEquals(values, ann.getStringArrayValue("aName"));
   }

   @Test
   public void testSingleStringArrayElement()
   {
      AnnotationSource<O> ann = target.addAnnotation("MyAnnotation");
      String[] values = new String[] { "A" };
      ann.setStringValue("A");
      Assert.assertArrayEquals(values, ann.getStringArrayValue());
   }

   @Test
   public void testNamedSingleStringArrayElement()
   {
      AnnotationSource<O> ann = target.addAnnotation("MyAnnotation");
      String[] values = new String[] { "A" };
      ann.setStringValue("aName", "A");
      Assert.assertArrayEquals(values, ann.getStringArrayValue("aName"));
   }

   @Test
   public void testCheckValueAreDefined() 
   {
      int size = target.getAnnotations().size();

      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class")
               .setLiteralValue("foo", "bar");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertTrue(annotation.isTypeElementDefined("foo"));
      assertTrue(annotation.isTypeElementDefined("expected"));
      assertFalse(annotation.isTypeElementDefined("missing"));
      assertFalse(annotation.isTypeElementDefined("fooo"));
   }

   @Test
   public void testAddDefaultAnnotationValuesToMarker()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      annotation.removeAllValues();
      assertTrue(annotation.isMarker());
      annotation.addAnnotationValue().setName("MockContainedAnnotation").setLiteralValue("0");
      assertTrue(annotation.isSingleValue());
      assertNotNull(annotation.getAnnotationValue());
      assertEquals("MockContainedAnnotation", annotation.getAnnotationValue().getName());
      assertEquals("0", annotation.getAnnotationValue().getLiteralValue());
      annotation.addAnnotationValue().setName("MockContainedAnnotation").setLiteralValue("1");
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue();
      assertNotNull(values);
      assertEquals(2, values.length);
      for (int i = 0; i < values.length; i++)
      {
         assertEquals("MockContainedAnnotation", values[i].getName());
         assertEquals(Integer.toString(i), values[i].getLiteralValue());
      }
   }

   @Test
   public void testAddNamedDefaultAnnotationValuesToMarker()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      annotation.removeAllValues();
      assertTrue(annotation.isMarker());
      annotation.addAnnotationValue("value").setName("MockContainedAnnotation").setLiteralValue("0");
      assertTrue(annotation.isSingleValue());
      assertNotNull(annotation.getAnnotationValue());
      assertEquals("MockContainedAnnotation", annotation.getAnnotationValue().getName());
      assertEquals("0", annotation.getAnnotationValue().getLiteralValue());
      annotation.addAnnotationValue("value").setName("MockContainedAnnotation").setLiteralValue("1");
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue();
      assertNotNull(values);
      assertEquals(2, values.length);
      for (int i = 0; i < values.length; i++)
      {
         assertEquals("MockContainedAnnotation", values[i].getName());
         assertEquals(Integer.toString(i), values[i].getLiteralValue());
      }
   }

   @Test
   public void testAddNextDefaultAnnotationValue()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      assertTrue(annotation.isSingleValue());
      annotation.addAnnotationValue().setName("MockContainedAnnotation").setLiteralValue("1");
      assertTrue(annotation.isSingleValue());
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue();
      assertNotNull(values);
      assertEquals(2, values.length);
      for (int i = 0; i < values.length; i++)
      {
         assertEquals("MockContainedAnnotation", values[i].getName());
         assertEquals(Integer.toString(i), values[i].getLiteralValue());
      }
   }

   @Test
   public void testAddNextNamedDefaultAnnotationValue()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      assertTrue(annotation.isSingleValue());
      annotation.addAnnotationValue("value").setName("MockContainedAnnotation").setLiteralValue("1");
      assertTrue(annotation.isSingleValue());
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue();
      assertNotNull(values);
      assertEquals(2, values.length);
      for (int i = 0; i < values.length; i++)
      {
         assertEquals("MockContainedAnnotation", values[i].getName());
         assertEquals(Integer.toString(i), values[i].getLiteralValue());
      }
   }

   @Test
   public void testAddDefaultAnnotationValueToEmptyArray()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      assertTrue(annotation.isSingleValue());
      annotation.setLiteralValue("{}");

   }

   @Test
   public void testAddNamedAnnotationValuesToMarker()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      annotation.removeAllValues();
      assertTrue(annotation.isMarker());
      annotation.addAnnotationValue("child").setName("MockContainedAnnotation").setLiteralValue("0");
      assertTrue(annotation.isNormal());
      assertNotNull(annotation.getAnnotationValue("child"));
      assertEquals("MockContainedAnnotation", annotation.getAnnotationValue("child").getName());
      assertEquals("0", annotation.getAnnotationValue("child").getLiteralValue());
      annotation.addAnnotationValue("child").setName("MockContainedAnnotation").setLiteralValue("1");
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue("child");
      assertNotNull(values);
      assertEquals(2, values.length);
      for (int i = 0; i < values.length; i++)
      {
         assertEquals("MockContainedAnnotation", values[i].getName());
         assertEquals(Integer.toString(i), values[i].getLiteralValue());
      }
   }

   @Test
   public void testAddNamedAnnotationValuesToSingleValue()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      assertTrue(annotation.isSingleValue());
      annotation.addAnnotationValue("child").setName("MockContainedAnnotation").setLiteralValue("0");
      assertTrue(annotation.isNormal());
      assertNotNull(annotation.getAnnotationArrayValue());
      assertNotNull(annotation.getAnnotationArrayValue("value"));
      assertNotNull(annotation.getAnnotationValue("child"));
      assertEquals("MockContainedAnnotation", annotation.getAnnotationValue("child").getName());
      assertEquals("0", annotation.getAnnotationValue("child").getLiteralValue());
      annotation.addAnnotationValue("child").setName("MockContainedAnnotation").setLiteralValue("1");
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue("child");
      assertNotNull(values);
      assertEquals(2, values.length);
      for (int i = 0; i < values.length; i++)
      {
         assertEquals("MockContainedAnnotation", values[i].getName());
         assertEquals(Integer.toString(i), values[i].getLiteralValue());
      }
   }

   @Test
   public void testRemoveDefaultAnnotationValue()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      assertTrue(annotation.isSingleValue());
      annotation.addAnnotationValue().setName("MockContainedAnnotation").setLiteralValue("1");
      final AnnotationSource<O>[] values = annotation.getAnnotationArrayValue();
      assertNotNull(values);
      assertEquals(2, values.length);
      assertSame(annotation, annotation.removeAnnotationValue(values[0]));
      final AnnotationSource<O> lastChild = annotation.getAnnotationValue();
      assertNotNull(lastChild);
      assertEquals("MockContainedAnnotation", lastChild.getName());
      assertEquals("1", lastChild.getLiteralValue());
      assertSame(annotation, annotation.removeAnnotationValue(lastChild));
      assertTrue(annotation.isMarker());
   }

   @Test
   public void testRemoveDefaultAnnotationValueFromNormalAnnotation()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      assertTrue(annotation.isSingleValue());
      annotation.setStringValue("foo", "bar");
      assertTrue(annotation.isNormal());
      assertSame(annotation, annotation.removeAnnotationValue(annotation.getAnnotationArrayValue()[0]));
      assertTrue(annotation.isNormal());
      assertNull(annotation.getLiteralValue());
   }

   @Test
   public void testRemoveNamedAnnotationValue()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      annotation.addAnnotationValue("foo").setName("MockContainedAnnotation").setStringValue("foo");
      annotation.addAnnotationValue("foo").setName("MockContainedAnnotation").setStringValue("bar");

      AnnotationSource<O>[] foo = annotation.getAnnotationArrayValue("foo");
      assertNotNull(foo);
      assertEquals(2, foo.length);

      assertEquals("foo", foo[0].getStringValue());
      assertEquals("bar", foo[1].getStringValue());

      annotation.removeAnnotationValue("foo", foo[0]);
      foo = annotation.getAnnotationArrayValue("foo");
      assertNotNull(foo);
      assertEquals(1, foo.length);
      assertEquals("bar", foo[0].getStringValue());
   }

   @Test
   public void testConvertNestedAnnotationArrayElement()
   {
      final AnnotationSource<O> annotation = target.getAnnotation("MockContainerAnnotation");
      // demonstrate that we interpret a single-element array equally to an annotation value:
      final AnnotationSource<O> nested = annotation.getAnnotationValue();
      assertTrue(nested.isSingleValue());
      nested.removeAllValues();
      assertTrue(nested.isMarker());
   }

   @Test
   public void testAddAnnotationArrayElementClass() 
   {
      final AnnotationSource<O> annotation = target.addAnnotation().setName("MyAnnotation");
      annotation.addAnnotationValue(MockAnnotation.class);
      assertTrue(target.getOrigin().hasImport(MockAnnotation.class));
      assertEquals(1, annotation.getAnnotationArrayValue().length);
      assertEquals("MockAnnotation", annotation.getAnnotationArrayValue()[0].getName());
   }

   @Test
   public void testAddAnnotationArrayElementClassNamed() 
   {
      final AnnotationSource<O> annotation = target.addAnnotation().setName("MyAnnotation");
      annotation.addAnnotationValue("nested", MockAnnotation.class);
      assertTrue(target.getOrigin().hasImport(MockAnnotation.class));
      assertEquals(1, annotation.getAnnotationArrayValue("nested").length);
      assertEquals("MockAnnotation", annotation.getAnnotationArrayValue("nested")[0].getName());
   }

   @Test
   public void testAddJavaLangAnnotationShouldNotBeImported()
   {
      target.addAnnotation(Override.class);
      Importer<?> importer = target.getOrigin();
      Assert.assertNull(importer.getImport(Override.class));
   }

   @Test
   public void testGetLiteralValueReturnsFQNInClassAnnotationValue()
   {
      target.addAnnotation(Test.class).setClassValue("expected", IllegalStateException.class);
      AnnotationSource<O> annotation = target.getAnnotation(Test.class);
      Assert.assertEquals(IllegalStateException.class.getName(), annotation.getStringValue("expected"));
   }

   @Test
   public void testSetLiteralValueStructured()
   {
      String annotationValue = "clazz = String.class, method = \"startAnalyse\"";
      AnnotationSource<O> ann = target.addAnnotation(Test.class);
      ann.setLiteralValue(annotationValue);
      Assert.assertEquals("startAnalyse", ann.getStringValue("method"));
      ann.setLiteralValue(annotationValue);
      Assert.assertEquals(String.class, ann.getClassValue("clazz"));
   }

}