/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class LocationCapableTest
{
   @Test
   public void testFieldLocationInSource()
   {
      JavaClassSource clazz = Roaster.parse(JavaClassSource.class,
               "public class Foo { public String bar; private int age;}");
      FieldSource<JavaClassSource> field = clazz.getField("bar");
      Assert.assertEquals(19, field.getStartPosition());
      Assert.assertEquals(37, field.getEndPosition());
   }

   @Test
   public void testMethodLocationInSource()
   {
      JavaClassSource clazz = Roaster.parse(JavaClassSource.class,
               "public class Foo { public String bar(){return null;}}");
      MethodSource<JavaClassSource> method = clazz.getMethod("bar");
      Assert.assertEquals(19, method.getStartPosition());
      Assert.assertEquals(52, method.getEndPosition());
   }

   @Test
   public void testFieldLineAndColumnNumberInSource()
   {
      JavaClassSource clazz = Roaster.parse(JavaClassSource.class,
               "public class Foo {\n public String bar;\n}");
      FieldSource<JavaClassSource> field = clazz.getField("bar");
      Assert.assertEquals(2, field.getLineNumber());
      Assert.assertEquals(1, field.getColumnNumber());
   }

   @Test
   public void testMethodLineAndColumnNumberInSource()
   {
      JavaClassSource clazz = Roaster.parse(JavaClassSource.class,
               "public class Foo {\n"
                        + " public String method(){\n"
                        + "  return null;\n"
                        + " }\n"
                        + "\n"
                        + " public String bar(){\n"
                        + "  return null;\n"
                        + " }\n"
                        + "}");
      MethodSource<JavaClassSource> method = clazz.getMethod("bar");
      Assert.assertEquals(6, method.getLineNumber());
      Assert.assertEquals(1, method.getColumnNumber());
   }
}
