/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
import org.jboss.forge.roaster.model.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PropertiesTest<O extends JavaSource<O> & PropertyHolderSource<O>>
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
            return (boolean.class.equals(type) ? "is" : "get") + Strings.capitalize(s);
         }
      },
      MUTATOR
      {
         @Override
         String format(Class<?> type, String s)
         {
            return "set" + Strings.capitalize(s);
         }
      };

      abstract String format(Class<?> type, String s);
   }

   @Parameters(name = "{2} {1}.{3}")
   public static List<Object[]> createParameters()
   {
      final List<Object[]> parameters = new ArrayList<>();
      parameters.add(new Object[] { JavaClassSource.class, "MockClass", String.class, "field",
               EnumSet.of(PropertyComponent.FIELD) });
      parameters.add(new Object[] { JavaEnumSource.class, "MockEnum", String.class, "field",
               EnumSet.of(PropertyComponent.FIELD) });
      parameters.add(new Object[] { JavaInterfaceSource.class, "MockInterface", int.class, "count",
               EnumSet.of(PropertyComponent.ACCESSOR) });
      parameters.add(new Object[] { JavaInterfaceSource.class, "BigInterface", boolean.class, "verbose",
               EnumSet.of(PropertyComponent.ACCESSOR, PropertyComponent.MUTATOR) });
      return parameters;
   }

   @Parameter(0)
   public Class<O> sourceType;

   @Parameter(1)
   public String resourceName;

   @Parameter(2)
   public Class<?> type;

   @Parameter(3)
   public String name;

   @Parameter(4)
   public Set<PropertyComponent> existingItems;

   private O source;

   @Before
   public void reset() throws IOException
   {
      String fileName = String.format("/org/jboss/forge/grammar/java/%s.java", resourceName);
      try (final InputStream stream = JavaClassTest.class.getResourceAsStream(fileName))
      {
         source = Roaster.parse(sourceType, stream);
      }
   }

   @Test
   public void testHasProperty()
   {
      assertTrue(source.hasProperty(name));
      assertFalse(source.hasProperty("noSuchProperty"));
   }

   @Test
   public void testGetPropertyByName()
   {
      PropertySource<O> property = source.getProperty(name);
      assertEquals(name, property.getName());
      assertTrue(property.getType().isType(type));
   }

   @Test
   public void testIsAccessible()
   {
      assertEquals(existingItems.contains(PropertyComponent.ACCESSOR), source.getProperty(name).isAccessible());
   }

   @Test
   public void testIsMutable()
   {
      assertEquals(existingItems.contains(PropertyComponent.MUTATOR), source.getProperty(name).isMutable());
   }

   @Test
   public void testHasField()
   {
      assertEquals(existingItems.contains(PropertyComponent.FIELD), source.getProperty(name).hasField());
   }

   @Test
   public void testGetField()
   {
      final FieldSource<O> field = source.getProperty(name).getField();

      if (!existingItems.contains(PropertyComponent.FIELD))
      {
         assertNull(field);
         return;
      }

      assertNotNull(field);
      assertEquals(name, field.getName());
      assertTrue(field.getType().isType(type));
   }

   @Test
   public void testGetAccessor()
   {
      final MethodSource<O> accessor = source.getProperty(name).getAccessor();

      if (!existingItems.contains(PropertyComponent.ACCESSOR))
      {
         assertNull(accessor);
         return;
      }
      assertNotNull(accessor);

      assertEquals(PropertyComponent.ACCESSOR.format(type, name), accessor.getName());
      assertTrue(accessor.getReturnType().isType(type));
   }

   @Test
   public void testGetMutator()
   {
      final MethodSource<O> mutator = source.getProperty(name).getMutator();

      if (!existingItems.contains(PropertyComponent.MUTATOR))
      {
         assertNull(mutator);
         return;
      }
      assertNotNull(mutator);

      assertEquals(PropertyComponent.MUTATOR.format(type, name), mutator.getName());
      assertTrue(mutator.isReturnTypeVoid());
      assertEquals(1, mutator.getParameters().size());
      assertTrue(mutator.getParameters().get(0).getType().isType(type));
   }

   @Test
   public void testSetAccessibleTrue()
   {
      assumeFalse(existingItems.contains(PropertyComponent.ACCESSOR));

      final PropertySource<O> property = source.getProperty(name);
      property.setAccessible(true);
      assertTrue(property.isAccessible());

      final MethodSource<O> accessor = property.getAccessor();
      assertNotNull(accessor);
      assertTrue(source.hasMethod(accessor));
      assertTrue(source.isInterface() || accessor.isPublic());
      assertTrue(accessor.getReturnType().isType(type));
      assertEquals(PropertyComponent.ACCESSOR.format(type, name), accessor.getName());
      assertTrue(accessor.getParameters().isEmpty());
      assertTrue(!existingItems.contains(PropertyComponent.FIELD)
               || accessor.getBody().contains(String.format("return %s;", name)));
   }

   @Test
   public void testSetMutableTrue()
   {
      assumeFalse(existingItems.contains(PropertyComponent.MUTATOR));

      final PropertySource<O> property = source.getProperty(name);
      property.setMutable(true);

      final MethodSource<O> mutator = property.getMutator();
      assertNotNull(mutator);
      assertTrue(source.hasMethod(mutator));
      assertTrue(source.isInterface() || mutator.isPublic());
      assertTrue(mutator.isReturnTypeVoid());
      assertEquals(PropertyComponent.MUTATOR.format(type, name), mutator.getName());
      assertEquals(1, mutator.getParameters().size());
      final ParameterSource<O> parameter = mutator.getParameters().get(0);
      assertTrue(parameter.getType().isType(type));
      assertEquals(name, parameter.getName());
      assertTrue(!existingItems.contains(PropertyComponent.FIELD)
               || mutator.getBody().contains(String.format("this.%1$s=%1$s;", name)));
   }

   @Test(expected = IllegalStateException.class)
   public void testCreateFieldAgain()
   {
      source.getProperty(name).createField();
      assertFalse(existingItems.contains(PropertyComponent.FIELD));
      source.getProperty(name).createField();
   }

   @Test
   public void testRemoveField()
   {
      assumeTrue(existingItems.contains(PropertyComponent.FIELD));

      final FieldSource<O> field = source.getProperty(name).getField();
      assertTrue(source.hasField(field));
      source.getProperty(name).removeField();
      assertFalse(source.hasField(field));
   }

   @Test
   public void testSetAccessibleFalse()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));

      final PropertySource<O> property = source.getProperty(name);
      assertTrue(property.isAccessible());
      final MethodSource<O> accessor = property.getAccessor();
      assertNotNull(accessor);
      assertTrue(source.hasMethod(accessor));
      property.setAccessible(false);
      assertFalse(property.isAccessible());
      assertFalse(source.hasMethod(accessor));
   }

   @Test
   public void testSetMutableFalse()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));

      final PropertySource<O> property = source.getProperty(name);
      assertTrue(property.isMutable());
      final MethodSource<O> mutator = property.getMutator();
      assertTrue(source.hasMethod(mutator));
      if (existingItems.contains(PropertyComponent.FIELD))
      {
         assertTrue(property.hasField());
         assertNotNull(property.getField());
         assertFalse(property.getField().isFinal());
      }
      property.setMutable(false);
      assertFalse(source.hasMethod(mutator));

      if (existingItems.contains(PropertyComponent.FIELD))
      {
         assertTrue(property.hasField());
         assertNotNull(property.getField());
         assertTrue(property.getField().isFinal());
      }
   }

   @Test
   public void testAddProperty()
   {
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

   @Test
   public void testAddPropertyThenChangeType()
   {
      final PropertySource<O> property = source.addProperty("int", "something");
      assertTrue(source.hasMethodSignature("getSomething"));
      assertTrue(source.getMethod("getSomething").getReturnType().isType(int.class));
      property.setType(boolean.class);
      assertTrue(property.getType().isType(boolean.class));
      assertFalse(source.hasMethodSignature("getSomething"));
      assertTrue(source.hasMethodSignature("isSomething"));
      assertTrue(source.getMethod("isSomething").getReturnType().isType(boolean.class));
   }

   @Test
   public void testSetName()
   {
      assumeFalse("foo".equals(name));

      assertEquals(existingItems.contains(PropertyComponent.FIELD), sourceHasPropertyField(name));
      assertEquals(existingItems.contains(PropertyComponent.ACCESSOR),
               source.getMethod(PropertyComponent.ACCESSOR.format(type, name)) != null);
      assertEquals(existingItems.contains(PropertyComponent.MUTATOR),
               source.getMethod(PropertyComponent.MUTATOR.format(type, name), type) != null);

      final PropertySource<O> property = source.getProperty(name);

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
      assertFalse(existingItems.contains(PropertyComponent.FIELD) && sourceHasPropertyField(name));
      assertFalse(existingItems.contains(PropertyComponent.ACCESSOR)
               && source.getMethod(PropertyComponent.ACCESSOR.format(type, name)) != null);
      assertFalse(existingItems.contains(PropertyComponent.MUTATOR)
               && source.getMethod(PropertyComponent.MUTATOR.format(type, name), type) != null);

      assertTrue(source.isInterface() || sourceHasPropertyField("foo"));
      assertTrue(source.getMethod(PropertyComponent.ACCESSOR.format(type, "foo")) != null);
      assertTrue(source.isEnum() || source.getMethod(PropertyComponent.MUTATOR.format(type, "foo"), type) != null);

      if (property.hasField())
      {
         assertTrue(property.getAccessor().getBody().contains("return foo;"));
         assertTrue(!property.isMutable() || property.getMutator().getBody().contains("this.foo=foo;"));
      }
   }

   @Test
   public void testSetTypeClass()
   {
      assumeFalse(CharSequence.class.equals(type));
      final PropertySource<O> property = source.getProperty(name);
      property.setType(CharSequence.class);
      assertTrue(property.getType().isType(CharSequence.class));
      assertTrue(!existingItems.contains(PropertyComponent.FIELD)
               || property.getField().getType().isType(CharSequence.class));
      assertTrue(!existingItems.contains(PropertyComponent.ACCESSOR)
               || property.getAccessor().getReturnType().isType(CharSequence.class));
      assertTrue(!existingItems.contains(PropertyComponent.MUTATOR)
               || property.getMutator().getParameters().get(0).getType().isType(CharSequence.class));
   }

   @Test
   public void testSetTypeString()
   {
      assumeFalse(CharSequence.class.equals(type));
      final PropertySource<O> property = source.getProperty(name);
      property.setType("CharSequence");
      assertEquals("CharSequence", property.getType().getName());
      assertTrue(!existingItems.contains(PropertyComponent.FIELD)
               || "CharSequence".equals(property.getField().getType().getName()));
      assertTrue(!existingItems.contains(PropertyComponent.ACCESSOR)
               || "CharSequence".equals(property.getAccessor().getReturnType().getName()));
      assertTrue(!existingItems.contains(PropertyComponent.MUTATOR)
               || "CharSequence".equals(property.getMutator().getParameters().get(0).getType().getName()));
   }

   @Test
   public void testSetTypeJavaType()
   {
      final PropertySource<O> property = source.getProperty(name);
      property.setType(source);
      assertEquals(source.getQualifiedName(), property.getType().getQualifiedName());
      assertTrue(!existingItems.contains(PropertyComponent.FIELD)
               || Strings.areEqual(source.getQualifiedName(), property.getField().getType().getQualifiedName()));
      assertTrue(!existingItems.contains(PropertyComponent.ACCESSOR)
               || Strings
                        .areEqual(source.getQualifiedName(),
                                 property.getAccessor().getReturnType().getQualifiedName()));
      assertTrue(!existingItems.contains(PropertyComponent.MUTATOR)
               || Strings.areEqual(source.getQualifiedName(), property.getMutator().getParameters().get(0).getType()
                        .getQualifiedName()));
   }

   @Test
   public void testPropertySeesChangedAccessor()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));

      final PropertySource<O> property = source.getProperty(name);
      assertTrue(property.isAccessible());
      property.getAccessor().setName("foo");

      assertFalse(property.isAccessible());
   }

   @Test
   public void testPropertySeesChangedMutator()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));

      final PropertySource<O> property = source.getProperty(name);
      assertTrue(property.isMutable());
      property.getMutator().setName("foo");

      assertFalse(property.isMutable());
   }

   @Test
   public void testPropertyHasAnnotationField()
   {
      assumeTrue(existingItems.contains(PropertyComponent.FIELD));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class);
      assertTrue(property.hasAnnotation(Deprecated.class));
      property.getField().removeAnnotation(ann);
   }

   @Test
   public void testPropertyHasAnnotationAccessor()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class);
      assertTrue(property.hasAnnotation(Deprecated.class));
      property.getAccessor().removeAnnotation(ann);
   }

   @Test
   public void testPropertyHasAnnotationMutator()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class);
      assertTrue(property.hasAnnotation(Deprecated.class));
      property.getMutator().removeAnnotation(ann);
   }

   @Test
   public void testPropertyHasAnnotationTypeField()
   {
      assumeTrue(existingItems.contains(PropertyComponent.FIELD));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class.getName());
      assertTrue(property.hasAnnotation(Deprecated.class.getName()));
      property.getField().removeAnnotation(ann);
   }

   @Test
   public void testPropertyHasAnnotationTypeAccessor()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class.getName());
      assertTrue(property.hasAnnotation(Deprecated.class.getName()));
      property.getAccessor().removeAnnotation(ann);
   }

   @Test
   public void testPropertyHasAnnotationTypeMutator()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class.getName());
      assertTrue(property.hasAnnotation(Deprecated.class.getName()));
      property.getMutator().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationTypeField()
   {
      assumeTrue(existingItems.contains(PropertyComponent.FIELD));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class.getName());
      assertNotNull(property.getAnnotation(Deprecated.class.getName()));
      property.getField().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationTypeAccessor()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class.getName());
      assertNotNull(property.getAnnotation(Deprecated.class.getName()));
      property.getAccessor().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationTypeMutator()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class.getName()));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class.getName());
      assertNotNull(property.getAnnotation(Deprecated.class.getName()));
      property.getMutator().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationField()
   {
      assumeTrue(existingItems.contains(PropertyComponent.FIELD));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class);
      assertNotNull(property.getAnnotation(Deprecated.class));
      property.getField().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationAccessor()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class);
      assertNotNull(property.getAnnotation(Deprecated.class));
      property.getAccessor().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationMutator()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class);
      assertNotNull(property.getAnnotation(Deprecated.class));
      property.getMutator().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationsField()
   {
      assumeTrue(existingItems.contains(PropertyComponent.FIELD));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getField().addAnnotation(Deprecated.class);
      assertEquals(1, property.getAnnotations().size());
      property.getField().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationsAccessor()
   {
      assumeTrue(existingItems.contains(PropertyComponent.ACCESSOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getAccessor().addAnnotation(Deprecated.class);
      assertEquals(1, property.getAnnotations().size());
      property.getAccessor().removeAnnotation(ann);
   }

   @Test
   public void testPropertyGetAnnotationsMutator()
   {
      assumeTrue(existingItems.contains(PropertyComponent.MUTATOR));
      final PropertySource<O> property = source.getProperty(name);
      assertFalse(property.hasAnnotation(Deprecated.class));

      AnnotationSource<O> ann = property.getMutator().addAnnotation(Deprecated.class);
      assertEquals(1, property.getAnnotations().size());
      property.getMutator().removeAnnotation(ann);
   }

   private boolean sourceHasPropertyField(String fieldName)
   {
      if (source.isInterface())
      {
         return false;
      }
      final FieldSource<O> field = source.getField(fieldName);
      return !(field == null || field.isStatic());
   }
}
