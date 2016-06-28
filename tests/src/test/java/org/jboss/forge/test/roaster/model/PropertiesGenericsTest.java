/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class PropertiesGenericsTest
{
   @Test
   public void addGenericAndGetPropertyType()
   {
      JavaClassSource classSource = Roaster.create(JavaClassSource.class);
      classSource.addProperty("java.util.List<java.lang.String>", "list");
      PropertySource<JavaClassSource> property = classSource.getProperty("list");
      Type<JavaClassSource> propertyType = property.getType();
      assertEquals("List<String>", propertyType.toString());
   }

   @Test
   public void addGenericAndGetFieldType()
   {
      JavaClassSource classSource = Roaster.create(JavaClassSource.class);
      classSource.addProperty("java.util.List<java.lang.String>", "list");
      PropertySource<JavaClassSource> property = classSource.getProperty("list");
      FieldSource<JavaClassSource> field = property.getField();
      Type<JavaClassSource> fieldType = field.getType();
      assertEquals("List<String>", fieldType.toString());
   }
}