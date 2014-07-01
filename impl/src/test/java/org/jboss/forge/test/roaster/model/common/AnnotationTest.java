/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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
   public void reset()
   {
      resetTests();
   }

   public abstract void resetTests();

   @Test
   public void testParseAnnotation() throws Exception
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
   public void testAddAnnotation() throws Exception
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> annotation = target.addAnnotation().setName("RequestScoped");
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());
      assertEquals(annotation, target.getAnnotations().get(size));
      assertEquals("RequestScoped", annotation.getName());
   }

   @Test
   public void testAddAnonymousAnnotation() throws Exception
   {
      int size = target.getAnnotations().size();
      AnnotationSource<O> annotation = target.addAnnotation();
      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());
      assertEquals(annotation, target.getAnnotations().get(size));
      assertEquals("@MISSING", annotation.toString());
   }

   @Test
   public void testAddAnnotationByClass() throws Exception
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
   public void testAddAnnotationByName() throws Exception
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
   public void testCanAddAnnotationDuplicate() throws Exception
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
   public void testCannotAddAnnotationWithIllegalName() throws Exception
   {
      target.addAnnotation("sdf*(&#$%");
   }

   @Test
   public void testAddEnumValue() throws Exception
   {
      target.addAnnotation(Test.class).setEnumValue(MockEnumType.FOO);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType enumValue = annotation.getEnumValue(MockEnumType.class);
      assertEquals(MockEnumType.FOO, enumValue);
   }

   @Test
   public void testAddEnumNameValue() throws Exception
   {
      target.addAnnotation(Test.class).setEnumValue("name", MockEnumType.BAR);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType enumValue = annotation.getEnumValue(MockEnumType.class, "name");
      assertEquals(MockEnumType.BAR, enumValue);
   }

   @Test
   public void testAddEnumArrayValue() throws Exception
   {
      target.addAnnotation(Test.class).setEnumValue(MockEnumType.FOO, MockEnumType.BAR);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType[] enumArrayValue = annotation.getEnumArrayValue(MockEnumType.class);
      assertArrayEquals(MockEnumType.values(), enumArrayValue);
   }

   @Test
   public void testAddEnumArrayNameValue() throws Exception
   {
      target.addAnnotation(Test.class).setEnumArrayValue("name", MockEnumType.FOO, MockEnumType.BAR);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      MockEnumType[] enumArrayValue = annotation.getEnumArrayValue(MockEnumType.class, "name");
      assertArrayEquals(MockEnumType.values(), enumArrayValue);
   }

   @Test
   public void testAddClassValue() throws Exception
   {
      target.addAnnotation(Test.class).setClassValue(Integer.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Integer.class, annotation.getClassValue());

      annotation.setClassValue(int.class);
      assertEquals(int.class, annotation.getClassValue());
   }

   @Test
   public void testAddClassNameValue() throws Exception
   {
      target.addAnnotation(Test.class).setClassValue("type", Integer.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Integer.class, annotation.getClassValue("type"));

      annotation.setClassValue("type", int.class);
      assertEquals(int.class, annotation.getClassValue("type"));
   }

   @Test
   public void testAddClassArrayValue() throws Exception
   {
      target.addAnnotation(Test.class).setClassArrayValue(Integer.class, int.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertArrayEquals(new Class[] { Integer.class, int.class }, annotation.getClassArrayValue());
   }

   @Test
   public void testAddClassArrayNameValue() throws Exception
   {
      target.addAnnotation(Test.class).setClassArrayValue("types", Integer.class, int.class);

      List<AnnotationSource<O>> annotations = target.getAnnotations();

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertArrayEquals(new Class[] { Integer.class, int.class }, annotation.getClassArrayValue("types"));
   }

   @Test
   public void testAddNestedAnnotationValue() throws Exception
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
   public void testAddNestedAnnotationNameValue() throws Exception
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
   public void testAddNestedAnonymousAnnotationValue() throws Exception
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
   public void testAddNestedAnonymousAnnotationNameValue() throws Exception
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
   public void testAddDeeplyNestedAnnotationValue() throws Exception
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
   public void testAddDeeplyNestedAnnotationNameValue() throws Exception
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
   public void testAddDeeplyNestedAnonymousAnnotationValue() throws Exception
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
   public void testAddDeeplyNestedAnonymousAnnotationNameValue() throws Exception
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
   public void testAddLiteralValue() throws Exception
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
   public void testAddObjectValue() throws Exception
   {
      int size = target.getAnnotations().size();

      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class")
               .setLiteralValue("foo", "bar");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      assertEquals(size + 1, annotations.size());

      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertEquals(null, annotation.getLiteralValue());
      assertEquals("RuntimeException.class", annotation.getLiteralValue("expected"));
      assertEquals("bar", annotation.getLiteralValue("foo"));
   }

   @Test
   public void testAddValueConvertsToNormalAnnotation() throws Exception
   {
      target.addAnnotation(Test.class).setLiteralValue("RuntimeException.class");
      AnnotationSource<O> annotation = target.getAnnotations().get(target.getAnnotations().size() - 1);

      assertEquals("RuntimeException.class", annotation.getLiteralValue());
      assertTrue(annotation.isSingleValue());

      annotation.setLiteralValue("foo", "bar");
      assertFalse(annotation.isSingleValue());
      assertTrue(annotation.isNormal());

      assertEquals("RuntimeException.class", annotation.getLiteralValue());
      assertEquals("RuntimeException.class", annotation.getLiteralValue("value"));
      assertEquals("bar", annotation.getLiteralValue("foo"));
   }

   @Test
   public void testAnnotationBeginsAsMarker() throws Exception
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
   public void testHasAnnotationClassType() throws Exception
   {
      target.addAnnotation(Test.class);
      assertTrue(target.hasAnnotation(Test.class));
   }

   @Test
   public void testHasAnnotationStringType() throws Exception
   {
      target.addAnnotation(Test.class);
      assertTrue(target.hasAnnotation("Test"));
      assertTrue(target.hasAnnotation(Test.class.getName()));
   }

   @Test
   public void testHasAnnotationStringTypeSimple() throws Exception
   {
      target.addAnnotation(Test.class);
      assertNotNull(target.getAnnotation("Test"));
      assertNotNull(target.getAnnotation(Test.class.getSimpleName()));
   }

   @Test
   public void testGetAnnotationClassType() throws Exception
   {
      target.addAnnotation(Test.class);
      assertNotNull(target.getAnnotation(Test.class));
   }

   @Test
   public void testGetAnnotationStringType() throws Exception
   {
      target.addAnnotation(Test.class);
      assertNotNull(target.getAnnotation("org.junit.Test"));
      assertNotNull(target.getAnnotation(Test.class.getName()));
   }

   @Test
   public void testGetAnnotationStringTypeSimple() throws Exception
   {
      target.addAnnotation(Test.class);
      assertTrue(target.hasAnnotation("Test"));
      assertTrue(target.hasAnnotation(Test.class.getSimpleName()));
   }

   @Test
   public void testRemoveAllValues() throws Exception
   {
      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class");

      List<AnnotationSource<O>> annotations = target.getAnnotations();
      AnnotationSource<O> annotation = annotations.get(annotations.size() - 1);
      annotation.removeAllValues();

      assertEquals(0, annotation.getValues().size());
   }

   @Test
   public void testGetNames() throws Exception
   {
      target.addAnnotation(Test.class).setLiteralValue("expected", "RuntimeException.class");

      AnnotationSource<O> annotation = target.getAnnotation(Test.class);
      assertEquals(Test.class.getSimpleName(), annotation.getName());
      assertEquals(Test.class.getName(), annotation.getQualifiedName());
   }

   @Test
   public void testReAddObjectValue() throws Exception
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

}
