/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.model.source.PropertyHolderSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class PropertiesTest<O extends JavaSource<O> & PropertyHolderSource<O>>
{
   public enum PropertyComponent
   {
      FIELD
               {
                  @Override
                  String format(Class<?> type, String s)
                  {
                     return s;
                  }
               },
      ACCESSOR
               {
                  @Override
                  String format(Class<?> type, String s)
                  {
                     return (boolean.class.equals(type) ? "is" : "get") + StringUtils.capitalize(s);
                  }
               },
      MUTATOR
               {
                  @Override
                  String format(Class<?> type, String s)
                  {
                     return "set" + StringUtils.capitalize(s);
                  }
               };

      abstract String format(Class<?> type, String s);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testHasProperty(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assertTrue(source.hasProperty(testContext.name));
      assertFalse(source.hasProperty("noSuchProperty"));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testGetPropertyByName(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      PropertySource<O> property = source.getProperty(testContext.name);
      assertEquals(testContext.name, property.getName());
      assertTrue(property.getType().isType(testContext.type));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testIsAccessible(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assertEquals(testContext.existingItems.contains(PropertyComponent.ACCESSOR),
               source.getProperty(testContext.name).isAccessible());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testIsMutable(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assertEquals(testContext.existingItems.contains(PropertyComponent.MUTATOR),
               source.getProperty(testContext.name).isMutable());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testHasField(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assertEquals(testContext.existingItems.contains(PropertyComponent.FIELD),
               source.getProperty(testContext.name).hasField());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testGetField(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      final FieldSource<O> field = source.getProperty(testContext.name).getField();

      if (!testContext.existingItems.contains(PropertyComponent.FIELD))
      {
         assertNull(field);
         return;
      }

      assertNotNull(field);
      assertEquals(testContext.name, field.getName());
      assertTrue(field.getType().isType(testContext.type));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testGetAccessor(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      final MethodSource<O> accessor = source.getProperty(testContext.name).getAccessor();

      if (!testContext.existingItems.contains(PropertyComponent.ACCESSOR))
      {
         assertNull(accessor);
         return;
      }
      assertNotNull(accessor);

      assertEquals(PropertyComponent.ACCESSOR.format(testContext.type, testContext.name), accessor.getName());
      assertTrue(accessor.getReturnType().isType(testContext.type));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testGetMutator(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      final MethodSource<O> mutator = source.getProperty(testContext.name).getMutator();

      if (!testContext.existingItems.contains(PropertyComponent.MUTATOR))
      {
         assertNull(mutator);
         return;
      }
      assertNotNull(mutator);

      assertEquals(PropertyComponent.MUTATOR.format(testContext.type, testContext.name), mutator.getName());
      assertTrue(mutator.isReturnTypeVoid());
      assertEquals(1, mutator.getParameters().size());
      assertTrue(mutator.getParameters().get(0).getType().isType(testContext.type));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetAccessibleTrue(TestContext<O> testContext)
   {
      assumeFalse(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      property.setAccessible(true);
      assertTrue(property.isAccessible());

      final MethodSource<O> accessor = property.getAccessor();
      assertNotNull(accessor);
      assertTrue(source.hasMethod(accessor));
      assertTrue(source.isInterface() || accessor.isPublic());
      assertTrue(accessor.getReturnType().isType(testContext.type));
      assertEquals(PropertyComponent.ACCESSOR.format(testContext.type, testContext.name), accessor.getName());
      assertTrue(accessor.getParameters().isEmpty());
      assertTrue(!testContext.existingItems.contains(PropertyComponent.FIELD)
               || accessor.getBody().contains(String.format("return %s;", testContext.name)));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetMutableTrue(TestContext<O> testContext)
   {
      assumeFalse(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      property.setMutable(true);

      final MethodSource<O> mutator = property.getMutator();
      assertNotNull(mutator);
      assertTrue(source.hasMethod(mutator));
      assertTrue(source.isInterface() || mutator.isPublic());
      assertTrue(mutator.isReturnTypeVoid());
      assertEquals(PropertyComponent.MUTATOR.format(testContext.type, testContext.name), mutator.getName());
      assertEquals(1, mutator.getParameters().size());
      final ParameterSource<O> parameter = mutator.getParameters().get(0);
      assertTrue(parameter.getType().isType(testContext.type));
      assertEquals(testContext.name, parameter.getName());
      assertTrue(!testContext.existingItems.contains(PropertyComponent.FIELD)
               || mutator.getBody().contains(String.format("this.%1$s=%1$s;", testContext.name)));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testCreateFieldAgain(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assertThrows(IllegalStateException.class, () -> source.getProperty(testContext.name).createField());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testRemoveField(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.FIELD));
      O source = testContext.getSource();
      final FieldSource<O> field = source.getProperty(testContext.name).getField();
      assertTrue(source.hasField(field));
      source.getProperty(testContext.name).removeField();
      assertFalse(source.hasField(field));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetAccessibleFalse(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertTrue(property.isAccessible());
      final MethodSource<O> accessor = property.getAccessor();
      assertNotNull(accessor);
      assertTrue(source.hasMethod(accessor));
      property.setAccessible(false);
      assertFalse(property.isAccessible());
      assertFalse(source.hasMethod(accessor));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetMutableFalse(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));

      final PropertySource<O> property = source.getProperty(testContext.name);
      assertTrue(property.isMutable());
      final MethodSource<O> mutator = property.getMutator();
      assertTrue(source.hasMethod(mutator));
      if (testContext.existingItems.contains(PropertyComponent.FIELD))
      {
         assertTrue(property.hasField());
         assertNotNull(property.getField());
         assertFalse(property.getField().isFinal());
      }
      property.setMutable(false);
      assertFalse(source.hasMethod(mutator));

      if (testContext.existingItems.contains(PropertyComponent.FIELD))
      {
         assertTrue(property.hasField());
         assertNotNull(property.getField());
         assertTrue(property.getField().isFinal());
      }
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testAddProperty(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      final PropertySource<O> property = source.addProperty("Whatever", "blah");
      assertEquals(property, source.getProperty("blah"));

      if (!source.isInterface())
      {
         assertTrue(source.hasField("blah"));
         assertEquals("Whatever", source.getField("blah").getType().getName());
         if (source.isEnum())
         {
            assertTrue(source.getField("blah").isFinal());
         }
      }
      final String accessorName = "getBlah";
      assertTrue(source.hasMethodSignature(accessorName));
      final MethodSource<O> accessor = source.getMethod(accessorName);
      assertEquals("Whatever", accessor.getReturnType().getName());
      assertTrue(source.isInterface() || accessor.isPublic());

      if (source.isEnum())
      {
         return;
      }

      final String mutatorName = "setBlah";
      assertTrue(source.hasMethodSignature(mutatorName, "Whatever"));
      final MethodSource<O> mutator = source.getMethod(mutatorName, "Whatever");
      assertEquals("Whatever", mutator.getParameters().get(0).getType().getName());
      assertTrue(source.isInterface() || mutator.isPublic());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testAddPropertyThenChangeType(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      final PropertySource<O> property = source.addProperty("int", "something");
      assertTrue(source.hasMethodSignature("getSomething"));
      assertTrue(source.getMethod("getSomething").getReturnType().isType(int.class));
      property.setType(boolean.class);
      assertTrue(property.getType().isType(boolean.class));
      assertFalse(source.hasMethodSignature("getSomething"));
      assertTrue(source.hasMethodSignature("isSomething"));
      assertTrue(source.getMethod("isSomething").getReturnType().isType(boolean.class));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetName(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      assumeFalse("foo".equals(testContext.name));

      assertEquals(testContext.existingItems.contains(PropertyComponent.FIELD),
               sourceHasPropertyField(source, testContext.name));
      assertEquals(testContext.existingItems.contains(PropertyComponent.ACCESSOR),
               source.getMethod(PropertyComponent.ACCESSOR.format(testContext.type, testContext.name)) != null);
      assertEquals(testContext.existingItems.contains(PropertyComponent.MUTATOR),
               source.getMethod(PropertyComponent.MUTATOR.format(testContext.type, testContext.name), testContext.type)
                        != null);

      final PropertySource<O> property = source.getProperty(testContext.name);

      if (!source.isInterface() && !property.hasField())
      {
         property.createField();
      }
      property.setAccessible(true);

      if (!source.isEnum() && !property.isMutable())
      {
         property.createMutator();
      }

      property.setName("foo");
      assertEquals("foo", property.getName());

      // make sure none of the original items remain:
      assertFalse(
               testContext.existingItems.contains(PropertyComponent.FIELD) && sourceHasPropertyField(source, testContext.name));
      assertFalse(testContext.existingItems.contains(PropertyComponent.ACCESSOR)
               && source.getMethod(PropertyComponent.ACCESSOR.format(testContext.type, testContext.name)) != null);
      assertFalse(testContext.existingItems.contains(PropertyComponent.MUTATOR)
               &&
               source.getMethod(PropertyComponent.MUTATOR.format(testContext.type, testContext.name), testContext.type)
                        != null);

      assertTrue(source.isInterface() || sourceHasPropertyField(source, "foo"));
      assertTrue(source.getMethod(PropertyComponent.ACCESSOR.format(testContext.type, "foo")) != null);
      assertTrue(source.isEnum()
               || source.getMethod(PropertyComponent.MUTATOR.format(testContext.type, "foo"), testContext.type)
               != null);

      if (property.hasField())
      {
         assertTrue(property.getAccessor().getBody().contains("return foo;"));
         assertTrue(!property.isMutable() || property.getMutator().getBody().contains("this.foo=foo;"));
      }
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetTypeClass(TestContext<O> testContext)
   {
      assumeFalse(CharSequence.class.equals(testContext.type));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      property.setType(CharSequence.class);
      assertTrue(property.getType().isType(CharSequence.class));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.FIELD)
               || property.getField().getType().isType(CharSequence.class));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.ACCESSOR)
               || property.getAccessor().getReturnType().isType(CharSequence.class));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.MUTATOR)
               || property.getMutator().getParameters().get(0).getType().isType(CharSequence.class));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetTypeString(TestContext<O> testContext)
   {
      assumeFalse(CharSequence.class.equals(testContext.type));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      property.setType("CharSequence");
      assertEquals("CharSequence", property.getType().getName());
      assertTrue(!testContext.existingItems.contains(PropertyComponent.FIELD)
               || "CharSequence".equals(property.getField().getType().getName()));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.ACCESSOR)
               || "CharSequence".equals(property.getAccessor().getReturnType().getName()));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.MUTATOR)
               || "CharSequence".equals(property.getMutator().getParameters().get(0).getType().getName()));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testSetTypeJavaType(TestContext<O> testContext)
   {
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      property.setType(source);
      assertEquals(source.getQualifiedName(), property.getType().getQualifiedName());
      assertTrue(!testContext.existingItems.contains(PropertyComponent.FIELD)
               || Objects.equals(source.getQualifiedName(), property.getField().getType().getQualifiedName()));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.ACCESSOR)
               || Objects.equals(source.getQualifiedName(),
               property.getAccessor().getReturnType().getQualifiedName()));
      assertTrue(!testContext.existingItems.contains(PropertyComponent.MUTATOR)
               || Objects.equals(source.getQualifiedName(), property.getMutator().getParameters().get(0).getType()
               .getQualifiedName()));
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertySeesChangedAccessor(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertTrue(property.isAccessible());
      property.getAccessor().setName("foo");

      assertFalse(property.isAccessible());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertySeesChangedMutator(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertTrue(property.isMutable());
      property.getMutator().setName("foo");

      assertFalse(property.isMutable());
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyHasAnnotationField(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.FIELD));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class);
      assertTrue(property.hasAnnotation(Deprecated.class));
      property.getField().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyHasAnnotationAccessor(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class);
      assertTrue(property.hasAnnotation(Deprecated.class));
      property.getAccessor().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyHasAnnotationMutator(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class);
      assertTrue(property.hasAnnotation(Deprecated.class));
      property.getMutator().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyHasAnnotationTypeField(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.FIELD));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class.getName());
      assertTrue(property.hasAnnotation(Deprecated.class.getName()));
      property.getField().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyHasAnnotationTypeAccessor(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class.getName());
      assertTrue(property.hasAnnotation(Deprecated.class.getName()));
      property.getAccessor().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyHasAnnotationTypeMutator(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class.getName());
      assertTrue(property.hasAnnotation(Deprecated.class.getName()));
      property.getMutator().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationTypeField(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.FIELD));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class.getName());
      assertNotNull(property.getAnnotation(Deprecated.class.getName()));
      property.getField().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationTypeAccessor(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class.getName());
      assertNotNull(property.getAnnotation(Deprecated.class.getName()));
      property.getAccessor().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationTypeMutator(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class.getName());
      assertNotNull(property.getAnnotation(Deprecated.class.getName()));
      property.getMutator().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationField(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.FIELD));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class);
      assertNotNull(property.getAnnotation(Deprecated.class));
      property.getField().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationAccessor(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class);
      assertNotNull(property.getAnnotation(Deprecated.class));
      property.getAccessor().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationMutator(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class);
      assertNotNull(property.getAnnotation(Deprecated.class));
      property.getMutator().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationsField(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.FIELD));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class);
      assertEquals(1, property.getAnnotations().size());
      property.getField().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationsAccessor(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.ACCESSOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class);
      assertEquals(1, property.getAnnotations().size());
      property.getAccessor().removeAnnotation(ann);
   }

   @ParameterizedTest
   @ArgumentsSource(TestContextArgumentsProvider.class)
   void testPropertyGetAnnotationsMutator(TestContext<O> testContext)
   {
      assumeTrue(testContext.existingItems.contains(PropertyComponent.MUTATOR));
      O source = testContext.getSource();
      final PropertySource<O> property = source.getProperty(testContext.name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class);
      assertEquals(1, property.getAnnotations().size());
      property.getMutator().removeAnnotation(ann);
   }

   private boolean sourceHasPropertyField(O source, String fieldName)
   {
      if (source.isInterface())
      {
         return false;
      }
      final FieldSource<O> field = source.getField(fieldName);
      return !(field == null || field.isStatic());
   }

   public static class TestContextArgumentsProvider implements ArgumentsProvider
   {
      @Override
      public Stream<? extends Arguments> provideArguments(ExtensionContext context)
      {
         return Stream.of(
                  Arguments.of(new TestContext<>(JavaClassSource.class, "MockClass", String.class, "field",
                           EnumSet.of(PropertyComponent.FIELD))),
                  Arguments.of(new TestContext<>(JavaEnumSource.class, "MockEnum", String.class, "field",
                           EnumSet.of(PropertyComponent.FIELD))),
                  Arguments.of(new TestContext<>(JavaInterfaceSource.class, "MockInterface", int.class, "count",
                           EnumSet.of(PropertyComponent.ACCESSOR))),
                  Arguments.of(new TestContext<>(JavaInterfaceSource.class, "BigInterface", boolean.class, "verbose",
                           EnumSet.of(PropertyComponent.ACCESSOR, PropertyComponent.MUTATOR)))
         );
      }
   }

   public static class TestContext<O extends JavaSource<O> & PropertyHolderSource<O>>
   {
      Class<O> sourceType;
      String resourceName;
      Class<?> type;
      String name;
      Set<PropertyComponent> existingItems;

      TestContext(Class<O> sourceType, String resourceName, Class<?> type, String name,
               Set<PropertyComponent> existingItems)
      {
         this.sourceType = sourceType;
         this.resourceName = resourceName;
         this.type = type;
         this.name = name;
         this.existingItems = existingItems;
      }

      O getSource()
      {
         String fileName = String.format("/org/jboss/forge/grammar/java/%s.java", resourceName);
         try (final InputStream stream = JavaClassTest.class.getResourceAsStream(fileName))
         {
            return Roaster.parse(sourceType, stream);
         }
         catch (IOException e)
         {
            throw new UncheckedIOException(e);
         }
      }
   }
}
