/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.test.roaster.model;

import java.io.InputStream;
import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaClassPropertyTest
{
   public JavaClassSource getSource()
   {
      InputStream stream = JavaClassTest.class
               .getResourceAsStream("/org/jboss/forge/grammar/java/MockUnformattedClass.java");
      return Roaster.parse(JavaClassSource.class, stream);
   }

   @Test
   public void testProperties()
   {
      List<PropertySource<JavaClassSource>> properties = getSource().getProperties();
      Assert.assertEquals(2, properties.size());
   }

}
